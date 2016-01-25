package com.tale.uder.models;

import android.support.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.tale.uder.api.UderRestApi;
import com.tale.uder.api.entities.DirectionResponse;
import com.tale.uder.api.entities.Leg;
import com.tale.uder.api.entities.Route;
import com.tale.uder.api.entities.Step;
import java.util.ArrayList;
import java.util.List;
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
public class DirectionModel {

  @NonNull private final UderRestApi uderRestApi;

  public DirectionModel(@NonNull UderRestApi uderRestApi) {
    this.uderRestApi = uderRestApi;
  }

  @NonNull
  public Single<List<LatLng>> getDirection(String originPlaceId, String destinationPlaceId) {
    final String origin = String.format("place_id:%s", originPlaceId);
    final String destination = String.format("place_id:%s", destinationPlaceId);
    final Single<DirectionResponse> directionResponseSingle =
        uderRestApi.getDirection(origin, destination, Constants.API_KEY);
    return directionResponseSingle.flatMap(
        directionResponse -> calculatePoints(directionResponse.routes));
  }

  /**
   * Convert route info into a linear list of points which can display on map.
   *
   * @param routes routes from response.
   * @return a {@link List} of {@link LatLng} objects.
   */
  Single<List<LatLng>> calculatePoints(List<Route> routes) {
    if (routes == null || routes.isEmpty()) {
      return Single.error(new NoSuchElementException("No routes result"));
    }
    final Route route = routes.get(0);
    Leg selectedLeg = selectShortestLeg(route.legs);
    if (selectedLeg.steps == null || selectedLeg.steps.size() == 0) {
      return Single.error(new NoSuchElementException("No step"));
    }
    final int size = selectedLeg.steps.size();
    final List<LatLng> result = new ArrayList<>(size + 1);
    for (int i = 0; i < size; i++) {
      final Step step = selectedLeg.steps.get(i);
      result.add(new LatLng(step.startLocation.lat, step.startLocation.lng));
      if (i == size - 1) {
        result.add(new LatLng(step.endLocation.lat, step.endLocation.lng));
      }
    }
    return Single.just(result);
  }

  Leg selectShortestLeg(List<Leg> legs) {
    if (legs == null || legs.isEmpty()) {
      return null;
    }
    final int size = legs.size();
    if (size == 1) {
      return legs.get(0);
    }
    Leg result = legs.get(0);
    for (int i = 1; i < size; i++) {
      final Leg leg = legs.get(i);
      if (leg.distance.value < result.distance.value) {
        result = leg;
      }
    }
    return result;
  }
}
