package com.utopiannerd.netflix.titles.analytics.serdes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class JsonSerializer<T> implements Serializer<T> {
  private final Gson gson = new GsonBuilder().create();

  // Providing default constructor needed by Kafka
  public JsonSerializer() {}

  @Override
  public void configure(Map<String, ?> props, boolean isKey) {}

  @Override
  public byte[] serialize(String topic, T data) {
    if (data == null) return null;

    try {
      return gson.toJson(data).getBytes(StandardCharsets.UTF_8);
    } catch (Exception ex) {
      throw new SerializationException("Error serializing JSON message", ex);
    }
  }

  @Override
  public void close() {}
}
