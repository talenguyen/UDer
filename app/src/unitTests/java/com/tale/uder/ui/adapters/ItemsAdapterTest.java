package com.tale.uder.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tale.uder.UderRobolectricUnitTestRunner;
import com.tale.uder.R;
import com.tale.uder.api.entities.Item;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(UderRobolectricUnitTestRunner.class) public class ItemsAdapterTest {

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private LayoutInflater layoutInflater;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private ItemsAdapter adapter;

  @Before public void beforeEachTest() {
    layoutInflater = mock(LayoutInflater.class);
    adapter = new ItemsAdapter(layoutInflater);
  }

  @Test public void getItemCount_shouldBeZeroByDefault() {
    assertThat(adapter.getItemCount()).isEqualTo(0);
  }

  @Test public void getItemCount_shouldReturnCorrectValueAfterSetData() {
    adapter.setData(asList(mock(Item.class), mock(Item.class)));
    assertThat(adapter.getItemCount()).isEqualTo(2);
  }

  @Test public void setData_shouldNotifyObserversAboutChange() {
    RecyclerView.AdapterDataObserver observer = mock(RecyclerView.AdapterDataObserver.class);
    adapter.registerAdapterDataObserver(observer);

    adapter.setData(emptyList());
    verify(observer).onChanged();
    verifyNoMoreInteractions(observer);
  }

  @Test public void onCreateViewHolder_shouldCreateViewHolder() {
    ViewGroup parent = mock(ViewGroup.class);
    View itemView = mock(View.class);

    ImageView imageView = mock(ImageView.class);
    when(itemView.findViewById(R.id.list_item_image_view)).thenReturn(imageView);

    TextView titleTextView = mock(TextView.class);
    when(itemView.findViewById(R.id.list_item_title_text_view)).thenReturn(titleTextView);

    TextView shortDescriptionTextView = mock(TextView.class);
    when(itemView.findViewById(R.id.list_item_short_description_text_view)).thenReturn(
        shortDescriptionTextView);

    when(layoutInflater.inflate(R.layout.list_item, parent, false)).thenReturn(itemView);

    ItemViewHolder viewHolder = adapter.onCreateViewHolder(parent, 0);
    verify(layoutInflater).inflate(R.layout.list_item, parent, false);

    assertThat(viewHolder.itemView).isSameAs(itemView);
    // Also, we don't expect exceptions here.
  }

  @Test public void onBindViewHolder_shouldBindItemsToTheViewHolders() {
    List<Item> items = asList(Item.builder()
        .id("1")
        .title("t1")
        .shortDescription("s1")
        .build(), Item.builder()
        .id("2")
        .title("t2")
        .shortDescription("s2")
        .build(), Item.builder()
        .id("3")
        .title("t3")
        .shortDescription("s3")
        .build());

    adapter.setData(items);

    for (int position = 0, size = items.size(); position < size; position++) {
      ItemViewHolder itemViewHolder = mock(ItemViewHolder.class);
      adapter.onBindViewHolder(itemViewHolder, position);

      verify(itemViewHolder).bind(items.get(position));
    }
  }
}