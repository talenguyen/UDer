/**
 * uder
 *
 * Created by Giang Nguyen on 1/23/16.
 * Copyright (c) 2016 Umbala. All rights reserved.
 */

package com.tale.uder.ui.main;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.tale.uder.models.AnalyticsModel;
import com.tale.uder.models.DirectionModel;
import com.tale.uder.ui.presenters.Presenter;
import java.util.List;
import javax.inject.Inject;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainPresenter extends Presenter<MainView> {

  @NonNull private final AnalyticsModel analyticsModel;
  @NonNull private final DirectionModel directionModel;
  private Place destPlace;
  private Place orgPlace;

  /**
   * Default constructor for inject.
   */
  @Inject public MainPresenter(@NonNull AnalyticsModel analyticsModel,
      @NonNull DirectionModel directionModel) {
    this.analyticsModel = analyticsModel;
    this.directionModel = directionModel;
  }

  public void setFrom(Place place) {
    final MainView view = view();
    if (view != null) {
      final CharSequence address = place.getAddress();
      view.showFromAddress(address);
      view.showFromLocation(place.getLatLng());
      analyticsModel.sendEvent(AnalyticsModel.EvenBuilder.pickFrom(address));
    }
    orgPlace = place;
    checkAndUpdateDirection();
  }

  public void setTo(Place place) {
    final MainView view = view();
    if (view != null) {
      final CharSequence address = place.getAddress();
      view.showToAddress(address);
      view.showToLocation(place.getLatLng());
      analyticsModel.sendEvent(AnalyticsModel.EvenBuilder.pickTo(address));
    }
    destPlace = place;
    checkAndUpdateDirection();
  }

  private void checkAndUpdateDirection() {
    if (orgPlace != null && destPlace != null) {
      Single<List<LatLng>> direction =
          directionModel.getDirection(orgPlace.getId(), destPlace.getId());
      if (!test()) {
        direction = direction.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
      }
      final Subscription subscription =
          direction.subscribe(this::onDirectionLoaded, this::onDirectionError);
      unsubscribeOnUnbindView(subscription);
    }
  }

  @VisibleForTesting boolean test() {
    return false;
  }

  private void onDirectionError(Throwable throwable) {
    analyticsModel.sendError("error_direction", throwable);
    final MainView view = view();
    if (view != null) {
      view.showLoadDirectionError();
    }
  }

  private void onDirectionLoaded(List<LatLng> points) {
    final MainView view = view();
    if (view != null) {
      view.showDirection(points);
    }
  }

  public void onSearchPlaceError(Status status) {
    analyticsModel.sendError(status.getStatusMessage(),
        new RuntimeException(status.getStatusMessage()));
  }

  public void onError(String message, Exception e) {
    analyticsModel.sendError(message, e);
  }
}
