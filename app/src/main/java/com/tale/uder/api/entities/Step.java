/**
 * uder
 *
 * Created by Giang Nguyen on 1/24/16.
 * Copyright (c) 2016 Umbala. All rights reserved.
 */

package com.tale.uder.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Step {
  @JsonProperty("start_location") public Location startLocation;
  @JsonProperty("end_location") public Location endLocation;
}
