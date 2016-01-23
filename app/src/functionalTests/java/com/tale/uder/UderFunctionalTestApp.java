package com.tale.uder;

import android.support.annotation.NonNull;
import com.tale.uder.models.AnalyticsModel;
import com.tale.uder.models.ModelsModule;
import timber.log.Timber;

public class UderFunctionalTestApp extends UderApp {

  @NonNull @Override protected DaggerApplicationComponent.Builder prepareApplicationComponent() {
    return super.prepareApplicationComponent().modelsModule(new ModelsModule() {
      @NonNull @Override
      public AnalyticsModel provideAnalyticsModel(@NonNull UderApp app) {
        // We don't need real analytics in Functional tests, but let's just log it instead!
        return new AnalyticsModel() {

          // We'll check that application initializes Analytics before working with it!
          private volatile boolean isInitialized;

          @Override public void init() {
            isInitialized = true;
            Timber.d("Analytics: initialized.");
          }

          @Override public void sendEvent(@NonNull String eventName) {
            throwIfNotInitialized();
            Timber.d("Analytics: send event %s", eventName);
          }

          @Override public void sendError(@NonNull String message, @NonNull Throwable error) {
            throwIfNotInitialized();
            Timber.e(error, message);
          }

          private void throwIfNotInitialized() {
            if (!isInitialized) {
              throw new AssertionError("Analytics was not initialized!");
            }
          }
        };
      }
    });
  }
}
