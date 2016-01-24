package com.tale.uder.api;

import android.support.annotation.NonNull;
import com.tale.uder.api.entities.DirectionResponse;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Single;

public interface UderRestApi {

  @GET("/maps/api/directions/json") @NonNull Single<DirectionResponse> getDirection(
      @Query("origin") String origin, @Query("destination") String destination,
      @Query("key") String key);

}
