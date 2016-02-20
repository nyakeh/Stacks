package uk.co.nyakeh.stacks.database;

public class StockDbSchema {
    public static final class StockPurchaseTable {
        public static final String NAME = "stockPurchase";

        public static final class Cols {
            public static final String ID = "id";
            public static final String SYMBOL = "symbol";
            public static final String DATE_PURCHASED = "datePurchased";
            public static final String PRICE = "price";
            public static final String COUNT = "count";
            public static final String FEE = "fee";
            public static final String TOTAL = "total";
        }
    }
}