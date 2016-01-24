package com.tale.uder.models;

import android.support.annotation.NonNull;

/**
 * Common interface for Analytics systems like: Yandex App Metrics, Google Analytics, Flurry, etc.
 */
public interface AnalyticsModel {

  class EvenBuilder {
    /**
     * Private constructor for Utility class
     */
    private EvenBuilder() {
    }

    public static String pickFrom(CharSequence address) {
      return String.format("from:%s", address);
    }

    public static String pickTo(CharSequence address) {
      return String.format("to:%s", address);
    }
  }

  void init();

  void sendEvent(@NonNull String eventName);

  void sendError(@NonNull String message, @NonNull Throwable error);
}
