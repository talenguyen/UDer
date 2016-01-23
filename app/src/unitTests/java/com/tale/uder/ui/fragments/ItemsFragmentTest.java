package com.tale.uder.ui.fragments;

import com.tale.uder.UderRobolectricUnitTestRunner;
import com.tale.uder.ui.presenters.ItemsPresenter;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(UderRobolectricUnitTestRunner.class) public class ItemsFragmentTest {

  @Test public void onTryAgainButtonClick_shouldRequestDataFromPresenter() {
    ItemsFragment itemsFragment = new ItemsFragment();
    ItemsPresenter itemsPresenter = mock(ItemsPresenter.class);

    itemsFragment.itemsPresenter = itemsPresenter;
    itemsFragment.onTryAgainButtonClick();
    verify(itemsPresenter).reloadData();
  }
}