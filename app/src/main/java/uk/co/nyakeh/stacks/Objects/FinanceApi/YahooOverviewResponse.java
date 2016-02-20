package uk.co.nyakeh.stacks.objects.FinanceApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YahooOverviewResponse {
    @SerializedName("query")
    @Expose
    public YahooOverviewQuery query;
}
