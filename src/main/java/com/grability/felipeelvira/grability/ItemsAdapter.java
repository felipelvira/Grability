package com.grability.felipeelvira.grability;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by felipeelvira on 5/28/16.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsViewHolder> {

    private List<StoreItem> storeItemList = Collections.emptyList();
    private Context context;

    private final ItemClickListener listener;

    public ItemsAdapter(List<StoreItem> storeItemList, Context context, ItemClickListener itemClickListener) {
        this.storeItemList = storeItemList;
        this.context = context;
        this.listener = itemClickListener;
    }

    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new ItemsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ItemsViewHolder holder, final int position) {
        StoreItem item = storeItemList.get(position);

        holder.title.setText(item.getLabel());
        Picasso.with(context)
                .load(item.getThumbnail())
                .fit()
                .into(holder.imageView);

        ViewCompat.setTransitionName(holder.imageView, String.valueOf(position) + "_image");

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(holder, position);
            }
        });

        Log.d("Item thumbnail", item.getThumbnail());

    }
    @Override
    public int getItemCount() {
        return storeItemList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void insert(int position, StoreItem item) {
        storeItemList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(StoreItem item) {
        int position = storeItemList.indexOf(item);
        storeItemList.remove(position);
        notifyItemRemoved(position);
    }

}
