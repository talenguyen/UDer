package com.tale.uder.developer_settings;

import android.support.annotation.NonNull;
import com.tale.uder.ui.other.NoOpViewModifier;
import com.tale.uder.ui.other.ViewModifier;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

@Module public class DeveloperSettingsModule {

  @NonNull public static final String MAIN_ACTIVITY_VIEW_MODIFIER = "main_activity_view_modifier";

  @Provides @NonNull @Named(MAIN_ACTIVITY_VIEW_MODIFIER)
  public ViewModifier provideMainActivityViewModifier() {
    return new NoOpViewModifier();
  }

  @Provides @NonNull public DeveloperSettingModel provideDeveloperSettingModel() {
    return () -> {
      // no-op!
    };
  }

  @Provides @NonNull @Singleton public LeakCanaryProxy provideLeakCanaryProxy() {
    return new NoOpLeakCanaryProxy();
  }
}
