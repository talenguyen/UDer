package com.tale.uder.developer_settings;

import android.support.annotation.NonNull;
import com.tale.uder.ui.developer_settings.DeveloperSettingsFragment;
import dagger.Subcomponent;

@Subcomponent public interface DeveloperSettingsComponent {
  void inject(@NonNull DeveloperSettingsFragment developerSettingsFragment);
}
