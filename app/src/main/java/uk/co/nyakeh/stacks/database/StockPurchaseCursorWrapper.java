package uk.co.nyakeh.stacks.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import uk.co.nyakeh.stacks.objects.StockPurchase;
import uk.co.nyakeh.stacks.database.StockDbSchema.StockPurchaseTable;

public class StockPurchaseCursorWrapper extends CursorWrapper {

    public StockPurchaseCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public StockPurchase getStockPurchase() {
        String id = getString(getColumnIndex(StockPurchaseTable.Cols.ID));
        String symbol = getString(getColumnIndex(StockPurchaseTable.Cols.SYMBOL));
        Long datePurchased = getLong(getColumnIndex(StockPurchaseTable.Cols.DATE_PURCHASED));
        double price = getDouble(getColumnIndex(StockPurchaseTable.Cols.PRICE));
        int quantity = getInt(getColumnIndex(StockPurchaseTable.Cols.QUANTITY));
        double fee = getDouble(getColumnIndex(StockPurchaseTable.Cols.FEE));
        double total = getDouble(getColumnIndex(StockPurchaseTable.Cols.TOTAL));

        StockPurchase stockPurchase = new StockPurchase(UUID.fromString(id),symbol,new Date(datePurchased), price, quantity, fee, total);
        return stockPurchase;
    }
}
