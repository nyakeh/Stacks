package uk.co.nyakeh.stacks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import uk.co.nyakeh.stacks.objects.StockPurchase;
import uk.co.nyakeh.stacks.database.StockDbSchema.StockPurchaseTable;

public class StockLab {
    private static StockLab sStockLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private StockLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new StockBaseHelper(mContext).getWritableDatabase();
    }

    public static StockLab get(Context context) {
        if (sStockLab == null) {
            sStockLab = new StockLab(context);
        }
        return sStockLab;
    }

    public void addStockPurchase(StockPurchase stockPurchase) {
        ContentValues values = getStockPurchaseContentValues(stockPurchase);
        mDatabase.insert(StockPurchaseTable.NAME, null, values);
    }

    private ContentValues getStockPurchaseContentValues(StockPurchase stockPurchase) {
        ContentValues values = new ContentValues();
        values.put(StockPurchaseTable.Cols.ID, stockPurchase.Id.toString());
        values.put(StockPurchaseTable.Cols.SYMBOL, stockPurchase.Symbol);
        values.put(StockPurchaseTable.Cols.DATE_PURCHASED, stockPurchase.DatePurchased.getTime());
        values.put(StockPurchaseTable.Cols.PRICE, stockPurchase.Price);
        values.put(StockPurchaseTable.Cols.COUNT, stockPurchase.Count);
        values.put(StockPurchaseTable.Cols.FEE, stockPurchase.Fee);
        values.put(StockPurchaseTable.Cols.TOTAL, stockPurchase.Total);
        return values;
    }

}