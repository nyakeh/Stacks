package uk.co.nyakeh.stacks.objects;

public class Fund {
    public String Symbol;
    public double Price;
    public String Change;

    public Fund(String symbol, double price, String change) {
        Symbol = symbol;
        Price = price;
        Change = change;
    }
}