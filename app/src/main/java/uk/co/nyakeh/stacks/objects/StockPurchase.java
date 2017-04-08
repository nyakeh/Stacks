package uk.co.nyakeh.stacks.objects;

import java.util.Date;
import java.util.UUID;

public class StockPurchase {
    public UUID Id;
    public String Symbol;
    public Date DatePurchased;
    public double Price;
    public double Quantity;
    public double Fee;
    public double Total;

    public StockPurchase(UUID id, String symbol, Date datePurchased, double price, double quantity, double fee) {
        Id = id;
        Symbol = symbol;
        DatePurchased = datePurchased;
        Price = price;
        Quantity = quantity;
        Fee = fee;
        Total = (quantity * price) + fee;
    }

    public StockPurchase(UUID id, String symbol, Date datePurchased, double price, double quantity, double fee, double total) {
        Id = id;
        Symbol = symbol;
        DatePurchased = datePurchased;
        Price = price;
        Quantity = quantity;
        Fee = fee;
        Total = total;
    }
}