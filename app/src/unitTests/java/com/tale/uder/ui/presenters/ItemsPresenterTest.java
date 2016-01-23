package com.tale.uder.ui.presenters;

import android.support.annotation.NonNull;
import com.tale.uder.api.entities.Item;
import com.tale.uder.models.AnalyticsModel;
import com.tale.uder.models.ItemsModel;
import com.tale.uder.ui.views.ItemsView;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import rx.Single;
import rx.schedulers.Schedulers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ItemsPresenterTest {

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private ItemsModel itemsModel;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private ItemsPresenterConfiguration presenterConfiguration;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private ItemsPresenter itemsPresenter;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private ItemsView itemsView;

  @Before public void beforeEachTest() {
    itemsModel = mock(ItemsModel.class);
    presenterConfiguration = ItemsPresenterConfiguration.builder().ioScheduler(
        Schedulers.immediate()) // We don't need async behavior in tests.
        .build();

    itemsPresenter =
        new ItemsPresenter(presenterConfiguration, itemsModel, mock(AnalyticsModel.class));
    itemsView = mock(ItemsView.class);
  }

  @Test public void reloadData_shouldMoveViewToLoadingState() {
    itemsPresenter.bindView(itemsView);

    when(itemsModel.getItems()).thenReturn(Single.just(emptyList()));

    itemsPresenter.reloadData();
    verify(itemsView).showLoadingUi();

  }

  @Test public void reloadData_shouldSendDataToTheView() {
    itemsPresenter.bindView(itemsView);

    List<Item> items = asList(
        Item.builder().id("1").title("t1").shortDescription("s1").build(),
        Item.builder().id("2").title("t2").shortDescription("s2").build());

    when(itemsModel.getItems()).thenReturn(Single.just(items));

    itemsPresenter.reloadData();
    verify(itemsView).showContentUi(items);
    verify(itemsView, never()).showErrorUi(any(Throwable.class));
  }

  @Test public void reloadData_shouldSendErrorToTheView() {
    itemsPresenter.bindView(itemsView);

    Throwable error = new RuntimeException();
    when(itemsModel.getItems()).thenReturn(Single.error(error));

    itemsPresenter.reloadData();
    verify(itemsView).showErrorUi(error);
    verify(itemsView, never()).showContentUi(anyListOf(Item.class));
  }
}