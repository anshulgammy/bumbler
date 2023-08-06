package com.utopiannerd.netflix.titles.analytics.serdes;

import com.utopiannerd.netflix.titles.analytics.model.NetflixTitle;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public final class CustomSerdes {

  private CustomSerdes() {}

  public static Serde<NetflixTitle> netflixTitleSerdes() {
    JsonSerializer<NetflixTitle> serializer = new JsonSerializer<>();
    JsonDeserializer<NetflixTitle> deserializer = new JsonDeserializer<>(NetflixTitle.class);
    return Serdes.serdeFrom(serializer, deserializer);
  }
}
