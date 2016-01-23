package com.tale.uder;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnitRunner;

public class UderFunctionalTestsRunner extends AndroidJUnitRunner {

  @Override @NonNull
  public Application newApplication(@NonNull ClassLoader cl, @NonNull String className,
      @NonNull Context context)
      throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    return Instrumentation.newApplication(UderFunctionalTestApp.class, context);
  }
}
