package com.utopiannerd.netflix.titles.analytics;

import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.RAW_DATA_TOPIC;

import com.utopiannerd.netflix.titles.analytics.consumer.NetflixKafkaConsumer;
import com.utopiannerd.netflix.titles.analytics.producer.NetflixKafkaProducer;
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
    CompletableFuture<Void> streamerFuture =
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

    NetflixKafkaConsumer netflixRawDataConsumer = new NetflixKafkaConsumer(RAW_DATA_TOPIC);

    // Run raw data consumer in a separate thread.
    CompletableFuture<Void> consumerFuture =
        CompletableFuture.runAsync(() -> netflixRawDataConsumer.consume());

    // Joining the threads so that main thread doesn't end.
    CompletableFuture<Void> combinedFutures =
        CompletableFuture.allOf(streamerFuture, consumerFuture);
    combinedFutures.join();

    LOGGER.info("NetflixTitlesAnalyticsApplication completed");
  }
}
