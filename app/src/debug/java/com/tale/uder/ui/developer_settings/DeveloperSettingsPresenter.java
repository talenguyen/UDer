package com.tale.uder.ui.developer_settings;

import android.support.annotation.NonNull;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.tale.uder.developer_settings.DeveloperSettingsModelImpl;
import com.tale.uder.models.AnalyticsModel;
import com.tale.uder.ui.presenters.Presenter;

public class DeveloperSettingsPresenter extends Presenter<DeveloperSettingsView> {

  @NonNull private final DeveloperSettingsModelImpl developerSettingsModel;

  @NonNull private final AnalyticsModel analyticsModel;

  public DeveloperSettingsPresenter(@NonNull DeveloperSettingsModelImpl developerSettingsModel,
      @NonNull AnalyticsModel analyticsModel) {
    this.developerSettingsModel = developerSettingsModel;
    this.analyticsModel = analyticsModel;
  }

  @NonNull private static String booleanToEnabledDisabled(boolean enabled) {
    return enabled ? "enabled" : "disabled";
  }

  @Override public void bindView(@NonNull DeveloperSettingsView view) {
    super.bindView(view);

    view.changeGitSha(developerSettingsModel.getGitSha());
    view.changeBuildDate(developerSettingsModel.getBuildDate());
    view.changeBuildVersionCode(developerSettingsModel.getBuildVersionCode());
    view.changeBuildVersionName(developerSettingsModel.getBuildVersionName());
    view.changeStethoState(developerSettingsModel.isStethoEnabled());
    view.changeLeakCanaryState(developerSettingsModel.isLeakCanaryEnabled());
    view.changeTinyDancerState(developerSettingsModel.isTinyDancerEnabled());
    view.changeHttpLoggingLevel(developerSettingsModel.getHttpLoggingLevel());
  }

  public void changeStethoState(boolean enabled) {
    if (developerSettingsModel.isStethoEnabled() == enabled) {
      return; // no-op
    }

    analyticsModel.sendEvent("developer_settings_stetho_" + booleanToEnabledDisabled(enabled));

    boolean stethoWasEnabled = developerSettingsModel.isStethoEnabled();

    developerSettingsModel.changeStethoState(enabled);

    final DeveloperSettingsView view = view();

    if (view != null) {
      view.showMessage("Stetho was " + booleanToEnabledDisabled(enabled));

      if (stethoWasEnabled) {
        view.showAppNeedsToBeRestarted();
      }
    }
  }

  public void changeLeakCanaryState(boolean enabled) {
    if (developerSettingsModel.isLeakCanaryEnabled() == enabled) {
      return; // no-op
    }

    analyticsModel.sendEvent("developer_settings_leak_canary_" + booleanToEnabledDisabled(enabled));

    developerSettingsModel.changeLeakCanaryState(enabled);

    final DeveloperSettingsView view = view();

    if (view != null) {
      view.showMessage("LeakCanary was " + booleanToEnabledDisabled(enabled));
      view.showAppNeedsToBeRestarted(); // LeakCanary can not be enabled on demand (or it's possible?)
    }
  }

  public void changeTinyDancerState(boolean enabled) {
    if (developerSettingsModel.isTinyDancerEnabled() == enabled) {
      return; // no-op
    }

    analyticsModel.sendEvent("developer_settings_tiny_dancer_" + booleanToEnabledDisabled(enabled));

    developerSettingsModel.changeTinyDancerState(enabled);

    final DeveloperSettingsView view = view();

    if (view != null) {
      view.showMessage("TinyDancer was " + booleanToEnabledDisabled(enabled));
    }
  }

  public void changeHttpLoggingLevel(@NonNull HttpLoggingInterceptor.Level loggingLevel) {
    if (developerSettingsModel.getHttpLoggingLevel() == loggingLevel) {
      return; // no-op
    }

    analyticsModel.sendEvent("developer_settings_http_logging_level_" + loggingLevel);

    developerSettingsModel.changeHttpLoggingLevel(loggingLevel);

    final DeveloperSettingsView view = view();

    if (view != null) {
      view.showMessage("Http logging level was changed to " + loggingLevel.toString());
    }
  }
}
