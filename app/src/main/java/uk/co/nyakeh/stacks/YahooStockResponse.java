package uk.co.nyakeh.stacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YahooStockResponse {
    @SerializedName("query")
    @Expose
    public YahooStockQuery query;
}
