/**
 * uder
 *
 * Created by Giang Nguyen on 1/25/16.
 * Copyright (c) 2016 Umbala. All rights reserved.
 */

package com.tale.uder.api.entities;

import android.support.annotation.NonNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry {
  @JsonProperty("location") public Location location;

  /**
   * Default constructor for parsing and testing.
   */
  public Geometry() {
    // no-op
  }

  public Geometry(@NonNull Location location) {
    this.location = location;
  }

}
