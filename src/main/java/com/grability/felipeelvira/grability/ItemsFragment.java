package com.grability.felipeelvira.grability;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinsBeard;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by felipeelvira on 5/31/16.
 */
public class ItemsFragment extends Fragment implements ItemClickListener {
    List<StoreItem> storeItems = new ArrayList<>();
    public Merlin merlin;
    public MerlinsBeard merlinsBeard;
    private RecyclerView recyclerView;
    private ItemsAdapter adapter;
    private ItemLocalStorage localStorage;
    private boolean isPhone;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.content_main,container, false);

        localStorage = new ItemLocalStorage(getContext());

        merlin = new Merlin.Builder().withConnectableCallbacks().build(getContext());
        merlinsBeard = MerlinsBeard.from(getContext());
        isPhone = getResources().getBoolean(R.bool.is_phone);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        setGridManager(isPhone);

        checkConnection();

        return view;
    }

    private void setGridManager(Boolean isphone){
        int columns;
        if(isphone)
            columns = 1;
        else
            columns = 3;

        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),columns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new SlideInLeftAnimator());

        Log.d("isPhone", String.valueOf(isphone));
    }

    public void prepareItemsData (){
        ServerRequests requests = new ServerRequests(getContext());
        requests.fetchServerData( new ItemCallBack() {
            @Override
            public void done( ArrayList<StoreItem> returnedItems) {
                storeItems= returnedItems;
                prepareAdapter ();
            }
        });
    }
    private void prepareAdapter (){
        adapter = new ItemsAdapter(storeItems, getContext(),this);
        recyclerView.setAdapter(adapter);
    }
    private void fragmentTransition (View v, int i){


        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle bdl=new Bundle();
        int itemNumber =i;

        bdl.putInt("itemNumber",itemNumber);
        fragment.setArguments(bdl);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(new ItemsTransition());
            fragment.setEnterTransition(new Fade());
            setExitTransition(new Fade());
            fragment.setSharedElementReturnTransition(new ItemsTransition());
        }

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(v, "itemImage")
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void checkConnection(){

        if (merlinsBeard.isConnected()) {

            if(localStorage.getLocalItems() != null){
                storeItems = localStorage.getLocalItems();
                prepareAdapter ();
            }else {
                prepareItemsData ();
            }

        } else {
            connectionNotification("Your device has not access to network connection.");
            if(localStorage.getLocalItems() != null){
                storeItems = localStorage.getLocalItems();
                prepareAdapter ();
            }else {
                connectionNotification("Please, verify internet connection.");
            }

        }
    }

    private void connectionNotification(String text){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(text);


        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onItemClicked(ItemsViewHolder holder, int position) {
        Log.d("s","clicked");
        fragmentTransition (holder.imageView, position);
    }
}