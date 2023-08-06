package com.utopiannerd.netflix.titles.analytics.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NetflixTitleAggregation {

  private final Map<String, List<String>> AGGREGATION_MAP = new HashMap<>();

  public void updateAggregation(String titleType, String netflixTitleString) {
    List<String> netflixTitleStringList = this.AGGREGATION_MAP.get(titleType);
    if (netflixTitleStringList == null || netflixTitleStringList.size() == 0) {
      netflixTitleStringList = new ArrayList<>();
    }
    netflixTitleStringList.add(netflixTitleString);
    this.AGGREGATION_MAP.put(titleType, netflixTitleStringList);
  }
}
