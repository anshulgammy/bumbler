package com.utopiannerd.netflix.titles.analytics.stream;

import static com.utopiannerd.netflix.titles.analytics.util.KafkaUtil.NON_NULL_OR_EMPTY_CHECK_MESSAGE;
import static com.utopiannerd.netflix.titles.analytics.util.KafkaUtil.createKafkaStreamConfigurationMap;
import static com.utopiannerd.netflix.titles.analytics.util.KafkaUtil.shutdownKafkaResource;
import static java.util.Objects.requireNonNull;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetflixKafkaStreamListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(NetflixKafkaStreamListener.class);

  private final String topicName;
  private final KStream<String, String> kStream;
  private final KafkaStreams kafkaStreams;

  public NetflixKafkaStreamListener(String topicName) {

    this.topicName = topicName;
    Preconditions.checkArgument(
        StringUtils.isNotBlank(topicName), NON_NULL_OR_EMPTY_CHECK_MESSAGE.apply("topicName"));

    StreamsBuilder streamsBuilder = new StreamsBuilder();
    this.kStream =
        requireNonNull(
            streamsBuilder.stream(topicName), NON_NULL_OR_EMPTY_CHECK_MESSAGE.apply("kStream"));

    Topology topology = streamsBuilder.build();
    StreamsConfig streamsConfig = new StreamsConfig(createKafkaStreamConfigurationMap());
    this.kafkaStreams =
        requireNonNull(
            new KafkaStreams(topology, streamsConfig),
            NON_NULL_OR_EMPTY_CHECK_MESSAGE.apply("kafkaStreams"));
  }

  public void processStream() {

    LOGGER.info("processStream started");

    /*KStream<String, NetflixTitle> transformedKStream =
    this.kStream.mapValues(
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
        });*/

    kStream.peek(
        (key, netflixTitle) -> {
          System.out.println("Hello");
          LOGGER.info("Consumer received record | Key: {} Value: {}", key, netflixTitle);
        });

    this.kafkaStreams.start();

    shutdownKafkaResource(this.kafkaStreams);

    LOGGER.info("processStream completed");
  }
}
