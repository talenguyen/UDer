/**
 * uder
 *
 * Created by Giang Nguyen on 1/23/16.
 * Copyright (c) 2016 Umbala. All rights reserved.
 */

package com.tale.uder.ui.main;

import com.google.android.gms.maps.model.LatLng;

public interface MainView {
  void showFromAddress(CharSequence address);

  void showFromLocation(LatLng latLng);

  void showToAddress(CharSequence address);

  void showToLocation(LatLng latLng);
}
