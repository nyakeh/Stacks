package uk.co.nyakeh.stacks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.UnknownHostException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleFinanceClient extends AsyncTask<String, Integer, String> {
    private static final String GOOGLE_FINANCE_URL = "http://finance.google.com/finance/info?client=ig&q=";
    private IAsyncTask _activity;
    private Context _context;

    public GoogleFinanceClient(IAsyncTask activity, Context context) {
        _activity = activity;
        _context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        String share = params[0];
        StockCache stockCache = StockCache.GetInstance();
        if (stockCache.ServeCachedResult(share)) {
            return stockCache.CachedLookupFor(share);
        }
        Request request = new Request.Builder()
                .url(GOOGLE_FINANCE_URL + share)
                .build();
        try {
            Response response = new OkHttpClient().newCall(request).execute();
            result = response.body().string();
            stockCache.CacheResult(_context, share, result);
        } catch (UnknownHostException unknownHostException) {
            return stockCache.PersistedLookupFor(_context, share);
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
