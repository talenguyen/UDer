package com.tale.uder.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.tale.uder.ApplicationModule;
import com.tale.uder.UderApp;
import com.tale.uder.R;
import com.tale.uder.api.entities.Item;
import com.tale.uder.models.AnalyticsModel;
import com.tale.uder.models.ItemsModel;
import com.tale.uder.performance.AnyThread;
import com.tale.uder.ui.adapters.ItemsAdapter;
import com.tale.uder.ui.adapters.VerticalSpaceItemDecoration;
import com.tale.uder.ui.presenters.ItemsPresenter;
import com.tale.uder.ui.presenters.ItemsPresenterConfiguration;
import com.tale.uder.ui.views.ItemsView;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import rx.schedulers.Schedulers;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ItemsFragment extends BaseFragment implements ItemsView {
  @Bind(R.id.items_loading_ui) View loadingUiView;

  @Bind(R.id.items_loading_error_ui) View errorUiView;

  @Bind(R.id.items_content_ui) RecyclerView contentUiRecyclerView;

  ItemsAdapter itemsAdapter;

  @Inject @Named(ApplicationModule.MAIN_THREAD_HANDLER) Handler mainThreadHandler;

  @Inject ItemsPresenter itemsPresenter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    UderApp.get(getContext())
        .applicationComponent()
        .plus(new ItemsFragmentModule())
        .inject(this);
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_items, container, false);
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    ButterKnife.bind(this, view);
    contentUiRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), VERTICAL, false));
    contentUiRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(
        (int) getResources().getDimension(R.dimen.list_item_vertical_space_between_items)));
    itemsAdapter = new ItemsAdapter(getActivity().getLayoutInflater());
    contentUiRecyclerView.setAdapter(itemsAdapter);
    itemsPresenter.bindView(this);
    itemsPresenter.reloadData();
  }

  @SuppressWarnings("ResourceType")
  // Lint does not understand that we shift execution on Main Thread.
  @Override @AnyThread public void showLoadingUi() {
    runOnUiThreadIfFragmentAlive(() -> {
      loadingUiView.setVisibility(VISIBLE);
      errorUiView.setVisibility(GONE);
      contentUiRecyclerView.setVisibility(GONE);
    });
  }

  @SuppressWarnings("ResourceType")
  // Lint does not understand that we shift execution on Main Thread.
  @Override @AnyThread public void showErrorUi(@NonNull Throwable error) {
    runOnUiThreadIfFragmentAlive(() -> {
      loadingUiView.setVisibility(GONE);
      errorUiView.setVisibility(VISIBLE);
      contentUiRecyclerView.setVisibility(GONE);
    });
  }

  @SuppressWarnings("ResourceType")
  // Lint does not understand that we shift execution on Main Thread.
  @Override @AnyThread public void showContentUi(@NonNull List<Item> items) {
    runOnUiThreadIfFragmentAlive(() -> {
      loadingUiView.setVisibility(GONE);
      errorUiView.setVisibility(GONE);
      contentUiRecyclerView.setVisibility(VISIBLE);
      itemsAdapter.setData(items);
    });
  }

  @OnClick(R.id.items_loading_error_try_again_button) void onTryAgainButtonClick() {
    itemsPresenter.reloadData();
  }

  @Override public void onDestroyView() {
    itemsPresenter.unbindView(this);
    super.onDestroyView();
  }

  @Subcomponent(modules = ItemsFragmentModule.class) public interface ItemsFragmentComponent {
    void inject(@NonNull ItemsFragment itemsFragment);
  }

  @Module public static class ItemsFragmentModule {

    @Provides @NonNull public ItemsPresenter provideItemsPresenter(@NonNull ItemsModel itemsModel,
        @NonNull AnalyticsModel analyticsModel) {
      return new ItemsPresenter(
          ItemsPresenterConfiguration.builder().ioScheduler(Schedulers.io()).build(), itemsModel,
          analyticsModel);
    }
  }
}
