package com.tale.uder;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

@Module public class ApplicationModule {

  public static final String MAIN_THREAD_HANDLER = "main_thread_handler";

  @NonNull private final UderApp uderApp;

  public ApplicationModule(@NonNull UderApp uderApp) {
    this.uderApp = uderApp;
  }

  @Provides @NonNull @Singleton public UderApp provideUderApp() {
    return uderApp;
  }

  @Provides @NonNull @Singleton public ObjectMapper provideObjectMapper() {
    return new ObjectMapper();
  }

  @Provides @NonNull @Named(MAIN_THREAD_HANDLER) @Singleton
  public Handler provideMainThreadHandler() {
    return new Handler(Looper.getMainLooper());
  }

}
