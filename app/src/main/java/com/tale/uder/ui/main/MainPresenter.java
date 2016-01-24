/**
 * uder
 *
 * Created by Giang Nguyen on 1/23/16.
 * Copyright (c) 2016 Umbala. All rights reserved.
 */

package com.tale.uder.ui.main;

import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.tale.uder.models.AnalyticsModel;
import com.tale.uder.ui.presenters.Presenter;
import javax.inject.Inject;

public class MainPresenter extends Presenter<MainView> {

  @NonNull private final AnalyticsModel analyticsModel;

  /**
   * Default constructor for inject.
   */
  @Inject public MainPresenter(@NonNull AnalyticsModel analyticsModel) {
    this.analyticsModel = analyticsModel;
  }

  public void setFrom(Place place) {
    final MainView view = view();
    if (view != null) {
      final CharSequence address = place.getAddress();
      view.showFromAddress(address);
      view.showFromLocation(place.getLatLng());
      analyticsModel.sendEvent(AnalyticsModel.EvenBuilder.pickFrom(address));
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
