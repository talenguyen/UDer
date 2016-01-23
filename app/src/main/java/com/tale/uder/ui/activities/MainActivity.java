package com.tale.uder.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.tale.uder.UderApp;
import com.tale.uder.R;
import com.tale.uder.ui.fragments.ItemsFragment;
import com.tale.uder.ui.other.ViewModifier;
import javax.inject.Inject;
import javax.inject.Named;

import static com.tale.uder.developer_settings.DeveloperSettingsModule.MAIN_ACTIVITY_VIEW_MODIFIER;

public class MainActivity extends BaseActivity {

  @Inject @Named(MAIN_ACTIVITY_VIEW_MODIFIER) ViewModifier viewModifier;

  @SuppressLint("InflateParams") // It's okay in our case.
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    UderApp.get(this).applicationComponent().inject(this);

    setContentView(viewModifier.modify(getLayoutInflater().inflate(R.layout.activity_main, null)));

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.main_frame_layout, new ItemsFragment())
          .commit();
    }
  }
}
