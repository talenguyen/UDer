package com.tale.uder.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

  @Inject @Named(MAIN_ACTIVITY_VIEW_MODIFIER) ViewModifier viewModifier;

  @SuppressLint("InflateParams") // It's okay in our case.
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    UderApp.get(this).applicationComponent().inject(this);

    setContentView(viewModifier.modify(getLayoutInflater().inflate(R.layout.activity_main, null)));

    if (savedInstanceState == null) {
      final SupportMapFragment supportMapFragment = new SupportMapFragment();
      supportMapFragment.getMapAsync(this);
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.main_frame_layout, supportMapFragment)
          .commit();
    }
  }

  @Override public void onMapReady(GoogleMap googleMap) {

  }


}
