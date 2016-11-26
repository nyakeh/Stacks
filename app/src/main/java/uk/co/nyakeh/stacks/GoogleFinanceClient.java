package uk.co.nyakeh.stacks;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleFinanceClient extends AsyncTask<String, Integer, String> {
    public static final String GOOGLE_FINANCE_URL = "http://finance.google.com/finance/info?client=ig&q=";
    private IAsyncTask _activity;

    public GoogleFinanceClient(IAsyncTask activity) {
        _activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        String share = params[0];
        Request request = new Request.Builder()
                .url(GOOGLE_FINANCE_URL + share)
                .build();
        try {
            Response response = new OkHttpClient().newCall(request).execute();
            result = response.body().string();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        _activity.PostExecute(result);
    }
}
