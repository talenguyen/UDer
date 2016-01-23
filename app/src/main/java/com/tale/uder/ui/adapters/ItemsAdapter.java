package com.tale.uder.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.tale.uder.R;
import com.tale.uder.api.entities.Item;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemViewHolder> {

  @NonNull private final LayoutInflater layoutInflater;

  @NonNull private List<Item> items = emptyList();

  public ItemsAdapter(@NonNull LayoutInflater layoutInflater) {
    this.layoutInflater = layoutInflater;
  }

  @Override public int getItemCount() {
    return items.size();
  }

  public void setData(@NonNull List<Item> items) {
    this.items = unmodifiableList(items); // Prevent possible side-effects.
    notifyDataSetChanged();
  }

  @Override @NonNull
  public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ItemViewHolder(layoutInflater.inflate(R.layout.list_item, parent, false));
  }

  @Override public void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int position) {
    viewHolder.bind(items.get(position));
  }
}
