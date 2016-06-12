package com.grability.felipeelvira.grability;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by felipeelvira on 5/29/16.
 */
public class ItemLocalStorage {

    public static final String ST_NM = "itemDetails";
    private static String dbName = "itemsArray";
    SharedPreferences itemLocalDB;
    Gson gson;

    public ItemLocalStorage(Context context) {
        itemLocalDB = context.getSharedPreferences(ST_NM, 0);
        gson = new Gson();
    }

    public void StorageItemData(List storeItem){
        SharedPreferences.Editor spEditor = itemLocalDB.edit();
        String jsonItems = gson.toJson(storeItem);
        spEditor.putString(dbName, jsonItems);

        spEditor.commit();
    }
    public ArrayList getLocalItems (){
        List items;

        if(itemLocalDB.contains(dbName)){
            String jsonItems = itemLocalDB.getString(dbName, null);
            StoreItem[] favoriteItems = gson.fromJson(jsonItems,StoreItem[].class);
            items = Arrays.asList(favoriteItems);
            items = new ArrayList(items);
        }else
            return null;

        return (ArrayList) items;
    }

    public void clearUsrData() {
        SharedPreferences.Editor spEdit = itemLocalDB.edit();
        spEdit.clear();
        spEdit.commit();
    }

}
