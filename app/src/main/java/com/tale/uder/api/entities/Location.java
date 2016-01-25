/**
 * uder
 *
 * Created by Giang Nguyen on 1/24/16.
 * Copyright (c) 2016 Umbala. All rights reserved.
 */

package com.tale.uder.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
  @JsonProperty("lat") public double lat;
  @JsonProperty("lng") public double lng;


  /**
   * Default constructor for parsing and testing.
   */
  public Location() {
    // no-op
  }

  public Location(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }
}
