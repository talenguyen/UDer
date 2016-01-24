package com.tale.uder.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tale.uder.R;
import com.tale.uder.UderApp;
import com.tale.uder.ui.other.ViewModifier;
import javax.inject.Inject;
import javax.inject.Named;

import static com.tale.uder.developer_settings.DeveloperSettingsModule.MAIN_ACTIVITY_VIEW_MODIFIER;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, MainView {

  private static final int SEARCH_FROM_REQUEST_CODE = 1;
  private static final int SEARCH_TO_REQUEST_CODE = 2;

  @Inject @Named(MAIN_ACTIVITY_VIEW_MODIFIER) ViewModifier viewModifier;
  @Inject MainPresenter presenter;

  @NonNull @Bind(R.id.rootView) View rootView;

  @NonNull @Bind(R.id.tvFrom) TextView tvFrom;

  @NonNull @Bind(R.id.tvTo) TextView tvTo;

  private GoogleMap googleMap;
  private Marker markerFrom;
  private Marker markerTo;
  private int mapPadding;

  @SuppressLint("InflateParams") // It's okay in our case.
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    UderApp.get(this).applicationComponent().inject(this);

    setContentView(viewModifier.modify(getLayoutInflater().inflate(R.layout.activity_main, null)));

    ButterKnife.bind(this);

    final SupportMapFragment supportMapFragment =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    supportMapFragment.getMapAsync(this);

    presenter.bindView(this);

    mapPadding = getResources().getDisplayMetrics().widthPixels / 3;

  }

  @Override protected void onDestroy() {
    presenter.unbindView(this);
    super.onDestroy();
  }

  @OnClick(R.id.btPickFrom) public void sendSearchPlaceRequestForFrom(View view) {
    sendSearchPlaceRequest(SEARCH_FROM_REQUEST_CODE);
  }

  @OnClick(R.id.btPickTo) public void sendSearchPlaceRequestForTo(View view) {
    sendSearchPlaceRequest(SEARCH_TO_REQUEST_CODE);
  }

  @Override public void onMapReady(GoogleMap googleMap) {
    this.googleMap = googleMap;
    setupMaps(googleMap);
  }

  private void setupMaps(GoogleMap googleMap) {
    final UiSettings uiSettings = googleMap.getUiSettings();
    uiSettings.setZoomGesturesEnabled(true);
    uiSettings.setScrollGesturesEnabled(true);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == SEARCH_FROM_REQUEST_CODE || requestCode == SEARCH_TO_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        final Place place = PlaceAutocomplete.getPlace(this, data);
        if (requestCode == SEARCH_FROM_REQUEST_CODE) {
          presenter.setFrom(place);
        } else {
          presenter.setTo(place);
        }
      } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
        Status status = PlaceAutocomplete.getStatus(this, data);
        presenter.onSearchPlaceError(status);
      }
    }
  }

  private void sendSearchPlaceRequest(int requestCode) {
    try {
      Intent intent =
          new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
      startActivityForResult(intent, requestCode);
    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
      showError(e);
    }
  }

  private void showError(Exception e) {
    final String message = getString(R.string.can_not_connect_to_google_play_services);
    Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    presenter.onError(message, e);
  }

  @Override public void showFromAddress(CharSequence address) {
    if (tvFrom == null) {
      return;
    }
    tvFrom.setText(address);
  }

  @Override public void showToAddress(CharSequence address) {
    if (tvTo == null) {
      return;
    }
    tvTo.setText(address);
  }

  @Override public void showFromLocation(@NonNull LatLng latLng) {
    if (googleMap == null) {
      return;
    }
    if (markerFrom == null) {
      markerFrom = addMarker(latLng, getString(R.string.from));
    } else {
      // If marker already exist then we just change its position.
      markerFrom.setPosition(latLng);
    }
    updateBounds();
  }

  @Override public void showToLocation(@NonNull LatLng latLng) {
    if (googleMap == null) {
      return;
    }
    if (markerTo == null) {
      markerTo = addMarker(latLng, getString(R.string.to));
    } else {
      // If marker already exist then we just change its position.
      markerTo.setPosition(latLng);
    }
    updateBounds();
  }

  private Marker addMarker(@NonNull LatLng latLng, @NonNull String title) {
    return googleMap.addMarker(new MarkerOptions().position(latLng).title(title).draggable(true));
  }

  private void updateBounds() {
    final LatLngBounds.Builder builder = new LatLngBounds.Builder();
    if (markerFrom != null) {
      builder.include(markerFrom.getPosition());
    }
    if (markerTo != null) {
      builder.include(markerTo.getPosition());
    }

    final LatLngBounds bounds = builder.build();
    final CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, mapPadding);
    googleMap.animateCamera(update);
  }
}
