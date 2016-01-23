package com.tale.uder;

import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;

public class TestUtils {

  private TestUtils() {
    throw new IllegalStateException("No instances, please");
  }

  @NonNull public static UderApp app() {
    return (UderApp) InstrumentationRegistry.getTargetContext().getApplicationContext();
  }
}
