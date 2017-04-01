package uk.co.nyakeh.stacks.database;

public class DividendDbSchema {
    public static final class DividendTable {
        public static final String NAME = "Dividend";

        public static final class Cols {
            public static final String ID = "id";
            public static final String DATE = "date";
            public static final String AMOUNT= "amount";
        }
    }
}