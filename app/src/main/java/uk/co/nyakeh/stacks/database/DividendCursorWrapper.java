package uk.co.nyakeh.stacks.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import uk.co.nyakeh.stacks.database.DividendDbSchema.DividendTable;
import uk.co.nyakeh.stacks.objects.Dividend;

public class DividendCursorWrapper extends CursorWrapper {
    public DividendCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Dividend getDividend() {
        String id = getString(getColumnIndex(DividendTable.Cols.ID));
        Long date = getLong(getColumnIndex(DividendTable.Cols.DATE));
        double amount = getDouble(getColumnIndex(DividendTable.Cols.AMOUNT));

        Dividend dividend = new Dividend(UUID.fromString(id), new Date(date), amount);
        return dividend;
    }
}