package com.tale.uder.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.tale.uder.R;
import com.tale.uder.UderApp;
import com.tale.uder.ui.other.ViewModifier;
import javax.inject.Inject;
import javax.inject.Named;

import static com.tale.uder.developer_settings.DeveloperSettingsModule.MAIN_ACTIVITY_VIEW_MODIFIER;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

  private static final int SEARCH_FROM_REQUEST_CODE = 1;
  private static final int SEARCH_TO_REQUEST_CODE = 2;

  @Inject @Named(MAIN_ACTIVITY_VIEW_MODIFIER) ViewModifier viewModifier;

  @Bind(R.id.rootView) View rootView;

  @SuppressLint("InflateParams") // It's okay in our case.
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    UderApp.get(this).applicationComponent().inject(this);

    setContentView(viewModifier.modify(getLayoutInflater().inflate(R.layout.activity_main, null)));

    ButterKnife.bind(this);

    if (savedInstanceState == null) {
      final SupportMapFragment supportMapFragment = new SupportMapFragment();
      supportMapFragment.getMapAsync(this);
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.main_frame_layout, supportMapFragment)
          .commit();
    }
  }

  @OnClick(R.id.btPickFrom) public void sendSearchPlaceRequestForFrom(View view) {
    sendSearchPlaceRequest(SEARCH_FROM_REQUEST_CODE);
  }

  @OnClick(R.id.btPickTo) public void sendSearchPlaceRequestForTo(View view) {
    sendSearchPlaceRequest(SEARCH_TO_REQUEST_CODE);
  }

  @Override public void onMapReady(GoogleMap googleMap) {
  }

  private void sendSearchPlaceRequest(int requestCode) {
    try {
      Intent intent =
          new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
      startActivityForResult(intent, requestCode);
    } catch (GooglePlayServicesRepairableException e) {
      showGooglePlayServicesRepairableError();
    } catch (GooglePlayServicesNotAvailableException e) {
      showGooglePlayServicesNotAvailableError();
    }
  }

  private void showGooglePlayServicesRepairableError() {
    Snackbar.make(rootView, R.string.can_not_connect_to_google_play_services, Snackbar.LENGTH_SHORT)
        .show();
  }

  private void showGooglePlayServicesNotAvailableError() {
    Snackbar.make(rootView, R.string.can_not_connect_to_google_play_services, Snackbar.LENGTH_SHORT)
        .show();
  }
}
