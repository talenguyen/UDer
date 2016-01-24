/**
 * uder
 *
 * Created by Giang Nguyen on 1/24/16.
 * Copyright (c) 2016 Umbala. All rights reserved.
 */

package com.tale.uder.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Leg {
  @JsonProperty("distance") public Distance distance;
  @JsonProperty("steps") public List<Step> steps;
}
