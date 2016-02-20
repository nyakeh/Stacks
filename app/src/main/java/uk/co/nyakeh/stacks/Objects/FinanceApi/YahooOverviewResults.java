package uk.co.nyakeh.stacks.Objects.FinanceApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YahooOverviewResults {

    @SerializedName("quote")
    @Expose
    public YahooOverviewQuote quote;
}
