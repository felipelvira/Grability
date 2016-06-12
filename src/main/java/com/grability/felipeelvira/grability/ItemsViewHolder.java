package com.grability.felipeelvira.grability;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by felipeelvira on 6/11/16.
 */
public class ItemsViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public ImageView imageView;
    public RelativeLayout relativeLayout;

    public ItemsViewHolder(View view) {
        super(view);

        title = (TextView) view.findViewById(R.id.title);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);

    }

}
