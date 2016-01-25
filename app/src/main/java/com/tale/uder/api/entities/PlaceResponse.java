/**
 * uder
 *
 * Created by Giang Nguyen on 1/25/16.
 * Copyright (c) 2016 Umbala. All rights reserved.
 */

package com.tale.uder.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceResponse {
  @JsonProperty("results") public List<Address> results;
}
