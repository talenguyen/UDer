/**
 * uder
 *
 * Created by Giang Nguyen on 1/23/16.
 * Copyright (c) 2016 Umbala. All rights reserved.
 */

package com.tale.uder.ui.main;

import android.support.annotation.NonNull;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private MainPresenter mainPresenter;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private MainView mainView;
  
  @Before public void beforeEachTest() {
    mainPresenter =
        new MainPresenter();
    mainView = mock(MainView.class);
  }

  @Test public void onFromPicked_shouldDisplayAddressAndPlace() {
    mainPresenter.bindView(mainView);

    final Place place = mock(Place.class);
    final String address = "123 Ho Chi Minh";
    when(place.getAddress()).thenReturn(address);
    final LatLng latLng = new LatLng(1, 1);
    when(place.getLatLng()).thenReturn(latLng);
    mainPresenter.onFromPicked(place);

    verify(mainView).showFromAddress(eq(address));
    verify(mainView).showFromLocation(eq(latLng));
  }

  @Test public void onToPicked_shouldDisplayAddressAndPlace() {
    mainPresenter.bindView(mainView);

    final Place place = mock(Place.class);
    final String address = "123 Ho Chi Minh";
    when(place.getAddress()).thenReturn(address);
    final LatLng latLng = new LatLng(1, 1);
    when(place.getLatLng()).thenReturn(latLng);
    mainPresenter.onToPicked(place);

    verify(mainView).showToAddress(eq(address));
    verify(mainView).showToLocation(eq(latLng));
  }

  @Test public void unbind_shouldNeverInteractAfterUnbind() {
    mainPresenter.bindView(mainView);

    final Place place = mock(Place.class);
    final String address = "123 Ho Chi Minh";
    when(place.getAddress()).thenReturn(address);
    final LatLng latLng = new LatLng(1, 1);
    when(place.getLatLng()).thenReturn(latLng);
    // Unbind before onPick
    mainPresenter.unbindView(mainView);

    mainPresenter.onFromPicked(place);
    verifyZeroInteractions(mainView);

    mainPresenter.onToPicked(place);
    verifyZeroInteractions(mainView);

  }
}
