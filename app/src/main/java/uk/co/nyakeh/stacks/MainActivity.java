package uk.co.nyakeh.stacks;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private YahooOverviewQuote mYahooOverviewQuote;
    TextView mNameTextView;
    TextView mSymbolTextView;
    TextView mStockExchangeTextView;
    TextView mChangeTextView;
    TextView mChangeinPercentTextView;
    TextView mOpenTextView;

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

        mNameTextView = (TextView) findViewById(R.id.stockName);
        mSymbolTextView = (TextView) findViewById(R.id.stockSymbol);
        mStockExchangeTextView = (TextView) findViewById(R.id.stockExchange);
        mChangeTextView = (TextView) findViewById(R.id.stockChange);
        mChangeinPercentTextView = (TextView) findViewById(R.id.stockChangeInPercent);
        mOpenTextView = (TextView) findViewById(R.id.stockOpen);

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
        mStockExchangeTextView.setText(mYahooOverviewQuote.StockExchange);
        mChangeTextView.setText(mYahooOverviewQuote.Change);
        mChangeinPercentTextView.setText(mYahooOverviewQuote.ChangeinPercent);
        mOpenTextView.setText(mYahooOverviewQuote.Open);
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
        if (id == R.id.nav_main) {

        } else if (id == R.id.nav_stock) {
            Intent intent = new Intent(this, StockActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_graph) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
