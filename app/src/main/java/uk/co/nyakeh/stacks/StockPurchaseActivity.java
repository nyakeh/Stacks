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

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import uk.co.nyakeh.stacks.database.StockLab;
import uk.co.nyakeh.stacks.objects.StockPurchase;

public class StockPurchaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView;
    private StockPurchaseAdapter mStockPurchaseAdapter;
    private EditText mSymbolField;
    private EditText mPriceField;
    private EditText mQuantityField;
    private EditText mFeeField;
    private static final String WHOLE_NUMBER_REGEX = "(^[0-9]+)(?!.+)";
    private static final String MONEY_VALUE_REGEX = "(\\d*\\.\\d{1,2}|\\d+)$";

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
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.stockPurchase_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateUI();
    }

    private void addStockPurchase() {
        if (purchaseInputValid()){
            StockPurchase stockPurchase = new StockPurchase(UUID.randomUUID(), mSymbolField.getText().toString(), new Date(), Double.parseDouble(mPriceField.getText().toString()), Integer.parseInt(mQuantityField.getText().toString()), Double.parseDouble(mFeeField.getText().toString()));
            StockLab.get(this).addStockPurchase(stockPurchase);
            updateUI();
            Snackbar.make(findViewById(R.id.app_bar_stock_purchase), "Stock purchase stored", Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean purchaseInputValid() {
        if (mSymbolField.getText().toString().trim().length() == 0 || !Pattern.matches(MONEY_VALUE_REGEX, mPriceField.getText().toString()) || !Pattern.matches(WHOLE_NUMBER_REGEX, mQuantityField.getText().toString()) || !Pattern.matches(MONEY_VALUE_REGEX, mFeeField.getText().toString())){
            Snackbar.make(findViewById(R.id.app_bar_stock_purchase), "Please fill in all fields", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
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

    private class StockPurchaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private StockPurchase mStockPurchase;
        private TextView mSymbolTextView;
        private TextView mDatePurchasedTextView;
        private TextView mPriceTextView;
        private TextView mQuantityTextView;
        private TextView mTotalTextView;

        public StockPurchaseHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mSymbolTextView = (TextView) itemView.findViewById(R.id.list_item_symbol);
            mDatePurchasedTextView = (TextView) itemView.findViewById(R.id.list_item_date);
            mPriceTextView = (TextView) itemView.findViewById(R.id.list_item_price);
            mQuantityTextView = (TextView) itemView.findViewById(R.id.list_item_quantity);
            mTotalTextView = (TextView) itemView.findViewById(R.id.list_item_total);
        }

        private void bindStockPurchase(StockPurchase stockPurchase) {
            mStockPurchase = stockPurchase;
            mSymbolTextView.setText(mStockPurchase.Symbol);
            String datePurchased = DateFormat.format("EEEE, MMM dd, yyyy", mStockPurchase.DatePurchased).toString();
            mDatePurchasedTextView.setText(datePurchased);
            mPriceTextView.setText(String.format("%.2f", mStockPurchase.Price));
            mQuantityTextView.setText(String.valueOf(mStockPurchase.Quantity));
            mTotalTextView.setText(String.format("%.2f", mStockPurchase.Total));
        }

        @Override
        public void onClick(View view) {
            StockLab.get(getParent()).deleteStockPurchase(mStockPurchase.Id);
            updateUI();
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
            View view = layoutInflater.inflate(R.layout.list_item_stock_history, parent, false);
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