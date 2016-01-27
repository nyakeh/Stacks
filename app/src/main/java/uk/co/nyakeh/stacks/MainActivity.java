package uk.co.nyakeh.stacks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final OkHttpClient client = new OkHttpClient();
    String mApiCallResult = "";
    String name = "";
    String symbol = "";
    String stockExchange = "";
    String change = "";
    String changeinPercent = "";
    String open = "";
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String response = run("http://query.yahooapis.com/v1/public/yql?q=select%20%2a%20from%20yahoo.finance.quotes%20where%20symbol%20in%20%28%22VMID.L%22%29%0A%09%09&env=http%3A%2F%2Fdatatables.org%2Falltables.env&format=json");

                    mNameTextView.setText(name);
                    mSymbolTextView.setText(symbol);
                    mStockExchangeTextView.setText(stockExchange);
                    mChangeTextView.setText(change);
                    mChangeinPercentTextView.setText(changeinPercent);
                    mOpenTextView.setText(open);
                    Snackbar.make(view, response, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mNameTextView = (TextView) findViewById(R.id.stockName);
        mSymbolTextView = (TextView) findViewById(R.id.stockSymbol);
        mStockExchangeTextView = (TextView) findViewById(R.id.stockExchange);
        mChangeTextView = (TextView) findViewById(R.id.stockChange);
        mChangeinPercentTextView = (TextView) findViewById(R.id.stockChangeInPercent);
        mOpenTextView = (TextView) findViewById(R.id.stockOpen);
    }

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mApiCallResult = e.getMessage();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                String responseBody = response.body().string();
                System.out.println(responseBody);
                mApiCallResult = responseBody;
                try {
                    JSONObject responseJson = (JSONObject) new JSONTokener(responseBody).nextValue();
                    JSONObject queryJson = responseJson.getJSONObject("query");
                    JSONObject resultsJson = queryJson.getJSONObject("results");
                    JSONObject quoteJson = resultsJson.getJSONObject("quote");
                    name = quoteJson.getString("Name");
                    symbol = quoteJson.getString("Symbol");
                    stockExchange = quoteJson.getString("StockExchange");
                    change = quoteJson.getString("Change");
                    changeinPercent = quoteJson.getString("ChangeinPercent");
                    open = quoteJson.getString("Open");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //mApiCallResult = response.body().string();
            }
        });
        return mApiCallResult;
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
