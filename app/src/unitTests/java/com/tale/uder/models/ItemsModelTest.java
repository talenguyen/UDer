package com.tale.uder.models;

import android.support.annotation.NonNull;
import com.tale.uder.api.UderRestApi;
import com.tale.uder.api.entities.Item;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import rx.Single;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemsModelTest {

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private UderRestApi uderRestApi;

  @SuppressWarnings("NullableProblems") // Initialized in @Before.
  @NonNull private ItemsModel itemsModel;

  @Before public void beforeEachTest() {
    uderRestApi = mock(UderRestApi.class);
    itemsModel = new ItemsModel(uderRestApi);
  }

  @Test public void getItems_shouldReturnItemsFromUderRestApi() {
    List<Item> items = Arrays.asList(mock(Item.class), mock(Item.class));
    when(uderRestApi.items()).thenReturn(Single.just(items));

    assertThat(itemsModel.getItems().toBlocking().value()).containsExactlyElementsOf(items);
  }

  @Test public void getItems_shouldReturnErrorFromUderRestApi() {
    Exception error = new RuntimeException();
    when(uderRestApi.items()).thenReturn(Single.error(error));

    try {
      itemsModel.getItems().toBlocking().value();
      failBecauseExceptionWasNotThrown(RuntimeException.class);
    } catch (Exception expected) {
      assertThat(expected).isSameAs(error);
    }
  }
}