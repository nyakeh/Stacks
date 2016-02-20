package uk.co.nyakeh.stacks.Objects;

import java.util.Date;
import java.util.UUID;

public class StockPurchase {
    public UUID mId;
    public String mSymbol;
    public Date mDatePurchased;
    public int mPrice;
    public int mCount;
    public double mFee;
    public double mTotal;

    public StockPurchase(UUID id, String symbol, Date datePurchased, int price, int count, double fee, double total) {
        mId = id;
        mSymbol = symbol;
        mDatePurchased = datePurchased;
        mPrice = price;
        mCount = count;
        mFee = fee;
        mTotal = total;
    }
}