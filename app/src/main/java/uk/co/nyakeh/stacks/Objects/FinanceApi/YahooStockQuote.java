package uk.co.nyakeh.stacks.Objects.FinanceApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YahooStockQuote {
    @SerializedName("Symbol")
    @Expose
    public String Symbol;
    @SerializedName("Date")
    @Expose
    public String Date;
    @SerializedName("Open")
    @Expose
    public String Open;
    @SerializedName("High")
    @Expose
    public String High;
    @SerializedName("Low")
    @Expose
    public String Low;
    @SerializedName("Close")
    @Expose
    public String Close;
    @SerializedName("Volume")
    @Expose
    public String Volume;
    @SerializedName("Adj_Close")
    @Expose
    public String AdjClose;
}
