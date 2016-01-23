package com.tale.uder;

import android.support.annotation.NonNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tale.uder.api.ApiModule;
import com.tale.uder.api.ChangeableBaseUrl;
import com.tale.uder.api.UderRestApi;
import com.tale.uder.developer_settings.DeveloperSettingsComponent;
import com.tale.uder.developer_settings.DeveloperSettingsModule;
import com.tale.uder.developer_settings.LeakCanaryProxy;
import com.tale.uder.models.ModelsModule;
import com.tale.uder.network.NetworkModule;
import com.tale.uder.ui.activities.MainActivity;
import com.tale.uder.ui.fragments.ItemsFragment;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = {
    ApplicationModule.class, NetworkModule.class, ApiModule.class, ModelsModule.class,
    DeveloperSettingsModule.class,
}) public interface ApplicationComponent {

  // Provide ObjectMapper from the real app to the tests without need in injection to the test.
  @NonNull ObjectMapper objectMapper();

  // Provide UderRestApi from the real app to the tests without need in injection to the test.
  @NonNull UderRestApi qualityMattersApi();

  @NonNull ChangeableBaseUrl changeableBaseUrl();

  // Provide LeakCanary without injection to leave
  @NonNull LeakCanaryProxy leakCanaryProxy();

  @NonNull ItemsFragment.ItemsFragmentComponent plus(
      @NonNull ItemsFragment.ItemsFragmentModule itemsFragmentModule);

  @NonNull DeveloperSettingsComponent plusDeveloperSettingsComponent();

  void inject(@NonNull UderApp uderApp);

  void inject(@NonNull MainActivity mainActivity);
}
