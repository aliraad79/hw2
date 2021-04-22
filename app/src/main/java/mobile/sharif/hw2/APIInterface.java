package mobile.sharif.hw2;

import android.util.Log;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIInterface {

    private static String api_key = "sk.eyJ1IjoicmFhZHRlZCIsImEiOiJja25zdHRtM3kwN3RyMnVuM2lzZ2FxajRvIn0.XjMBN0r7YwG_Xpl6dwc4Lw";

    public APIInterface() {
    }

    private void extractCoinFromResponse(String response) {
        try {
        } catch (Exception e) {
            Log.i("JSON", e.toString());
        }
    }

    public void retrieveCoinFromApi() {

        OkHttpClient okHttpClient = new OkHttpClient();
        String query = "LOS";
        String uri = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + query + ".json?access_token=" + api_key;
        String url = Objects.requireNonNull(HttpUrl.parse(uri)).newBuilder().build().toString();
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
                    extractCoinFromResponse(resp);
                }
            }
        });
    }
}
