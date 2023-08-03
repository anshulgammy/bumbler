package com.utopiannerd.twitter.streaming.analytics.util;

public final class ConfigurationConstants {

  // Kafka Producer configuration starts.
  public static final String BOOTSTRAP_SERVERS = "127.0.0.1:9092";
  public static final String RAW_DATA_TOPIC = "raw-data-topic";
  public static final String ACKS_CONFIG = "all";
  public static final String MAX_IN_FLIGHT_CONN = "5";
  public static final String COMPRESSION_TYPE = "snappy";
  public static final String RETRIES_CONFIG = Integer.toString(Integer.MAX_VALUE);
  public static final String LINGER_CONFIG = "20";
  public static final String BATCH_SIZE = Integer.toString(32 * 1024);
  // Kafka Producer configuration ends.

  // Twitter Client configuration starts.
  public static final String CONSUMER_KEYS = "H8***42";
  public static final String CONSUMER_SECRETS = "xn***dI";
  public static final String SECRET = "SPz***Xg";
  public static final String TOKEN = "13***1";
  public static final String CLIENT_NAME = "anshulgammy-hosebird-client";
  public static final int BLOCKING_QUEUE_SIZE=30;
  // Twitter Client configuration ends.

}
