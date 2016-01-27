package uk.co.nyakeh.stacks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StockActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private RecyclerView mStockRecyclerView;
    private StockAdapter mStockAdapter;
    private YahooResult mYahooResult = new YahooResult();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    run();
                    updateUI();
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mStockRecyclerView = (RecyclerView) findViewById(R.id.stock_recycler_view);
        mStockRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateUI() {
        if (mStockAdapter == null) {
            mStockAdapter = new StockAdapter(mYahooResult.Quotes);
            mStockRecyclerView.setAdapter(mStockAdapter);
        } else {
            mStockAdapter.setYahooQuotes(mYahooResult.Quotes);
            mStockAdapter.notifyDataSetChanged();
        }
    }

    void run() throws IOException {
        Request request = new Request.Builder()
                .url("http://query.yahooapis.com/v1/public/yql?env=http%3A%2F%2Fdatatables.org%2Falltables.env&format=json&q=select%20*%20from%20yahoo.finance.historicaldata%20where%20startDate=%272016-01-01%27%20and%20endDate=%272017-01-10%27%20and%20symbol=%27vmid.l%27")
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                String responseBody = response.body().string();
                System.out.println(responseBody);

                try {
                    JSONObject responseJson = (JSONObject) new JSONTokener(responseBody).nextValue();
                    JSONObject queryJson = responseJson.getJSONObject("query");
                    JSONObject resultsJson = queryJson.getJSONObject("results");
                    JSONArray quoteJsonArray = resultsJson.getJSONArray("quote");
                    for (int i = 0; i < quoteJsonArray.length(); i++) {
                        JSONObject dayQuoteJSONObject = quoteJsonArray.getJSONObject(i);
                        System.out.println("before");

                        YahooQuote currentQuote = new YahooQuote(dayQuoteJSONObject.getString("Symbol"),dayQuoteJSONObject.getString("Date"),dayQuoteJSONObject.getString("Open"),dayQuoteJSONObject.getString("High"),dayQuoteJSONObject.getString("Low"),dayQuoteJSONObject.getString("Close"),dayQuoteJSONObject.getString("Volume"),dayQuoteJSONObject.getString("Adj_Close"));
                        System.out.println("after");
                        mYahooResult.Quotes.add(currentQuote);
                        System.out.println(currentQuote.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_main) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_stock) {

        } else if (id == R.id.nav_graph) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class StockHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private YahooQuote mYahooQuote;
        private TextView mTitleTextView;
        private TextView mAuthorTextView;
        private TextView mProgressTextView;

        public StockHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_book_title);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.list_item_book_author);
            mProgressTextView = (TextView) itemView.findViewById(R.id.list_item_book_progress);
        }

        private void bindBook(YahooQuote yahooQuote) {
            mYahooQuote = yahooQuote;
            mTitleTextView.setText(yahooQuote.Symbol);
            mAuthorTextView.setText(yahooQuote.Date);
            mProgressTextView.setText(yahooQuote.Open);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class StockAdapter extends RecyclerView.Adapter<StockHolder> {
        private List<YahooQuote> mYahooQuotes;

        public StockAdapter(List<YahooQuote> yahooQuotes) {
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
            YahooQuote book = mYahooQuotes.get(position);
            stockHolder.bindBook(book);
        }

        @Override
        public int getItemCount() {
            return mYahooQuotes.size();
        }

        public void setYahooQuotes(List<YahooQuote> yahooQuotes) {
            mYahooQuotes = yahooQuotes;
        }
    }
}
