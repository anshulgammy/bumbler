package com.utopiannerd.twitter.streaming.analytics;

import static com.utopiannerd.twitter.streaming.analytics.util.ConfigurationConstants.BLOCKING_QUEUE_SIZE;

import com.google.common.collect.Lists;
import com.utopiannerd.twitter.streaming.analytics.producer.TwitterRawDataProducer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterStreamingAnalyticsApplication {

  public static void main(String[] args) {

    BlockingQueue<String> twitterBlockingQueue = new LinkedBlockingQueue<>(BLOCKING_QUEUE_SIZE);
    List<String> targetTrackTerms = Arrays.asList("coronavirus");

    TwitterRawDataProducer twitterRawDataProducer = new TwitterRawDataProducer(twitterBlockingQueue, targetTrackTerms);
    twitterRawDataProducer.produce();
  }
}
