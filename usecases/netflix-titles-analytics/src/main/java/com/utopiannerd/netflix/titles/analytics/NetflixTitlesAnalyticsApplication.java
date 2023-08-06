package com.utopiannerd.netflix.titles.analytics;

import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.RAW_DATA_TOPIC;

import com.utopiannerd.netflix.titles.analytics.producer.NetflixKafkaProducer;
import com.utopiannerd.netflix.titles.analytics.stream.NetflixKafkaStreamListener;
import com.utopiannerd.netflix.titles.analytics.stream.NetflixTitlesStreamingAgent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetflixTitlesAnalyticsApplication {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(NetflixTitlesAnalyticsApplication.class);

  public static void main(String[] args) {

    LOGGER.info("NetflixTitlesAnalyticsApplication started");

    NetflixKafkaProducer netflixRawDataProducer = new NetflixKafkaProducer(RAW_DATA_TOPIC);

    NetflixTitlesStreamingAgent netflixTitlesStreamingAgent =
        new NetflixTitlesStreamingAgent(netflixRawDataProducer);

    // Run streaming agent in a separate thread.
    CompletableFuture<Void> streamerAgentFuture =
        CompletableFuture.runAsync(
            () -> {
              try {
                netflixTitlesStreamingAgent.stream();
              } catch (URISyntaxException ex) {
                LOGGER.error("Error occurred while streaming Netflix Titles", ex);
                throw new RuntimeException(ex);
              } catch (IOException ex) {
                LOGGER.error("Error occurred while streaming Netflix Titles", ex);
                throw new RuntimeException(ex);
              }
            });

    // Custom Kafka Consumer.
    // NetflixKafkaConsumer netflixRawDataConsumer = new NetflixKafkaConsumer(RAW_DATA_TOPIC);
    // netflixRawDataConsumer.consume();

    // Custom KafkaStream which will do filter and transformation on data coming from raw-data-topic
    // and then write to sanitized-data-topic.
    NetflixKafkaStreamListener netflixKafkaStreamListener =
        new NetflixKafkaStreamListener(RAW_DATA_TOPIC);
    netflixKafkaStreamListener.processStream();

    // Custom KafkaStream which will read from sanitized-data-topic and peek the elements.
    /*NetflixKafkaTransformedStreamListener netflixKafkaTransformedStreamListener =
        new NetflixKafkaTransformedStreamListener(SANITIZED_DATA_TOPIC);
    netflixKafkaTransformedStreamListener.processStream();*/

    // As long as NetflixTitlesStreamingAgent keeps pushing to the raw-data-topic, the application
    // will keep on running.
    streamerAgentFuture.join();

    LOGGER.info("NetflixTitlesAnalyticsApplication completed");
  }
}
