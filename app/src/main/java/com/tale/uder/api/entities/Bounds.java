/**
 * uder
 *
 * Created by Giang Nguyen on 1/24/16.
 * Copyright (c) 2016 Umbala. All rights reserved.
 */

package com.tale.uder.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bounds {
  @JsonProperty("northeast") public Location northeast;
  @JsonProperty("southwest") public Location southwest;
}
