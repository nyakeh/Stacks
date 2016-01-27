package uk.co.nyakeh.stacks;

public class YahooQuote {
    public YahooQuote(String symbol, String date, String open, String high, String low, String close, String volume, String adj_Close) {
        Symbol = symbol;
        Date = date;
        Open = open;
        High = high;
        Low = low;
        Close = close;
        Volume = volume;
        Adj_Close = adj_Close;
    }

    public static String Symbol;
    public static String Date;
    public static String Open;
    public static String High;
    public static String Low;
    public static String Close;
    public static String Volume;
    public static String Adj_Close;
}
