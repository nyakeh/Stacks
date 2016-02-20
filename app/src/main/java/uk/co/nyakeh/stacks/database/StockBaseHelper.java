package uk.co.nyakeh.stacks.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import uk.co.nyakeh.stacks.database.StockDbSchema.StockPurchaseTable;

public class StockBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "stockBase.db";

    public StockBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + StockPurchaseTable.NAME + "(" + StockPurchaseTable.Cols.ID + ", " + StockPurchaseTable.Cols.SYMBOL + ", " + StockPurchaseTable.Cols.DATE_PURCHASED + ", " + StockPurchaseTable.Cols.PRICE + ", " + StockPurchaseTable.Cols.COUNT + ", " + StockPurchaseTable.Cols.FEE + ", " + StockPurchaseTable.Cols.TOTAL + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}