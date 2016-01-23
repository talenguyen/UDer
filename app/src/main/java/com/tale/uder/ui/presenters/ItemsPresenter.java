package com.tale.uder.ui.presenters;

import android.support.annotation.NonNull;
import com.tale.uder.models.AnalyticsModel;
import com.tale.uder.models.ItemsModel;
import com.tale.uder.ui.views.ItemsView;
import javax.inject.Inject;
import rx.Subscription;

public class ItemsPresenter extends Presenter<ItemsView> {

  @NonNull private final ItemsPresenterConfiguration presenterConfiguration;

  @NonNull private final ItemsModel itemsModel;

  @NonNull private final AnalyticsModel analyticsModel;

  @Inject public ItemsPresenter(@NonNull ItemsPresenterConfiguration presenterConfiguration,
      @NonNull ItemsModel itemsModel, @NonNull AnalyticsModel analyticsModel) {
    this.presenterConfiguration = presenterConfiguration;
    this.itemsModel = itemsModel;
    this.analyticsModel = analyticsModel;
  }

  public void reloadData() {
    {
      // Tip: in Kotlin you can use ? to operate with nullable values.
      final ItemsView view = view();

      if (view != null) {
        view.showLoadingUi();
      }
    }

    final Subscription subscription =
        itemsModel.getItems().subscribeOn(presenterConfiguration.ioScheduler()).subscribe(items -> {
              // Tip: in Kotlin you can use ? to operate with nullable values.
              final ItemsView view = view();

              if (view != null) {
                view.showContentUi(items);
              }

            }, error -> {
              analyticsModel.sendError("ItemsPresenter.reloadData failed", error);

              // Tip: in Kotlin you can use ? to operate with nullable values.
              final ItemsView view = view();

              if (view != null) {
                view.showErrorUi(error);
              }

            });

    // Prevent memory leak.
    unsubscribeOnUnbindView(subscription);
  }
}
