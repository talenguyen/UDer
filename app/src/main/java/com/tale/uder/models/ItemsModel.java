package com.tale.uder.models;

import android.support.annotation.NonNull;
import com.tale.uder.api.UderRestApi;
import com.tale.uder.api.entities.Item;
import java.util.List;
import rx.Single;

/**
 * Model is not an entity. It's a container for business logic code. M(VC), M(VP), M(VVM).
 * <p>
 * Why create Model classes? Such classes hide complex logic required to fetch/cache/process/etc
 * data.
 * So Presentation layer won't know the details of implementation and each class will do only one
 * job (SOLID).
 */
public class ItemsModel {

  @NonNull private final UderRestApi uderRestApi;

  public ItemsModel(@NonNull UderRestApi uderRestApi) {
    this.uderRestApi = uderRestApi;
  }

  @NonNull public Single<List<Item>> getItems() {
    return uderRestApi.items();
  }
}
