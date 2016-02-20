package uk.co.nyakeh.stacks.objects.FinanceApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YahooOverviewResults {

    @SerializedName("quote")
    @Expose
    public YahooOverviewQuote quote;
}
