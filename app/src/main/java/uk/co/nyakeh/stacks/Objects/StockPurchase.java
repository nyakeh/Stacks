package uk.co.nyakeh.stacks.objects;

import java.util.Date;
import java.util.UUID;

public class StockPurchase {
    public UUID Id;
    public String Symbol;
    public Date DatePurchased;
    public int Price;
    public int Quantity;
    public double Fee;
    public double Total;

    public StockPurchase(UUID id, String symbol, Date datePurchased, int price, int quantity, double fee, double total) {
        Id = id;
        Symbol = symbol;
        DatePurchased = datePurchased;
        Price = price;
        Quantity = quantity;
        Fee = fee;
        Total = total;
    }
}