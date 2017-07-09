package uk.co.nyakeh.stacks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.nyakeh.stacks.database.StockLab;
import uk.co.nyakeh.stacks.objects.Dividend;
import uk.co.nyakeh.stacks.objects.Metadata;
import uk.co.nyakeh.stacks.objects.StockPurchase;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IAsyncTask {
    private TextView mDiff;
    private TextView mPercentageChange;
    private TextView mDividendYield;
    private TextView mDividendPercentageYield;
    private TextView mPortfolio;
    private TextView mPercentageFI;
    private TextView mDaysSinceInvestment;
    private List<StockPurchase> _stockPurchaseHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);
        SetupNavigation(toolbar);

        mDiff = (TextView) findViewById(R.id.dashboard_diff);
        mPercentageChange = (TextView) findViewById(R.id.dashboard_percentageChange);
        mDividendYield = (TextView) findViewById(R.id.dashboard_dividendYield);
        mDividendPercentageYield = (TextView) findViewById(R.id.dashboard_dividendPercentageYield);
        mPortfolio = (TextView) findViewById(R.id.dashboard_portfolio);
        mPercentageFI = (TextView) findViewById(R.id.dashboard_percentageFI);
        mDaysSinceInvestment = (TextView) findViewById(R.id.dashboard_daysSinceInvestment);


        _stockPurchaseHistory = StockLab.get(this).getStockPurchaseHistory();
        List purchasedStocks = new ArrayList();
        for (StockPurchase stockPurchase : _stockPurchaseHistory) {
            if (!purchasedStocks.contains(stockPurchase.Symbol))
            {
                purchasedStocks.add(stockPurchase.Symbol);
            }
        }
        String asd = TextUtils.join(",", purchasedStocks);
        new GoogleFinanceClient(this, this).execute(asd);
    }

    public void PostExecute(String response) {
        Map<String, Double> sharePrices = new HashMap<>();
        try {
            response = response.replace("//","");
            JSONArray arr = new JSONArray(response);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject share = arr.optJSONObject(i);
                double sharePrice = Double.parseDouble(share.get("l").toString());
                String shareName = share.getString("t").toString();
                sharePrices.put(shareName, sharePrice);
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        double purchaseSum = 0;
        double portfolioSum = 0;
        Calendar cal = Calendar.getInstance();
        cal.set(1900, 01, 01);
        Date latestInvestment = cal.getTime();
        for (StockPurchase stockPurchase : _stockPurchaseHistory) {
            purchaseSum += stockPurchase.Total;
            if (sharePrices.containsKey(stockPurchase.Symbol)) {
                portfolioSum += sharePrices.get(stockPurchase.Symbol) * stockPurchase.Quantity;
            }else {
                Snackbar.make(findViewById(R.id.app_bar_dashboard), getString(R.string.cantFindStock_format, stockPurchase.Symbol), Snackbar.LENGTH_LONG).show();
            }
            if (stockPurchase.DatePurchased.after(latestInvestment)) {
                latestInvestment = stockPurchase.DatePurchased;
            }
        }

        Metadata metadata = StockLab.get(this).getMetadata();
        double dividendSum = 0;
        List<Dividend> dividendHistory = StockLab.get(this).getDividendHistory();
        for (Dividend dividend : dividendHistory) {
            dividendSum += dividend.Amount;
        }

        double changeInValue = portfolioSum - purchaseSum;
        double percentageChange = (changeInValue / purchaseSum) * 100;
        double percentageFI = (portfolioSum / metadata.FinancialIndependenceNumber) * 100;
        double dividendPercentageYield = (dividendSum / portfolioSum) * 100;
        mDiff.setText(getString(R.string.money_format, changeInValue));
        mPercentageChange.setText(getString(R.string.percentage_format, percentageChange));
        mDividendYield.setText(getString(R.string.money_format, dividendSum));
        mDividendPercentageYield.setText(getString(R.string.percentage_format, dividendPercentageYield));
        mPortfolio.setText(getString(R.string.money_format, portfolioSum));
        mPercentageFI.setText(getString(R.string.percentage_fi_format, percentageFI));
        long diff = new Date().getTime() - latestInvestment.getTime();
        int daysSinceInvestment = (int) (diff / (24 * 60 * 60 * 1000));
        mDaysSinceInvestment.setText(getString(R.string.days_format, daysSinceInvestment));
    }

    private void SetupNavigation(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dashboard_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.dashboard_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_fund) {
            Intent intent = new Intent(this, FundActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_purchaseHistory) {
            Intent intent = new Intent(this, StockPurchaseActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_dividend) {
            Intent intent = new Intent(this, DividendActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_controlPanel) {
            Intent intent = new Intent(this, ControlPanelActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dashboard_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}