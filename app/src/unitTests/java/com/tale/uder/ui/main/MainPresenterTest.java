/**
 * uder
 *
 * Created by Giang Nguyen on 1/23/16.
 * Copyright (c) 2016 Umbala. All rights reserved.
 */

package com.tale.uder.ui.main;

import android.support.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.tale.uder.api.entities.Address;
import com.tale.uder.api.entities.Geometry;
import com.tale.uder.api.entities.Location;
import com.tale.uder.models.AnalyticsModel;
import com.tale.uder.models.DirectionModel;
import com.tale.uder.models.PlaceModel;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import rx.Single;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private MainPresenter mainPresenter;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private DirectionModel directionModel;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private PlaceModel placeModel;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private MainView mainView;
  
  @Before public void beforeEachTest() {
    final AnalyticsModel analyticsModel = mock(AnalyticsModel.class);
    directionModel = mock(DirectionModel.class);
    placeModel = mock(PlaceModel.class);
    mainPresenter = new MainPresenter(analyticsModel, directionModel, placeModel) {
      @Override boolean test() {
        return true;
      }
    };
    mainView = mock(MainView.class);
  }

  @Test public void onFromPicked_shouldDisplayAddressAndPlace() {
    mainPresenter.bindView(mainView);

    final String address = "123 Ho Chi Minh";
    final LatLng latLng = new LatLng(1, 1);
    mainPresenter.setFrom(mockAddress(address, latLng));

    verify(mainView).showFromAddress(eq(address));
    verify(mainView).showFromLocation(eq(latLng));
  }

  @Test public void onToPicked_shouldDisplayAddressAndPlace() {
    mainPresenter.bindView(mainView);

    final String address = "123 Ho Chi Minh";
    final LatLng latLng = new LatLng(1, 1);
    mainPresenter.setTo(mockAddress(address, latLng));

    verify(mainView).showToAddress(eq(address));
    verify(mainView).showToLocation(eq(latLng));
  }

  @Test public void direction_shouldNotShowDirectionByDefault() {
    mainPresenter.bindView(mainView);
    verify(mainView, never()).showDirection(any());
  }

  @Test public void direction_shouldNotShowDirectionIfOnlyPickFrom() {
    mainPresenter.bindView(mainView);

    final Address address = mockAddress("123 Ho Chi Minh", new LatLng(1, 1));
    mainPresenter.setFrom(address);

    verify(mainView, never()).showDirection(any());
  }

  @Test public void direction_shouldNotShowDirectionIfOnlyPickTo() {
    mainPresenter.bindView(mainView);

    final Address address = mockAddress("123 Ho Chi Minh", new LatLng(1, 1));
    mainPresenter.setTo(address);

    verify(mainView, never()).showDirection(any());
  }

  @Test public void direction_shouldShowDirectionIfPickFromThenTo() {
    mainPresenter.bindView(mainView);
    final List<LatLng> directions =
        Arrays.asList(new LatLng(1, 2), new LatLng(1, 2), new LatLng(1, 2));
    final Address address = mockAddress("123 Ho Chi Minh", new LatLng(1, 1));
    when(directionModel.getDirection(any(), any())).thenReturn(Single.just(directions));
    mainPresenter.setFrom(address);
    mainPresenter.setTo(address);
    verify(mainView).showDirection(eq(directions));
  }

  @Test public void dragMarker_shouldChangeFromAddressAndPositionAfterDragFromMarker() {
    mainPresenter.bindView(mainView);
    final String formattedAddress = "123 Ho Chi Minh";
    final LatLng latLng = new LatLng(1, 1);
    Address address = mockAddress(formattedAddress, latLng);
    when(address.formattedAddress()).thenReturn(formattedAddress);
    when(placeModel.queryPlace(eq(latLng.latitude), eq(latLng.longitude))).thenReturn(
        Single.just(address));
    // Set address to display the marker.
    mainPresenter.setFrom(address);
    // Mocking change position
    mainPresenter.onChangeFromPosition(latLng);
    verify(mainView, times(2)).showFromAddress(formattedAddress);
    verify(mainView, times(2)).showFromLocation(latLng);
  }

  @Test public void dragMarker_shouldUpdateDirectionAfterPickFromAndToThenDragFromMarker() {
  }

  @Test public void dragMarker_shouldChangeToAddressAfterDragToMarker() {
  }

  @Test public void dragMarker_shouldUpdateDirectionAfterPickFromAndToThenDragToMarker() {
  }

  @Test public void unbind_shouldNeverInteractAfterUnbind() {
    when(directionModel.getDirection(any(), any())).thenReturn(Single.just(null));

    mainPresenter.bindView(mainView);

    // Unbind before onPick
    mainPresenter.unbindView(mainView);

    final Address address = mockAddress("123 Ho Chi Minh", new LatLng(1, 1));
    mainPresenter.setFrom(address);
    verifyZeroInteractions(mainView);

    mainPresenter.setTo(address);
    verifyZeroInteractions(mainView);
  }

  @NonNull private Address mockAddress(@NonNull String address, @NonNull LatLng latLng) {
    final Address mock = mock(Address.class);
    final Geometry geometry = new Geometry();
    geometry.location = new Location();
    geometry.location.lat = latLng.latitude;
    geometry.location.lng = latLng.longitude;
    when(mock.formattedAddress()).thenReturn(address);
    when(mock.geometry()).thenReturn(geometry);
    return mock;
  }
}
