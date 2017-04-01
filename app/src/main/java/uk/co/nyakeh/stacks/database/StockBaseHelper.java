package uk.co.nyakeh.stacks.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import uk.co.nyakeh.stacks.database.StockDbSchema.StockPurchaseTable;
import uk.co.nyakeh.stacks.database.MetadataDbSchema.MetadataTable;
import uk.co.nyakeh.stacks.database.DividendDbSchema.DividendTable;

public class StockBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 3;
    public static final String DATABASE_NAME = "stockBase.db";

    public StockBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + StockPurchaseTable.NAME + " (" + StockPurchaseTable.Cols.ID + ", " + StockPurchaseTable.Cols.SYMBOL + ", " + StockPurchaseTable.Cols.DATE_PURCHASED + ", " + StockPurchaseTable.Cols.PRICE + ", " + StockPurchaseTable.Cols.QUANTITY + ", " + StockPurchaseTable.Cols.FEE + ", " + StockPurchaseTable.Cols.TOTAL + ")");
        db.execSQL("create table " + MetadataTable.NAME + " (" + MetadataTable.Cols.ID + ", " + MetadataTable.Cols.YEARLYEXPENSES  + ", " + MetadataTable.Cols.SAFEWITHDRAWALRATE  + ", " + MetadataTable.Cols.FINANCIALINDEPENDENCENUMBER + ", " + MetadataTable.Cols.FUNDSWATCHLIST + ", " + MetadataTable.Cols.STOCKEXCHANGEPREFIX + ")");
        db.execSQL("insert into " + MetadataTable.NAME + " values (1,25000,4,625000,'VMID,VWRL,VUKE','LON:')");
        db.execSQL("create table " + DividendTable.NAME + " (" + DividendTable.Cols.ID + ", " + DividendTable.Cols.DATE  + ", " + DividendTable.Cols.AMOUNT + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("create table " + MetadataTable.NAME + " (" + MetadataTable.Cols.ID + ", " + MetadataTable.Cols.YEARLYEXPENSES  + ", " + MetadataTable.Cols.SAFEWITHDRAWALRATE  + ", " + MetadataTable.Cols.FINANCIALINDEPENDENCENUMBER + ", " + MetadataTable.Cols.FUNDSWATCHLIST + ", " + MetadataTable.Cols.STOCKEXCHANGEPREFIX + ")");
            db.execSQL("insert into " + MetadataTable.NAME + " values (1,25000,4,625000,'VMID,VWRL,VUKE','LON:')");
        }
        if (oldVersion < 3) {
            db.execSQL("create table " + DividendTable.NAME + " (" + DividendTable.Cols.ID + ", " + DividendTable.Cols.DATE  + ", " + DividendTable.Cols.AMOUNT + ")");
        }
    }
}