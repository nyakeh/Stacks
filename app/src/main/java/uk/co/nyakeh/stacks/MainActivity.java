package uk.co.nyakeh.stacks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import retrofit2.Call;
import uk.co.nyakeh.stacks.objects.FinanceApi.YahooOverviewQuote;
import uk.co.nyakeh.stacks.objects.FinanceApi.YahooOverviewResponse;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String STOCK_PRICE_KEY = "VMID.L|PRICE";
    private YahooOverviewQuote mYahooOverviewQuote;
    private SharedPreferences mSharedPreferences;
    TextView mNameTextView;
    TextView mStockPriceChangeTextView;
    TextView mSymbolTextView;
    TextView mChangeTextView;
    TextView mChangeInPercentTextView;
    TextView mOpenTextView;
    TextView mStockValuation;
    TextView mStockDailyValuationChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSharedPreferences = this.getSharedPreferences("uk.co.nyakeh.stacks", Context.MODE_PRIVATE);

        mNameTextView = (TextView) findViewById(R.id.stockName);
        mStockPriceChangeTextView = (TextView) findViewById(R.id.stockPriceChange);
        mSymbolTextView = (TextView) findViewById(R.id.stockSymbol);
        mChangeTextView = (TextView) findViewById(R.id.stockChange);
        mChangeInPercentTextView = (TextView) findViewById(R.id.stockChangeInPercent);
        mOpenTextView = (TextView) findViewById(R.id.stockOpen);
        mStockValuation = (TextView) findViewById(R.id.stockValuation);
        mStockDailyValuationChange = (TextView) findViewById(R.id.stockDailyValuationChange);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLatestStockData();
            }
        });

        getLatestStockData();
    }

    private void getLatestStockData() {
        RestClient.FinanceApiInterface service = RestClient.getClient();
        Call<YahooOverviewResponse> call = service.getStockOverview();
        call.enqueue(new retrofit2.Callback<YahooOverviewResponse>() {
            @Override
            public void onResponse(retrofit2.Response<YahooOverviewResponse> response) {
                if (response.isSuccess()) {
                    Log.d("MainActivity", "response = " + new Gson().toJson(response.body()));
                    YahooOverviewResponse result = response.body();
                    mYahooOverviewQuote = result.query.results.quote;
                    if (mYahooOverviewQuote.Open != null && !mYahooOverviewQuote.Open.isEmpty()) {
                        mSharedPreferences.edit().putString(STOCK_PRICE_KEY, mYahooOverviewQuote.Open).apply();
                    }
                    updateUI();
                } else {
                    Log.d("Api Error", response.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("MainActivity", "Status Code = " + t.getMessage());
            }
        });
    }

    private void updateUI() {
        mNameTextView.setText(mYahooOverviewQuote.Name);
        mSymbolTextView.setText(mYahooOverviewQuote.Symbol);
        mChangeTextView.setText(mYahooOverviewQuote.Change);
        mChangeInPercentTextView.setText(mYahooOverviewQuote.ChangeinPercent);

        String cachedStockOpen = mSharedPreferences.getString(STOCK_PRICE_KEY, "");
        if (mYahooOverviewQuote.Open == null && mYahooOverviewQuote.Open.isEmpty() && (!cachedStockOpen.isEmpty())) {
            mYahooOverviewQuote.Open = cachedStockOpen;
        }

        double myStockPurchaseValue = 35 * 28.126;
        double currentStockValue = 35 * Double.valueOf(mYahooOverviewQuote.Open);
        double stockPriceChange = currentStockValue - myStockPurchaseValue;

        mOpenTextView.setText("£" + mYahooOverviewQuote.Open);
        mStockPriceChangeTextView.setText(String.format("%.2f GBP", stockPriceChange));
        mStockValuation.setText(String.format("£%.2f", currentStockValue));

        double dayValuationChange = currentStockValue * (Double.valueOf(mYahooOverviewQuote.Change) / 100);
        mStockDailyValuationChange.setText(String.format("%.2f", dayValuationChange));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_stock) {
            Intent intent = new Intent(this, StockActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_purchaseHistory) {
            Intent intent = new Intent(this, StockPurchaseActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
