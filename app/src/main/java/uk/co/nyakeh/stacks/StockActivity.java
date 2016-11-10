package uk.co.nyakeh.stacks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.nyakeh.stacks.objects.FinanceApi.YahooStockQuery;
import uk.co.nyakeh.stacks.objects.FinanceApi.YahooStockQuote;
import uk.co.nyakeh.stacks.objects.FinanceApi.YahooStockResponse;

public class StockActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String STOCK_PRICE_KEY = "VMID.L|PRICE";
    private RecyclerView mStockRecyclerView;
    private StockAdapter mStockAdapter;
    private YahooStockQuery mYahooStockQuery;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.stock_toolbar);
        setSupportActionBar(toolbar);

        mSharedPreferences = this.getSharedPreferences("uk.co.nyakeh.stacks", Context.MODE_PRIVATE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLatestStockData();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.stock_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.stock_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mStockRecyclerView = (RecyclerView) findViewById(R.id.stock_recycler_view);
        mStockRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getLatestStockData();
    }

    private void getLatestStockData() {
        RestClient.FinanceApiInterface service = RestClient.getClient();
        Call<YahooStockResponse> call = service.getStockHistory();
        call.enqueue(new Callback<YahooStockResponse>() {
            @Override
            public void onResponse(Call<YahooStockResponse> call, Response<YahooStockResponse> response) {
                Log.d("StockActivity", "Status Code = " + response.code());
                if (response.isSuccessful()) {
                    Log.d("StockActivity", "response = " + new Gson().toJson(response.body()));
                    YahooStockResponse result = response.body();
                    mYahooStockQuery = result.query;

                    if(!mYahooStockQuery.results.quote.isEmpty()) {
                        YahooStockQuote latestQuote = mYahooStockQuery.results.quote.get(0);
                        mSharedPreferences.edit().putString(STOCK_PRICE_KEY, latestQuote.Close).apply();
                    }
                    updateUI();
                } else {
                    Log.d("error", response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<YahooStockResponse> call, Throwable t) {
                Log.d("StockActivity", "Status Code = " + t.getMessage());
            }
        });
    }

    private void updateUI() {
        if (mStockAdapter == null) {
            mStockAdapter = new StockAdapter(mYahooStockQuery.results.quote);
            mStockRecyclerView.setAdapter(mStockAdapter);
        } else {
            mStockAdapter.setYahooQuotes(mYahooStockQuery.results.quote);
            mStockAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.stock_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stock, menu);
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_dashboard) {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_fund) {
            Intent intent = new Intent(this, FundActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_purchaseHistory) {
            Intent intent = new Intent(this, StockPurchaseActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.stock_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class StockHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSymbolTextView;
        private TextView mDateTextView;
        private TextView mOpenTextView;

        public StockHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mSymbolTextView = (TextView) itemView.findViewById(R.id.list_item_symbol);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_date);
            mOpenTextView = (TextView) itemView.findViewById(R.id.list_item_total);
        }

        private void bindQuote(YahooStockQuote yahooQuote) {
            mSymbolTextView.setText(yahooQuote.Symbol);
            mDateTextView.setText(yahooQuote.Date);
            mOpenTextView.setText(yahooQuote.Open);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class StockAdapter extends RecyclerView.Adapter<StockHolder> {
        private List<YahooStockQuote> mYahooQuotes;

        public StockAdapter(List<YahooStockQuote> yahooQuotes) {
            mYahooQuotes = yahooQuotes;
        }

        @Override
        public StockHolder onCreateViewHolder(ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(StockActivity.this);
            View view = layoutInflater.inflate(R.layout.list_item_stock, parent, false);
            return new StockHolder(view);
        }

        @Override
        public void onBindViewHolder(StockHolder stockHolder, int position) {
            YahooStockQuote quote = mYahooQuotes.get(position);
            stockHolder.bindQuote(quote);
        }

        @Override
        public int getItemCount() {
            return mYahooQuotes.size();
        }

        public void setYahooQuotes(List<YahooStockQuote> yahooQuotes) {
            mYahooQuotes = yahooQuotes;
        }
    }
}
