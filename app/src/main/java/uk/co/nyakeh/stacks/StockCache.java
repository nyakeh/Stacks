package uk.co.nyakeh.stacks;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StockCache {
    public static final String STOCK_RESPONSE_KEY = "|RESULT";
    public static final int HOUR_IN_MS = 3600000;
    private static StockCache Instance = new StockCache();
    private Map<String, Date> _lastLookup;
    private Map<String, String> _lastResponse;

    private StockCache() {
        _lastLookup = new HashMap<>();
        _lastResponse = new HashMap<>();
    }

    public static StockCache GetInstance() {
        return Instance;
    }

    public boolean ServeCachedResult(String share) {
        if (_lastLookup.containsKey(share)) {
            if (_lastLookup.get(share).getTime() + HOUR_IN_MS > new Date().getTime()) {
                return true;
            }
        }
        return false;
    }

    public String CachedLookupFor(String share) {
        return _lastResponse.get(share);
    }

    public void CacheResult(Context context, String share, String response) {
        _lastLookup.put(share, new Date());
        _lastResponse.put(share, response);
        SharedPreferences sharedPreferences = context.getSharedPreferences("uk.co.nyakeh.stacks", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(share + STOCK_RESPONSE_KEY, response).apply();
    }

    public String PersistedLookupFor(Context context, String share) {
        if (_lastLookup.containsKey(share)) {
            return CachedLookupFor(share);
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("uk.co.nyakeh.stacks", Context.MODE_PRIVATE);
        String stockResponse = sharedPreferences.getString(share + STOCK_RESPONSE_KEY, "");
        if (stockResponse != "") {
            return stockResponse;
        }
        return "";
    }
}