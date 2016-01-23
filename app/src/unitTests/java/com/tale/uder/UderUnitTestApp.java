package com.tale.uder;

import android.support.annotation.NonNull;
import com.tale.uder.models.AnalyticsModel;
import com.tale.uder.models.ModelsModule;

import static org.mockito.Mockito.mock;

public class UderUnitTestApp extends UderApp {

  @NonNull @Override protected DaggerApplicationComponent.Builder prepareApplicationComponent() {
    return super.prepareApplicationComponent().modelsModule(new ModelsModule() {
      @NonNull @Override
      public AnalyticsModel provideAnalyticsModel(@NonNull UderApp app) {
        return mock(AnalyticsModel.class); // We don't need real analytics in Unit tests.
      }
    });
  }
}
