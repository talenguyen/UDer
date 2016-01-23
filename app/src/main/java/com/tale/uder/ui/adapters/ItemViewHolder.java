package com.tale.uder.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.tale.uder.R;
import com.tale.uder.api.entities.Item;

class ItemViewHolder extends RecyclerView.ViewHolder {

  private final TextView titleTextView;

  private final TextView shortDescriptionTextView;

  public ItemViewHolder(@NonNull View itemView) {
    super(itemView);
    titleTextView = (TextView) itemView.findViewById(R.id.list_item_title_text_view);
    shortDescriptionTextView =
        (TextView) itemView.findViewById(R.id.list_item_short_description_text_view);
  }

  public void bind(@NonNull Item item) {
    titleTextView.setText(item.title());
    shortDescriptionTextView.setText(item.shortDescription());
  }
}
