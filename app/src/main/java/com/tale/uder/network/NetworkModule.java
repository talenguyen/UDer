package com.tale.uder.network;

import android.support.annotation.NonNull;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.tale.uder.BuildConfig;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import timber.log.Timber;

import static com.squareup.okhttp.logging.HttpLoggingInterceptor.Level.BODY;
import static com.squareup.okhttp.logging.HttpLoggingInterceptor.Level.NONE;

@Module public class NetworkModule {

  @Provides @NonNull @Singleton
  public OkHttpClient provideOkHttpClient(@NonNull HttpLoggingInterceptor httpLoggingInterceptor) {
    OkHttpClient okHttpClient = new OkHttpClient();
    okHttpClient.interceptors().add(httpLoggingInterceptor);
    return okHttpClient;
  }

  @Provides @NonNull @Singleton public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
    HttpLoggingInterceptor httpLoggingInterceptor =
        new HttpLoggingInterceptor(message -> Timber.d(message));
    httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? BODY : NONE);
    return httpLoggingInterceptor;
  }
}
