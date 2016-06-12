package com.grability.felipeelvira.grability;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by felipeelvira on 5/31/16.
 */
public class ItemDetailFragment extends Fragment {
    private ItemLocalStorage localStorage;
    private StoreItem storeItem;
    ImageView imageView;
    TextView itemName, itemDescription;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.item_detail,container, false);
        imageView =(ImageView) view.findViewById(R.id.itemImage);
        itemName = (TextView) view.findViewById(R.id.itemDName);
        itemDescription = (TextView) view.findViewById(R.id.itemDDescription);
        itemDescription.setMovementMethod(new ScrollingMovementMethod());

        localStorage = new ItemLocalStorage(getContext());
        int itemNumber=getArguments().getInt("itemNumber");

        storeItem = (StoreItem) localStorage.getLocalItems().get(itemNumber);
        setData();

        Log.d("itemNumber", String.valueOf(itemNumber));

        return view;
    }

    private void setData(){

        Picasso.with(getContext())
                .load(storeItem.getLarge())
                .fit()
                .into(imageView);
        itemName.setText(storeItem.getLabel());
        itemDescription.setText(storeItem.getDescription());
    }
}