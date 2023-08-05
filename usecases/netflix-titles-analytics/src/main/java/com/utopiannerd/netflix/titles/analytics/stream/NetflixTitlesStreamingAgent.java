package com.utopiannerd.netflix.titles.analytics.stream;

import static com.utopiannerd.netflix.titles.analytics.configuration.KafkaConfiguration.NETFLIX_TITLES_CSV_FILE;
import static com.utopiannerd.netflix.titles.analytics.util.KafkaUtil.NON_NULL_OR_EMPTY_CHECK_MESSAGE;
import static java.util.Objects.requireNonNull;

import com.utopiannerd.netflix.titles.analytics.producer.NetflixKafkaProducer;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class NetflixTitlesStreamingAgent {

  private final NetflixKafkaProducer netflixKafkaProducer;

  public NetflixTitlesStreamingAgent(NetflixKafkaProducer netflixKafkaProducer) {
    this.netflixKafkaProducer =
        requireNonNull(
            netflixKafkaProducer, NON_NULL_OR_EMPTY_CHECK_MESSAGE.apply("netflixKafkaProducer"));
  }

  public void stream() throws URISyntaxException, IOException {

    Stream<String> fileStream =
        Files.lines(
            Path.of(
                ClassLoader.getSystemClassLoader().getResource(NETFLIX_TITLES_CSV_FILE).toURI()));

    fileStream
        .skip(1)
        .forEach(
            line -> {
              try {
                netflixKafkaProducer.produce(line);
                // Intentionally sleeping the thread, to simulate the scenario where kafka topic is
                // getting data after every 1 second.
                Thread.sleep(1000);
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            });
  }
}
