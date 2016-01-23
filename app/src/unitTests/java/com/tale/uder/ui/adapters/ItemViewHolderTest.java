package com.tale.uder.ui.adapters;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import com.tale.uder.UderRobolectricUnitTestRunner;
import com.tale.uder.R;
import com.tale.uder.api.entities.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(UderRobolectricUnitTestRunner.class) public class ItemViewHolderTest {

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private View itemView;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private ItemViewHolder itemViewHolder;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private TextView titleTextView;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private TextView shortDescriptionTextView;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private Item item;

  @Before public void beforeEachTest() {

    itemView = mock(View.class);

    titleTextView = mock(TextView.class);
    shortDescriptionTextView = mock(TextView.class);

    when(itemView.findViewById(R.id.list_item_title_text_view)).thenReturn(titleTextView);
    when(itemView.findViewById(R.id.list_item_short_description_text_view)).thenReturn(
        shortDescriptionTextView);

    itemViewHolder = new ItemViewHolder(itemView);

    item = Item.builder()
        .id("1")
        .title("Test title")
        .shortDescription("Desc")
        .build();
  }

  @Test public void bind_shouldSetTitle() {
    itemViewHolder.bind(item);
    verify(titleTextView).setText(item.title());
  }

  @Test public void bind_shouldSetShortDescription() {
    itemViewHolder.bind(item);
    verify(shortDescriptionTextView).setText(item.shortDescription());
  }
}