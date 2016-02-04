package uk.co.nyakeh.stacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YahooOverviewResponse {
    @SerializedName("query")
    @Expose
    public YahooOverviewQuery query;
}
