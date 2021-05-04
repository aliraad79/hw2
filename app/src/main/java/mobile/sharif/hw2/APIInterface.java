package mobile.sharif.hw2;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIInterface {
    public APIInterface() {
    }

    private void extractWordsFromResponse(String response, MainActivity.MyHandler handler) {
        ArrayList<MyLocation> foundLocations = new ArrayList<>();
        try {
            JSONArray arr = new JSONObject(response).getJSONArray("features");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String text = obj.getString("text");
                JSONArray center = obj.getJSONArray("center");
                MyLocation temp = new MyLocation(Double.parseDouble(center.getString(0)), Double.parseDouble(center.getString(1)), text);
                foundLocations.add(temp);
                Log.i("Query", text);
            }
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putSerializable(MainActivity.FOUND_LOCATIONS,foundLocations);
            msg.setData(bundle);
            msg.what = MainActivity.RESULTS;
            handler.sendMessage(msg);
        } catch (Exception e) {
            Log.i("JSON", e.toString());
        }
    }

    public void retrieveWords(String query, MainActivity.MyHandler handler) {

        OkHttpClient okHttpClient = new OkHttpClient();
        String api_key = "sk.eyJ1IjoicmFhZHRlZCIsImEiOiJja25zdHRtM3kwN3RyMnVuM2lzZ2FxajRvIn0.XjMBN0r7YwG_Xpl6dwc4Lw";
        String uri = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + query + ".json?access_token=" + api_key;
        String url = HttpUrl.parse(uri).newBuilder().build().toString();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("TAG", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String resp = response.body().string();
                    extractWordsFromResponse(resp, handler);
                }
            }
        });
    }
}
