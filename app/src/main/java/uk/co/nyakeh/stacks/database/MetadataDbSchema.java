package uk.co.nyakeh.stacks.database;

public class MetadataDbSchema {
    public static final class MetadataTable {
        public static final String NAME = "Metadata";

        public static final class Cols {
            public static final String ID = "id";
            public static final String YEARLYEXPENSES = "yearlyExpenses";
            public static final String SAFEWITHDRAWALRATE = "safeWithdrawalRate";
            public static final String FINANCIALINDEPENDENCENUMBER = "financialIndependenceNumber";
            public static final String FUNDSWATCHLIST = "fundsWatchlist";
            public static final String STOCKEXCHANGEPREFIX = "stockExchangePrefix";
        }
    }
}