package com.tale.uder.api;

import android.support.annotation.NonNull;
import com.tale.uder.api.entities.Item;
import java.util.List;
import retrofit.http.GET;
import rx.Single;

public interface UderRestApi {

  @GET("items") @NonNull Single<List<Item>> items();
}
