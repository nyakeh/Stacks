package uk.co.nyakeh.stacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YahooOverviewQuote {
    @SerializedName("symbol")
    @Expose
    public String symbol;
    @SerializedName("Ask")
    @Expose
    public String Ask;
    @SerializedName("AverageDailyVolume")
    @Expose
    public Object AverageDailyVolume;
    @SerializedName("Bid")
    @Expose
    public String Bid;
    @SerializedName("AskRealtime")
    @Expose
    public Object AskRealtime;
    @SerializedName("BidRealtime")
    @Expose
    public Object BidRealtime;
    @SerializedName("BookValue")
    @Expose
    public String BookValue;
    @SerializedName("Change_PercentChange")
    @Expose
    public String ChangePercentChange;
    @SerializedName("Change")
    @Expose
    public String Change;
    @SerializedName("Commission")
    @Expose
    public Object Commission;
    @SerializedName("Currency")
    @Expose
    public String Currency;
    @SerializedName("ChangeRealtime")
    @Expose
    public Object ChangeRealtime;
    @SerializedName("AfterHoursChangeRealtime")
    @Expose
    public Object AfterHoursChangeRealtime;
    @SerializedName("DividendShare")
    @Expose
    public Object DividendShare;
    @SerializedName("LastTradeDate")
    @Expose
    public String LastTradeDate;
    @SerializedName("TradeDate")
    @Expose
    public Object TradeDate;
    @SerializedName("EarningsShare")
    @Expose
    public Object EarningsShare;
    @SerializedName("ErrorIndicationreturnedforsymbolchangedinvalid")
    @Expose
    public Object ErrorIndicationreturnedforsymbolchangedinvalid;
    @SerializedName("EPSEstimateCurrentYear")
    @Expose
    public Object EPSEstimateCurrentYear;
    @SerializedName("EPSEstimateNextYear")
    @Expose
    public Object EPSEstimateNextYear;
    @SerializedName("EPSEstimateNextQuarter")
    @Expose
    public String EPSEstimateNextQuarter;
    @SerializedName("DaysLow")
    @Expose
    public String DaysLow;
    @SerializedName("DaysHigh")
    @Expose
    public String DaysHigh;
    @SerializedName("YearLow")
    @Expose
    public String YearLow;
    @SerializedName("YearHigh")
    @Expose
    public String YearHigh;
    @SerializedName("HoldingsGainPercent")
    @Expose
    public Object HoldingsGainPercent;
    @SerializedName("AnnualizedGain")
    @Expose
    public Object AnnualizedGain;
    @SerializedName("HoldingsGain")
    @Expose
    public Object HoldingsGain;
    @SerializedName("HoldingsGainPercentRealtime")
    @Expose
    public Object HoldingsGainPercentRealtime;
    @SerializedName("HoldingsGainRealtime")
    @Expose
    public Object HoldingsGainRealtime;
    @SerializedName("MoreInfo")
    @Expose
    public Object MoreInfo;
    @SerializedName("OrderBookRealtime")
    @Expose
    public Object OrderBookRealtime;
    @SerializedName("MarketCapitalization")
    @Expose
    public Object MarketCapitalization;
    @SerializedName("MarketCapRealtime")
    @Expose
    public Object MarketCapRealtime;
    @SerializedName("EBITDA")
    @Expose
    public Object EBITDA;
    @SerializedName("ChangeFromYearLow")
    @Expose
    public String ChangeFromYearLow;
    @SerializedName("PercentChangeFromYearLow")
    @Expose
    public String PercentChangeFromYearLow;
    @SerializedName("LastTradeRealtimeWithTime")
    @Expose
    public Object LastTradeRealtimeWithTime;
    @SerializedName("ChangePercentRealtime")
    @Expose
    public Object ChangePercentRealtime;
    @SerializedName("ChangeFromYearHigh")
    @Expose
    public String ChangeFromYearHigh;
    @SerializedName("PercebtChangeFromYearHigh")
    @Expose
    public String PercebtChangeFromYearHigh;
    @SerializedName("LastTradeWithTime")
    @Expose
    public String LastTradeWithTime;
    @SerializedName("LastTradePriceOnly")
    @Expose
    public String LastTradePriceOnly;
    @SerializedName("HighLimit")
    @Expose
    public Object HighLimit;
    @SerializedName("LowLimit")
    @Expose
    public Object LowLimit;
    @SerializedName("DaysRange")
    @Expose
    public String DaysRange;
    @SerializedName("DaysRangeRealtime")
    @Expose
    public Object DaysRangeRealtime;
    @SerializedName("FiftydayMovingAverage")
    @Expose
    public String FiftydayMovingAverage;
    @SerializedName("TwoHundreddayMovingAverage")
    @Expose
    public String TwoHundreddayMovingAverage;
    @SerializedName("ChangeFromTwoHundreddayMovingAverage")
    @Expose
    public String ChangeFromTwoHundreddayMovingAverage;
    @SerializedName("PercentChangeFromTwoHundreddayMovingAverage")
    @Expose
    public String PercentChangeFromTwoHundreddayMovingAverage;
    @SerializedName("ChangeFromFiftydayMovingAverage")
    @Expose
    public String ChangeFromFiftydayMovingAverage;
    @SerializedName("PercentChangeFromFiftydayMovingAverage")
    @Expose
    public String PercentChangeFromFiftydayMovingAverage;
    @SerializedName("Name")
    @Expose
    public String Name;
    @SerializedName("Notes")
    @Expose
    public Object Notes;
    @SerializedName("Open")
    @Expose
    public String Open;
    @SerializedName("PreviousClose")
    @Expose
    public String PreviousClose;
    @SerializedName("PricePaid")
    @Expose
    public Object PricePaid;
    @SerializedName("ChangeinPercent")
    @Expose
    public String ChangeinPercent;
    @SerializedName("PriceSales")
    @Expose
    public Object PriceSales;
    @SerializedName("PriceBook")
    @Expose
    public Object PriceBook;
    @SerializedName("ExDividendDate")
    @Expose
    public Object ExDividendDate;
    @SerializedName("PERatio")
    @Expose
    public Object PERatio;
    @SerializedName("DividendPayDate")
    @Expose
    public Object DividendPayDate;
    @SerializedName("PERatioRealtime")
    @Expose
    public Object PERatioRealtime;
    @SerializedName("PEGRatio")
    @Expose
    public String PEGRatio;
    @SerializedName("PriceEPSEstimateCurrentYear")
    @Expose
    public Object PriceEPSEstimateCurrentYear;
    @SerializedName("PriceEPSEstimateNextYear")
    @Expose
    public Object PriceEPSEstimateNextYear;
    @SerializedName("Symbol")
    @Expose
    public String Symbol;
    @SerializedName("SharesOwned")
    @Expose
    public Object SharesOwned;
    @SerializedName("ShortRatio")
    @Expose
    public Object ShortRatio;
    @SerializedName("LastTradeTime")
    @Expose
    public String LastTradeTime;
    @SerializedName("TickerTrend")
    @Expose
    public Object TickerTrend;
    @SerializedName("OneyrTargetPrice")
    @Expose
    public Object OneyrTargetPrice;
    @SerializedName("Volume")
    @Expose
    public String Volume;
    @SerializedName("HoldingsValue")
    @Expose
    public Object HoldingsValue;
    @SerializedName("HoldingsValueRealtime")
    @Expose
    public Object HoldingsValueRealtime;
    @SerializedName("YearRange")
    @Expose
    public String YearRange;
    @SerializedName("DaysValueChange")
    @Expose
    public Object DaysValueChange;
    @SerializedName("DaysValueChangeRealtime")
    @Expose
    public Object DaysValueChangeRealtime;
    @SerializedName("StockExchange")
    @Expose
    public String StockExchange;
    @SerializedName("DividendYield")
    @Expose
    public Object DividendYield;
    @SerializedName("PercentChange")
    @Expose
    public String PercentChange;
}
