package com.grability.felipeelvira.grability;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by felipeelvira on 5/28/16.
 */
public class ServerRequests {

    public static ProgressDialog progressDialog;
    public static String storeURL = "https://itunes.apple.com/us/rss/topfreeapplications/limit=25/json";
    private static  ItemLocalStorage localStorage;

    public ServerRequests(Context context) {
        localStorage = new ItemLocalStorage(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Cargando");
        progressDialog.setMessage("Espera por favor...");
    }

    public void fetchServerData(ItemCallBack itemCallBack) {
        progressDialog.show();
        new fetchServerDataAsyncTask( itemCallBack).execute();
    }

    public class fetchServerDataAsyncTask extends AsyncTask<Void , Void, ArrayList<StoreItem>>{

        ArrayList<StoreItem> storeItem;
        ItemCallBack itemCallBack;
        String responce;

        public fetchServerDataAsyncTask( ItemCallBack itemCallBack){
            //this.storeItem = storeItem;
            this.itemCallBack = itemCallBack;
        }

        @Override
        protected ArrayList<StoreItem> doInBackground(Void... params) {
            HashMap<String, String> postDataParams;
            postDataParams = new HashMap<>();

            storeItem = new ArrayList<StoreItem>();


            responce = ServerData(storeURL, postDataParams);
            //Log.d("Server Data", responce);
            try {
                JSONObject jsonObject = new JSONObject(responce);
                JSONObject entry = new JSONObject(jsonObject.getString("feed"));
                JSONArray jsonArray = new JSONArray(entry.getString("entry"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_data = jsonArray.getJSONObject(i);

                    String label = json_data.getJSONObject("im:name").getString("label");
                    String pic = json_data.getJSONArray("im:image").getJSONObject(0).getString("label");
                    String large = json_data.getJSONArray("im:image").getJSONObject(2).getString("label");
                    String description = json_data.getJSONObject("summary").getString("label");

                    storeItem.add(new StoreItem(label, pic,large,description));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(storeItem != null)
                localStorage.StorageItemData(storeItem);

            return storeItem;
        }
        @Override
        protected void onPostExecute(ArrayList<StoreItem> storeItem) {
            progressDialog.dismiss();
            itemCallBack.done(storeItem);
            super.onPostExecute(storeItem);
        }
    }

    public String ServerData(String path,HashMap<String, String> params) {
        String response="";
        URL url;

        try {
            url = new URL(path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(params));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                //Log.d("Output",br.toString());
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        Log.d("getPostDataString ", result.toString());
        return result.toString();
    }
}
