package com.utopiannerd.netflix.titles.analytics.stream;

import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.SANITIZED_DATA_TOPIC;
import static com.utopiannerd.netflix.titles.analytics.util.KafkaUtil.NON_NULL_OR_EMPTY_CHECK_MESSAGE;
import static com.utopiannerd.netflix.titles.analytics.util.KafkaUtil.createKafkaStreamConfigurationMap;
import static com.utopiannerd.netflix.titles.analytics.util.KafkaUtil.shutdownKafkaResource;

import com.google.common.base.Preconditions;
import com.utopiannerd.netflix.titles.analytics.model.NetflixTitle;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NetflixKafkaStreamListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(NetflixKafkaStreamListener.class);

  private static final List<String> CENSORED_KEYWORDS = Arrays.asList("Blood", "Game");
  private static final String MASKED_STRING = "***";

  private final String topicName;

  public NetflixKafkaStreamListener(String topicName) {
    this.topicName = topicName;
    Preconditions.checkArgument(
        StringUtils.isNotBlank(topicName), NON_NULL_OR_EMPTY_CHECK_MESSAGE.apply("topicName"));
  }

  public void processStream() {

    LOGGER.info("NetflixKafkaStreamListener.processStream() started");

    StreamsBuilder streamsBuilder = new StreamsBuilder();

    // Step 1: Getting the raw data KStream.
    KStream<String, String> kStream = streamsBuilder.stream(topicName);

    // Step 2: Using the raw data KStream to preparing transformed KStream of KStream<String,
    // NetflixTitle>.
    KStream<String, NetflixTitle> transformedKStream =
        kStream.mapValues(
            (key, value) -> {
              String[] netflixTitleDataArray = value.split(",");
              NetflixTitle netflixTitle =
                  NetflixTitle.builder()
                      .setShowId(netflixTitleDataArray[0])
                      .setType(netflixTitleDataArray[1])
                      .setTitle(netflixTitleDataArray[2])
                      .setDirector(netflixTitleDataArray[3])
                      .setCast(netflixTitleDataArray[4])
                      .setCountry(netflixTitleDataArray[5])
                      .setDateAdded(netflixTitleDataArray[6])
                      .setReleaseYear(netflixTitleDataArray[7])
                      .setRating(netflixTitleDataArray[8])
                      .setDuration(netflixTitleDataArray[9])
                      .setListedIn(netflixTitleDataArray[10])
                      .setDescription(String.valueOf(11))
                      .build();
              return netflixTitle;
            });

    transformedKStream
        // Step 3: Filter all such titles which are listed under type "TV Show".
        .filter((key, value) -> StringUtils.containsIgnoreCase(value.getType(), "TV Show"))
        // Step 4: Replace censored words present in title name with masked characters.
        .map(
            (key, value) -> {
              AtomicReference<String> titleName = new AtomicReference<>(value.getTitle());
              CENSORED_KEYWORDS.forEach(
                  censoredKeyword -> {
                    if (StringUtils.containsIgnoreCase(titleName.get(), censoredKeyword)) {
                      titleName.set(
                          StringUtils.replace(titleName.get(), censoredKeyword, MASKED_STRING));
                    }
                  });
              return KeyValue.pair(
                  key, NetflixTitle.builder(value).setTitle(titleName.get()).build().toString());
            })
        .peek(
            (key, value) ->
                LOGGER.info("transformedKStream received record | Key: {} Value: {}", key, value))
        // Step 5: Output the transformed stream to sanitized-data-topic.
        .to(SANITIZED_DATA_TOPIC);

    // Doing stateful transformation and finding the count to TV Show/Movie received in the
    // configured tumbling time window.
    transformedKStream
        .map((key, value) -> KeyValue.pair(value.getType(), value.toString()))
        .groupByKey()
        .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(60)))
        .count(Materialized.with(Serdes.String(), Serdes.Long()))
        .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
        .toStream()
        .peek(
            (key, value) ->
                LOGGER.info(
                    "windowedBy result key: {} and value: {}", key.key(), value.toString()));

    KafkaStreams kafkaStreams =
        new KafkaStreams(
            streamsBuilder.build(), new StreamsConfig(createKafkaStreamConfigurationMap()));
    kafkaStreams.start();

    shutdownKafkaResource(kafkaStreams);

    LOGGER.info("NetflixKafkaStreamListener.processStream() completed");
  }
}
