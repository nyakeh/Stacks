package uk.co.nyakeh.stacks.objects.FinanceApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class YahooStockResults {

    @SerializedName("quote")
    @Expose
    public List<YahooStockQuote> quote = new ArrayList<YahooStockQuote>();
}
