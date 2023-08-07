package com.utopiannerd.netflix.titles.analytics.stream;

import static com.utopiannerd.netflix.titles.analytics.util.KafkaUtil.NON_NULL_OR_EMPTY_CHECK_MESSAGE;
import static com.utopiannerd.netflix.titles.analytics.util.KafkaUtil.createKafkaTransformedStreamConfigurationMap;
import static com.utopiannerd.netflix.titles.analytics.util.KafkaUtil.shutdownKafkaResource;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NetflixKafkaTransformedStreamListener {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(NetflixKafkaTransformedStreamListener.class);

  private final String topicName;

  public NetflixKafkaTransformedStreamListener(String topicName) {
    this.topicName = topicName;
    Preconditions.checkArgument(
        StringUtils.isNotBlank(topicName), NON_NULL_OR_EMPTY_CHECK_MESSAGE.apply("topicName"));
  }

  public void processStream() {

    LOGGER.info("NetflixKafkaTransformedStreamListener.processStream() started");

    StreamsBuilder streamsBuilder = new StreamsBuilder();

    // Step 1: Getting the raw data KStream.
    KStream<String, String> kStream = streamsBuilder.stream(topicName);

    kStream.foreach(
        (key, value) ->
            LOGGER.info(
                "Record from sanitized-data-topic received | Key: {} Value: {}", key, value));

    KafkaStreams kafkaStreams =
        new KafkaStreams(
            streamsBuilder.build(),
            new StreamsConfig(createKafkaTransformedStreamConfigurationMap()));
    kafkaStreams.start();

    shutdownKafkaResource(kafkaStreams);

    LOGGER.info("NetflixKafkaTransformedStreamListener.processStream() completed");
  }
}
