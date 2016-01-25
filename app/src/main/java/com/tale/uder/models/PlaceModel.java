package com.tale.uder.models;

import android.support.annotation.NonNull;
import com.tale.uder.api.UderRestApi;
import com.tale.uder.api.entities.Address;
import com.tale.uder.api.entities.PlaceResponse;
import java.util.NoSuchElementException;
import rx.Single;

/**
 * Model is not an entity. It's a container for business logic code. M(VC), M(VP), M(VVM).
 * <p>
 * Why create Model classes? Such classes hide complex logic required to fetch/cache/process/etc
 * data.
 * So Presentation layer won't know the details of implementation and each class will do only one
 * job (SOLID).
 */
public class PlaceModel {

  @NonNull private final UderRestApi uderRestApi;

  public PlaceModel(@NonNull UderRestApi uderRestApi) {
    this.uderRestApi = uderRestApi;
  }

  @NonNull public Single<Address> queryPlace(double lat, double lng) {
    final Single<PlaceResponse> placeResponseSingle =
        uderRestApi.queryPlace(String.format("%f, %f", lat, lng), Constants.API_KEY);
    return placeResponseSingle.flatMap(this::getAddressFromResponse);
  }

  Single<Address> getAddressFromResponse(PlaceResponse placeResponse) {
    return Single.defer(() -> {
      if (placeResponse.results == null || placeResponse.results.isEmpty()) {
        return Single.error(new NoSuchElementException("No Result"));
      }
      return Single.just(placeResponse.results.get(0));
    });
  }
}
