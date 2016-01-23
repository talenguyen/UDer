package com.tale.uder.models;

import android.support.annotation.NonNull;
import com.tale.uder.UderApp;
import com.tale.uder.api.UderRestApi;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import timber.log.Timber;

@Module public class ModelsModule {

  @Provides @NonNull @Singleton
  public AnalyticsModel provideAnalyticsModel(@NonNull UderApp app) {
    return new NoOpAnalyticsModel();
  }

  @Provides @NonNull
  public ItemsModel provideItemsModel(@NonNull UderRestApi uderRestApi) {
    return new ItemsModel(uderRestApi);
  }

  static class NoOpAnalyticsModel implements AnalyticsModel {

    @Override public void init() {
      // no-op!
    }

    @Override public void sendEvent(@NonNull String eventName) {
      Timber.d("sendEvent =====> eventName = [%s]", eventName);
    }

    @Override public void sendError(@NonNull String message, @NonNull Throwable error) {
      Timber.e(error, "message: %s", message);
    }
  }
}
