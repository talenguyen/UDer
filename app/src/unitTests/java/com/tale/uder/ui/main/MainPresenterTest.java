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
import com.tale.uder.models.AnalyticsModel;
import com.tale.uder.models.DirectionModel;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import rx.Single;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private MainPresenter mainPresenter;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private DirectionModel directionModel;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private MainView mainView;
  
  @Before public void beforeEachTest() {
    final AnalyticsModel analyticsModel = mock(AnalyticsModel.class);
    directionModel = mock(DirectionModel.class);
    mainPresenter = new MainPresenter(analyticsModel, directionModel) {
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
    mainPresenter.setFrom(mockPlace(address, latLng));

    verify(mainView).showFromAddress(eq(address));
    verify(mainView).showFromLocation(eq(latLng));
  }

  @Test public void onToPicked_shouldDisplayAddressAndPlace() {
    mainPresenter.bindView(mainView);

    final String address = "123 Ho Chi Minh";
    final LatLng latLng = new LatLng(1, 1);
    mainPresenter.setTo(mockPlace(address, latLng));

    verify(mainView).showToAddress(eq(address));
    verify(mainView).showToLocation(eq(latLng));
  }

  @Test public void direction_shouldNotShowDirectionByDefault() {
    mainPresenter.bindView(mainView);
    verify(mainView, never()).showDirection(any());
  }

  @Test public void direction_shouldNotShowDirectionIfOnlyPickFrom() {
    mainPresenter.bindView(mainView);

    final Place place = mockPlace("123 Ho Chi Minh", new LatLng(1, 1));
    mainPresenter.setFrom(place);

    verify(mainView, never()).showDirection(any());
  }

  @Test public void direction_shouldNotShowDirectionIfOnlyPickTo() {
    mainPresenter.bindView(mainView);

    final Place place = mockPlace("123 Ho Chi Minh", new LatLng(1, 1));
    mainPresenter.setTo(place);

    verify(mainView, never()).showDirection(any());
  }

  @Test public void direction_shouldShowDirectionIfPickFromThenTo() {
    mainPresenter.bindView(mainView);
    final List<LatLng> directions =
        Arrays.asList(new LatLng(1, 2), new LatLng(1, 2), new LatLng(1, 2));
    final Place place = mockPlace("123 Ho Chi Minh", new LatLng(1, 1));
    when(directionModel.getDirection(any(), any())).thenReturn(Single.just(directions));
    mainPresenter.setFrom(place);
    mainPresenter.setTo(place);
    verify(mainView).showDirection(eq(directions));
  }

  @Test public void unbind_shouldNeverInteractAfterUnbind() {
    when(directionModel.getDirection(any(), any())).thenReturn(Single.just(null));

    mainPresenter.bindView(mainView);

    // Unbind before onPick
    mainPresenter.unbindView(mainView);

    final Place place = mockPlace("123 Ho Chi Minh", new LatLng(1, 1));
    mainPresenter.setFrom(place);
    verifyZeroInteractions(mainView);

    mainPresenter.setTo(place);
    verifyZeroInteractions(mainView);
  }

  @NonNull private Place mockPlace(@NonNull String address, @NonNull LatLng latLng) {
    final Place place = mock(Place.class);
    when(place.getAddress()).thenReturn(address);
    when(place.getLatLng()).thenReturn(latLng);
    return place;
  }
}
