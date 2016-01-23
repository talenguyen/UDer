package com.tale.uder;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.tale.uder.api.ApiModule;
import com.tale.uder.developer_settings.DeveloperSettingModel;
import com.tale.uder.models.AnalyticsModel;
import dagger.Lazy;
import javax.inject.Inject;
import timber.log.Timber;

public class UderApp extends Application {

  @SuppressWarnings("NullableProblems") // Initialize in onCreate.
  @Inject @NonNull AnalyticsModel analyticsModel;
  @SuppressWarnings("NullableProblems") // Initialize in onCreate.
  @Inject @NonNull Lazy<DeveloperSettingModel> developerSettingModel;
  @SuppressWarnings("NullableProblems")
  // Initialized in onCreate. But be careful if you have ContentProviders
  // -> their onCreate may be called before app.onCreate()
  // -> move initialization to attachBaseContext().
  @NonNull private ApplicationComponent applicationComponent;

  // Prevent need in a singleton (global) reference to the application object.
  @NonNull public static UderApp get(@NonNull Context context) {
    return (UderApp) context.getApplicationContext();
  }

  @Override public void onCreate() {
    super.onCreate();
    applicationComponent = prepareApplicationComponent().build();
    applicationComponent.inject(this);

    analyticsModel.init();

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
      developerSettingModel.get().apply();
    }
  }

  @NonNull protected DaggerApplicationComponent.Builder prepareApplicationComponent() {
    return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this))
        // This url may be changed dynamically for tests! See ChangeableBaseUrl.
        .apiModule(new ApiModule(
            "https://raw.githubusercontent.com/artem-zinnatullin/qualitymatters/master/rest_api/"));
  }

  @NonNull public ApplicationComponent applicationComponent() {
    return applicationComponent;
  }
}
