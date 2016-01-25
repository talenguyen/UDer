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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

// This class is immutable, it has correctly implemented hashCode and equals.
// Thanks to AutoValue https://github.com/google/auto/tree/master/value.
@AutoValue
@JsonDeserialize(builder = AutoValue_Address.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Address {

  private static final String JSON_PROPERTY_PLACE_ID = "place_id";
  private static final String JSON_PROPERTY_FORMATTED_ADDRESS = "formatted_address";
  private static final String JSON_PROPERTY_GEOMETRY = "geometry";

  @NonNull
  public static Builder builder() {
    return new AutoValue_Address.Builder();
  }

  @NonNull
  @JsonProperty(JSON_PROPERTY_PLACE_ID)
  public abstract String placeId();

  @NonNull
  @JsonProperty(JSON_PROPERTY_FORMATTED_ADDRESS)
  public abstract String formattedAddress();

  @NonNull
  @JsonProperty(JSON_PROPERTY_GEOMETRY)
  public abstract Geometry geometry();

  @AutoValue.Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static abstract class Builder {

    @NonNull
    @JsonProperty(JSON_PROPERTY_PLACE_ID)
    public abstract Builder placeId(@NonNull String id);

    @NonNull
    @JsonProperty(JSON_PROPERTY_FORMATTED_ADDRESS)
    public abstract Builder formattedAddress(@NonNull String address);

    @NonNull
    @JsonProperty(JSON_PROPERTY_GEOMETRY)
    public abstract Builder geometry(@NonNull Geometry geometry);

    @NonNull
    public abstract Address build();
  }
}
