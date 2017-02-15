package uk.co.nyakeh.stacks.records;

public class Metadata {
    public double YearlyExpenses;
    public double SafeWithdrawalRate;
    public double FinancialIndependenceNumber;
    public String FundsWatchlist;
    public String StockExchangePrefix;

    public Metadata(double yearlyExpenses, double safeWithdrawalRate, String fundsWatchlist, String stockExchangePrefix) {
        YearlyExpenses = yearlyExpenses;
        SafeWithdrawalRate = safeWithdrawalRate;
        FinancialIndependenceNumber = yearlyExpenses * (100/safeWithdrawalRate);
        FundsWatchlist = fundsWatchlist;
        StockExchangePrefix = stockExchangePrefix;
    }

    public Metadata(double yearlyExpenses, double safeWithdrawalRate, double financialIndependenceNumber, String fundsWatchlist, String stockExchangePrefix) {
        YearlyExpenses = yearlyExpenses;
        SafeWithdrawalRate = safeWithdrawalRate;
        FinancialIndependenceNumber = financialIndependenceNumber;
        FundsWatchlist = fundsWatchlist;
        StockExchangePrefix = stockExchangePrefix;
    }
}