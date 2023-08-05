package com.utopiannerd.netflix.titles.analytics.configuration;

public final class KafkaConfiguration {
  public static final String BOOTSTRAP_SERVERS = "[::1]:9092";
  public static final String RAW_DATA_TOPIC = "raw-data-topic";
  public static final String SANITIZED_DATA_TOPIC = "sanitized-data-topic";
  public static final String ACKS_CONFIG = "all";
  public static final String MAX_IN_FLIGHT_CONN = "5";
  public static final String COMPRESSION_TYPE = "snappy";
  public static final String RETRIES_CONFIG = Integer.toString(Integer.MAX_VALUE);
  public static final String LINGER_CONFIG = "20";
  public static final String BATCH_SIZE = Integer.toString(32 * 1024);
  public static final String GROUP_ID = "group-id";
  public static final String NETFLIX_TITLES_CSV_FILE = "netflix_titles.csv";
}
