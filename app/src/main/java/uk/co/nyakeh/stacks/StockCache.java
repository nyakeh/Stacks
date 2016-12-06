package uk.co.nyakeh.stacks;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StockCache {
    public static final int HOUR_IN_MS = 3600000;
    private static StockCache ourInstance = new StockCache();
    private Map<String, Date> _lastLookup;
    private Map<String,String> _lastResponse;

    private StockCache() {
        _lastLookup = new HashMap<>();
        _lastResponse = new HashMap<>();
    }

    public static StockCache GetInstance() {
        return ourInstance;
    }

    public boolean ServeCachedResult(String share){
        if (_lastLookup.containsKey(share)) {
            if (_lastLookup.get(share).getTime() + HOUR_IN_MS > new Date().getTime()) {
                return true;
            }
        }
        return false;
    }

    public String CachedLookupFor(String share){
        return _lastResponse.get(share);
    }

    public void CacheResult(String share, String response){
         _lastLookup.put(share, new Date());
         _lastResponse.put(share, response);
    }
}