package com.tale.uder;

import android.support.annotation.NonNull;

import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;

// Custom runner allows us set config in one place instead of setting it in each test class.
public class UderRobolectricUnitTestRunner extends RobolectricGradleTestRunner {

  // This value should be changed as soon as Robolectric will support newer api.
  private static final int SDK_EMULATE_LEVEL = 21;

  public UderRobolectricUnitTestRunner(@NonNull Class<?> klass) throws Exception {
    super(klass);
  }

  @Override
  public Config getConfig(@NonNull Method method) {
    final Config defaultConfig = super.getConfig(method);
    return new Config.Implementation(
        new int[]{SDK_EMULATE_LEVEL},
        defaultConfig.manifest(),
        defaultConfig.qualifiers(),
        defaultConfig.packageName(),
        defaultConfig.resourceDir(),
        defaultConfig.assetDir(),
        defaultConfig.shadows(),
        UderUnitTestApp.class, // Notice that we override real application class for Unit tests.
        defaultConfig.libraries(),
        defaultConfig.constants() == Void.class ? BuildConfig.class : defaultConfig.constants()
    );
  }

  @NonNull
  public static UderApp udersApp() {
    return (UderApp) RuntimeEnvironment.application;
  }
}
