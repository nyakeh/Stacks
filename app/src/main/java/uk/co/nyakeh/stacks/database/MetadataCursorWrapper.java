package uk.co.nyakeh.stacks.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import uk.co.nyakeh.stacks.database.MetadataDbSchema.MetadataTable;
import uk.co.nyakeh.stacks.records.Metadata;

public class MetadataCursorWrapper extends CursorWrapper {
    public MetadataCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Metadata getMetadata() {
        double yearlyExpenses = getDouble(getColumnIndex(MetadataTable.Cols.YEARLYEXPENSES));
        double safeWithdrawalRate = getDouble(getColumnIndex(MetadataTable.Cols.SAFEWITHDRAWALRATE));
        double financialIndependenceNumber = getDouble(getColumnIndex(MetadataTable.Cols.FINANCIALINDEPENDENCENUMBER));
        String fundsWatchlist = getString(getColumnIndex(MetadataTable.Cols.FUNDSWATCHLIST));
        String stockExchangePrefix = getString(getColumnIndex(MetadataTable.Cols.STOCKEXCHANGEPREFIX));

        Metadata stockPurchase = new Metadata(yearlyExpenses, safeWithdrawalRate, financialIndependenceNumber, fundsWatchlist, stockExchangePrefix);
        return stockPurchase;
    }
}