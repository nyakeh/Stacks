package uk.co.nyakeh.stacks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import uk.co.nyakeh.stacks.database.StockDbSchema.StockPurchaseTable;
import uk.co.nyakeh.stacks.database.MetadataDbSchema.MetadataTable;
import uk.co.nyakeh.stacks.records.Metadata;
import uk.co.nyakeh.stacks.records.StockPurchase;

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

    public List<StockPurchase> getStockPurchaseHistory(String stockSymbol) {
        ArrayList<StockPurchase> stockPurchases = new ArrayList<>();
        StockPurchaseCursorWrapper cursor = queryStockPurchases(StockPurchaseTable.Cols.SYMBOL + " = ?", new String[]{stockSymbol});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                StockPurchase stockPurchase = cursor.getStockPurchase();
                stockPurchases.add(stockPurchase);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Collections.sort(stockPurchases, new Comparator<StockPurchase>() {
            @Override
            public int compare(StockPurchase purchase1, StockPurchase purchase2) {
                return purchase1.DatePurchased.compareTo(purchase2.DatePurchased);
            }
        });
        return stockPurchases;
    }

    public void deleteStockPurchase(UUID id) {
        mDatabase.delete(StockPurchaseTable.NAME, StockPurchaseTable.Cols.ID + " = ?", new String[]{id.toString()});
    }

    public Metadata getMetadata() {
        Metadata metadata = null;
        MetadataCursorWrapper cursor = queryMetadata();
        try {
            cursor.moveToFirst();
            metadata = cursor.getMetadata();
        } finally {
            cursor.close();
        }
        return metadata;
    }

    public void updateMetadata(Metadata metadata) {
        ContentValues values = getMetadataContentValues(metadata);
        mDatabase.update(MetadataTable.NAME, values, null, null);
    }

    private StockPurchaseCursorWrapper queryStockPurchases(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(StockPurchaseTable.NAME,
                null,  // Columns - null selects *
                whereClause,
                whereArgs,
                null,  // groupBy
                null,  // having
                null); // orderBy

        return new StockPurchaseCursorWrapper(cursor);
    }

    private MetadataCursorWrapper queryMetadata() {
        Cursor cursor = mDatabase.query(MetadataDbSchema.MetadataTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        return new MetadataCursorWrapper(cursor);
    }

    private ContentValues getStockPurchaseContentValues(StockPurchase stockPurchase) {
        ContentValues values = new ContentValues();
        values.put(StockPurchaseTable.Cols.ID, stockPurchase.Id.toString());
        values.put(StockPurchaseTable.Cols.SYMBOL, stockPurchase.Symbol);
        values.put(StockPurchaseTable.Cols.DATE_PURCHASED, stockPurchase.DatePurchased.getTime());
        values.put(StockPurchaseTable.Cols.PRICE, stockPurchase.Price);
        values.put(StockPurchaseTable.Cols.QUANTITY, stockPurchase.Quantity);
        values.put(StockPurchaseTable.Cols.FEE, stockPurchase.Fee);
        values.put(StockPurchaseTable.Cols.TOTAL, stockPurchase.Total);
        return values;
    }

    private ContentValues getMetadataContentValues(Metadata metadata) {
        ContentValues values = new ContentValues();
        values.put(MetadataTable.Cols.YEARLYEXPENSES, metadata.YearlyExpenses);
        values.put(MetadataTable.Cols.SAFEWITHDRAWALRATE, metadata.SafeWithdrawalRate);
        values.put(MetadataTable.Cols.FINANCIALINDEPENDENCENUMBER, metadata.FinancialIndependenceNumber);
        values.put(MetadataTable.Cols.FUNDSWATCHLIST, metadata.FundsWatchlist);
        values.put(MetadataTable.Cols.STOCKEXCHANGEPREFIX, metadata.StockExchangePrefix);
        return values;
    }
}