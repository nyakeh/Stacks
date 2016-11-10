package uk.co.nyakeh.stacks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.co.nyakeh.stacks.database.StockLab;
import uk.co.nyakeh.stacks.objects.StockPurchase;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IAsyncTask {
    private TextView mDiff;
    private TextView mPercentageChange;
    private TextView mPortfolio;
    private TextView mPercentageFI;
    private TextView mDaysSinceInvestment;
    private TextView mForecast;
    public static final int FI_TARGET = 420000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);
        SetupNavigation(toolbar);

        mDiff = (TextView) findViewById(R.id.dashboard_diff);
        mPercentageChange = (TextView) findViewById(R.id.dashboard_percentageChange);
        mPortfolio = (TextView) findViewById(R.id.dashboard_portfolio);
        mPercentageFI = (TextView) findViewById(R.id.dashboard_percentageFI);
        mDaysSinceInvestment = (TextView) findViewById(R.id.dashboard_daysSinceInvestment);
        mForecast = (TextView) findViewById(R.id.dashboard_forecast);
        new GoogleFinanceClient(this).execute("LON:VMID");
    }

    public void PostExecute(String result) {
        double purchasedStockTotal = 0;
        int purchasedStockQuantity = 0;

        Calendar cal = Calendar.getInstance();
        cal.set(1900, 01, 01);
        Date latestInvestment = cal.getTime();
        List<StockPurchase> stockPurchaseHistory = StockLab.get(getParent()).getStockPurchaseHistory("VMID.L");
        for (StockPurchase stockPurchase : stockPurchaseHistory) {
            purchasedStockTotal += stockPurchase.Total;
            purchasedStockQuantity += stockPurchase.Quantity;
            if (stockPurchase.DatePurchased.after(latestInvestment)) {
                latestInvestment = stockPurchase.DatePurchased;
            }
        }

        try {
            JSONObject share = new JSONObject(result);
            double sharePrice = Double.parseDouble(share.get("l").toString());
            double portfolioSum = sharePrice * purchasedStockQuantity;
            double purchaseSum = purchasedStockTotal;
            double changeInValue = portfolioSum - purchaseSum;
            double percentageChange = (changeInValue / purchaseSum) * 100;
            double percentageFI = (portfolioSum / FI_TARGET) * 100;
            mDiff.setText(getString(R.string.money_format, changeInValue));
            mPercentageChange.setText(getString(R.string.percentage_format, percentageChange));
            mPortfolio.setText(getString(R.string.money_format, portfolioSum));
            mPercentageFI.setText(getString(R.string.percentage_fi, percentageFI));

            long diff = new Date().getTime() - latestInvestment.getTime();
            int daysSinceInvestment = (int) (diff / (24 * 60 * 60 * 1000));
            mDaysSinceInvestment.setText(getString(R.string.days_format, daysSinceInvestment));

            double tenYearForecast = (Math.pow((1.0041), (10 * 12)) * portfolioSum);
            mForecast.setText(getString(R.string.money_format, tenYearForecast));
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
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
        if (id == R.id.nav_main) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_fund) {
            Intent intent = new Intent(this, FundActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_stock) {
            Intent intent = new Intent(this, StockActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_purchaseHistory) {
            Intent intent = new Intent(this, StockPurchaseActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dashboard_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}