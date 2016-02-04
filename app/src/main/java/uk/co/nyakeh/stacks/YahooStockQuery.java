package uk.co.nyakeh.stacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YahooStockQuery {

    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("created")
    @Expose
    public String created;
    @SerializedName("lang")
    @Expose
    public String lang;
    @SerializedName("results")
    @Expose
    public YahooStockResults results;
}
