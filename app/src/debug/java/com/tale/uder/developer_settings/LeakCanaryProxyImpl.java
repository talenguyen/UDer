package com.tale.uder.developer_settings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tale.uder.UderApp;

public class LeakCanaryProxyImpl implements LeakCanaryProxy {

  @NonNull private final UderApp uderApp;

  @Nullable private RefWatcher refWatcher;

  public LeakCanaryProxyImpl(@NonNull UderApp uderApp) {
    this.uderApp = uderApp;
  }

  @Override public void init() {
    refWatcher = LeakCanary.install(uderApp);
  }

  @Override public void watch(@NonNull Object object) {
    if (refWatcher != null) {
      refWatcher.watch(object);
    }
  }
}
