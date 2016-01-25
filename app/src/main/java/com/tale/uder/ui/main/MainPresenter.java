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
import com.google.android.gms.maps.model.LatLng;
import com.tale.uder.api.entities.Address;
import com.tale.uder.api.entities.Location;
import com.tale.uder.models.AnalyticsModel;
import com.tale.uder.models.DirectionModel;
import com.tale.uder.models.PlaceModel;
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
  @NonNull private final PlaceModel placeModel;
  private Address destAddress;
  private Address orgAddress;

  @Inject public MainPresenter(@NonNull AnalyticsModel analyticsModel,
      @NonNull DirectionModel directionModel, @NonNull PlaceModel placeModel) {
    this.analyticsModel = analyticsModel;
    this.directionModel = directionModel;
    this.placeModel = placeModel;
  }

  public void setFrom(Address address) {
    final MainView view = view();
    if (view != null) {
      view.showFromAddress(address.formattedAddress());
      final Location location = address.geometry().location;
      view.showFromLocation(new LatLng(location.lat, location.lng));
      analyticsModel.sendEvent(AnalyticsModel.EvenBuilder.pickFrom(address.formattedAddress()));
    }
    orgAddress = address;
    checkAndUpdateDirection();
  }

  public void setTo(Address address) {
    final MainView view = view();
    if (view != null) {
      view.showToAddress(address.formattedAddress());
      final Location location = address.geometry().location;
      view.showToLocation(new LatLng(location.lat, location.lng));
      analyticsModel.sendEvent(AnalyticsModel.EvenBuilder.pickTo(address.formattedAddress()));
    }
    destAddress = address;
    checkAndUpdateDirection();
  }

  private void checkAndUpdateDirection() {
    if (orgAddress != null && destAddress != null) {
      Single<List<LatLng>> direction =
          directionModel.getDirection(orgAddress.placeId(), destAddress.placeId());
      if (!test()) {
        direction = direction.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread());
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

  public void onChangeFromPosition(@NonNull LatLng position) {
    Single<Address> queryPlace = getQueryAddressSingle(position);
    unsubscribeOnUnbindView(queryPlace.subscribe(this::setFrom, throwable -> setFrom(orgAddress)));
  }

  public void onChangeToPosition(@NonNull LatLng position) {
    Single<Address> queryPlace = getQueryAddressSingle(position);
    unsubscribeOnUnbindView(queryPlace.subscribe(this::setTo, throwable -> setTo(destAddress)));
  }


  @NonNull private Single<Address> getQueryAddressSingle(@NonNull LatLng position) {
    Single<Address> queryPlace = placeModel.queryPlace(position.latitude, position.longitude);
    if (!test()) {
      queryPlace =
          queryPlace.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    return queryPlace;
  }
}
