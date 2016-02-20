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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import uk.co.nyakeh.stacks.database.StockLab;
import uk.co.nyakeh.stacks.objects.StockPurchase;

public class StockPurchaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView;
    private StockPurchaseAdapter mStockPurchaseAdapter;
    private EditText mSymbolField;
    private EditText mPriceField;
    private EditText mQuantityField;
    private EditText mFeeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_purchase);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSymbolField = (EditText) findViewById(R.id.newStockPurchase_symbol);
        mPriceField = (EditText) findViewById(R.id.newStockPurchase_price);
        mQuantityField = (EditText) findViewById(R.id.newStockPurchase_quantity);
        mFeeField = (EditText) findViewById(R.id.newStockPurchase_fee);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStockPurchase();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.stockPurchase_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateUI();
    }

    private void addStockPurchase() {
        StockPurchase stockPurchase = new StockPurchase(UUID.randomUUID(), mSymbolField.getText().toString(), new Date(), Double.parseDouble(mPriceField.getText().toString()), Integer.parseInt(mQuantityField.getText().toString()), Double.parseDouble(mFeeField.getText().toString()));
        StockLab.get(this).addStockPurchase(stockPurchase);
        updateUI();
    }

    private void updateUI() {
        List<StockPurchase> stockPurchaseHistory = StockLab.get(this).getStockPurchaseHistory("VMID.L");

        if (mStockPurchaseAdapter == null) {
            mStockPurchaseAdapter = new StockPurchaseAdapter(stockPurchaseHistory);
            mRecyclerView.setAdapter(mStockPurchaseAdapter);
        } else {
            mStockPurchaseAdapter.setStockPurchases(stockPurchaseHistory);
            mStockPurchaseAdapter.notifyDataSetChanged();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_main) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_stock) {
            Intent intent = new Intent(this, StockActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class StockPurchaseHolder extends RecyclerView.ViewHolder {

        private TextView mSymbolTextView;
        private TextView mDatePurchasedTextView;
        private TextView mPriceTextView;
        private TextView mQuantityTextView;
        private TextView mFeeTextView;
        private TextView mTotalTextView;

        public StockPurchaseHolder(View itemView) {
            super(itemView);
            mSymbolTextView = (TextView) itemView.findViewById(R.id.list_item_stock_symbol);
            mDatePurchasedTextView = (TextView) itemView.findViewById(R.id.list_item_stock_date);
            mTotalTextView = (TextView) itemView.findViewById(R.id.list_item_stock_open);
        }

        private void bindStockPurchase(StockPurchase stockPurchase) {
            mSymbolTextView.setText(stockPurchase.Symbol);
            String datePurchased = DateFormat.format("EEEE, MMM dd, yyyy", stockPurchase.DatePurchased).toString();
            mDatePurchasedTextView.setText(datePurchased);
//            mPriceTextView.setText(String.format("%.2f", stockPurchase.Price));
//            mQuantityTextView.setText(stockPurchase.Quantity);
//            mFeeTextView.setText(String.format("%.2f", stockPurchase.Fee));
            mTotalTextView.setText(String.format("%.2f", stockPurchase.Total));
        }
    }

    private class StockPurchaseAdapter extends RecyclerView.Adapter<StockPurchaseHolder> {
        private List<StockPurchase> mStockPurchases;

        public StockPurchaseAdapter(List<StockPurchase> stockPurchases) {
            mStockPurchases = stockPurchases;
        }

        @Override
        public StockPurchaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(StockPurchaseActivity.this);
            View view = layoutInflater.inflate(R.layout.list_item_stock, parent, false);
            return new StockPurchaseHolder(view);
        }

        @Override
        public void onBindViewHolder(StockPurchaseHolder stockPurchaseHolder, int position) {
            StockPurchase stockPurchase = mStockPurchases.get(position);
            stockPurchaseHolder.bindStockPurchase(stockPurchase);
        }

        @Override
        public int getItemCount() {
            return mStockPurchases.size();
        }

        public void setStockPurchases(List<StockPurchase> stockPurchases) {
            mStockPurchases = stockPurchases;
        }
    }
}