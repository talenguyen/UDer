package com.tale.uder.developer_settings;

import android.support.annotation.NonNull;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.tale.uder.UderApp;
import com.tale.uder.models.AnalyticsModel;
import com.tale.uder.ui.developer_settings.DeveloperSettingsPresenter;
import com.tale.uder.ui.other.ViewModifier;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

import static android.content.Context.MODE_PRIVATE;

@Module public class DeveloperSettingsModule {

  @NonNull public static final String MAIN_ACTIVITY_VIEW_MODIFIER = "main_activity_view_modifier";

  @Provides @NonNull @Named(MAIN_ACTIVITY_VIEW_MODIFIER)
  public ViewModifier provideMainActivityViewModifier() {
    return new MainActivityViewModifier();
  }

  @Provides @NonNull public DeveloperSettingModel provideDeveloperSettingsModel(
      @NonNull DeveloperSettingsModelImpl developerSettingsModelImpl) {
    return developerSettingsModelImpl;
  }

  @Provides @NonNull @Singleton
  public DeveloperSettings provideDeveloperSettings(@NonNull UderApp uderApp) {
    return new DeveloperSettings(
        uderApp.getSharedPreferences("developer_settings", MODE_PRIVATE));
  }

  @Provides @NonNull @Singleton
  public LeakCanaryProxy provideLeakCanaryProxy(@NonNull UderApp uderApp) {
    return new LeakCanaryProxyImpl(uderApp);
  }

  // We will use this concrete type for debug code, but main code will see only DeveloperSettingsModel interface.
  @Provides @NonNull @Singleton public DeveloperSettingsModelImpl provideDeveloperSettingsModelImpl(
      @NonNull UderApp uderApp, @NonNull DeveloperSettings developerSettings,
      @NonNull OkHttpClient okHttpClient, @NonNull HttpLoggingInterceptor httpLoggingInterceptor,
      @NonNull LeakCanaryProxy leakCanaryProxy) {
    return new DeveloperSettingsModelImpl(uderApp, developerSettings, okHttpClient,
        httpLoggingInterceptor, leakCanaryProxy);
  }

  @Provides @NonNull public DeveloperSettingsPresenter provideDeveloperSettingsPresenter(
      @NonNull DeveloperSettingsModelImpl developerSettingsModelImpl,
      @NonNull AnalyticsModel analyticsModel) {
    return new DeveloperSettingsPresenter(developerSettingsModelImpl, analyticsModel);
  }
}
