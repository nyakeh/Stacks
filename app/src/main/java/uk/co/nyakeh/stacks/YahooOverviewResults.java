package uk.co.nyakeh.stacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class YahooOverviewResults {

    @SerializedName("quote")
    @Expose
    public YahooOverviewQuote quote;
}
