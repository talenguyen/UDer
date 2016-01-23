package com.tale.uder.api;

import android.support.annotation.NonNull;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.tale.uder.BuildConfig;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module public class ApiModule {

  @NonNull private final ChangeableBaseUrl changeableBaseUrl;

  public ApiModule(@NonNull String baseUrl) {
    changeableBaseUrl = new ChangeableBaseUrl(baseUrl);
  }

  @Provides @NonNull @Singleton public ChangeableBaseUrl provideChangeableBaseUrl() {
    return changeableBaseUrl;
  }

  @Provides @NonNull @Singleton
  public UderRestApi provideUderApi(@NonNull OkHttpClient okHttpClient,
      @NonNull ObjectMapper objectMapper, @NonNull ChangeableBaseUrl changeableBaseUrl) {
    final Retrofit.Builder builder = new Retrofit.Builder().baseUrl(changeableBaseUrl)
        .client(okHttpClient)
        .addConverterFactory(JacksonConverterFactory.create(
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

    // Fail early: check Retrofit configuration at creation time
    if (BuildConfig.DEBUG) {
      builder.validateEagerly();
    }

    return builder.build().create(UderRestApi.class);
  }
}
