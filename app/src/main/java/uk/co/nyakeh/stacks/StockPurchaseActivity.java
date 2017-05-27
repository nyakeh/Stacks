package uk.co.nyakeh.stacks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
`import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import uk.co.nyakeh.stacks.database.StockLab;
import uk.co.nyakeh.stacks.objects.StockPurchase;

public class StockPurchaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DateDialogCallbackInterface  {
    private RecyclerView _recyclerView;
    private StockPurchaseAdapter _stockPurchaseAdapter;
    private TextView _investmentSum;
    private EditText _symbolField;
    private EditText _priceField;
    private EditText _quantityField;
    private EditText _feeField;
    private Button _stockPurchaseDateButton;
    private Date _stockPurchaseDate = new Date();
    private static final String MONEY_VALUE_REGEX = "^(\\d*\\.\\d{1,2}|\\d+)$";
    private static final String SIX_DECIMAL_PLACES_REGEX = "^(\\d*\\.\\d{1,6}|\\d+)$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_purchase);
        Toolbar toolbar = (Toolbar) findViewById(R.id.stock_purchase_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.stock_purchase_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.stock_purchase_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        _investmentSum = (TextView) findViewById(R.id.newStockPurchase_investmentSum);
        _symbolField = (EditText) findViewById(R.id.newStockPurchase_symbol);
        _priceField = (EditText) findViewById(R.id.newStockPurchase_price);
        _quantityField = (EditText) findViewById(R.id.newStockPurchase_quantity);
        _feeField = (EditText) findViewById(R.id.newStockPurchase_fee);
        _stockPurchaseDateButton = (Button) findViewById(R.id.stock_purchase_date);

        _stockPurchaseDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy", _stockPurchaseDate).toString());
        _stockPurchaseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(_stockPurchaseDate);
                dialog.show(manager, "DialogDate");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStockPurchase();
            }
        });

        _recyclerView = (RecyclerView) findViewById(R.id.stockPurchase_recycler_view);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        updateUI();
    }

    private void addStockPurchase() {
        if (purchaseInputValid()){
            StockPurchase stockPurchase = new StockPurchase(UUID.randomUUID(), _symbolField.getText().toString(), _stockPurchaseDate, Double.parseDouble(_priceField.getText().toString()), Double.parseDouble(_quantityField.getText().toString()), Double.parseDouble(_feeField.getText().toString()));
            StockLab.get(this).addStockPurchase(stockPurchase);
            updateUI();
        }
    }

    private boolean purchaseInputValid() {
        if (_symbolField.getText().toString().trim().length() == 0 || !Pattern.matches(SIX_DECIMAL_PLACES_REGEX, _priceField.getText().toString()) || !Pattern.matches(SIX_DECIMAL_PLACES_REGEX, _quantityField.getText().toString()) || !Pattern.matches(MONEY_VALUE_REGEX, _feeField.getText().toString())){
            Snackbar.make(findViewById(R.id.app_bar_stockPurchase), "Please fill in all fields", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void updateUI() {
        List<StockPurchase> stockPurchaseHistory = StockLab.get(this).getStockPurchaseHistory();
        double investmentSum = 0;
        for(Iterator<StockPurchase> i = stockPurchaseHistory.iterator(); i.hasNext(); ) {
            StockPurchase item = i.next();
            investmentSum += item.Total;
        }
        _investmentSum.setText(getString(R.string.investmentSum_format, investmentSum));
        if (_stockPurchaseAdapter == null) {
            _stockPurchaseAdapter = new StockPurchaseAdapter(stockPurchaseHistory);
            _recyclerView.setAdapter(_stockPurchaseAdapter);
            ItemTouchHelper.Callback callback = new SwipeCallback(_stockPurchaseAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(_recyclerView);
        } else {
            _stockPurchaseAdapter.setStockPurchases(stockPurchaseHistory);
            _stockPurchaseAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_dashboard) {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_fund) {
            Intent intent = new Intent(this, FundActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_dividend) {
            Intent intent = new Intent(this, DividendActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_controlPanel) {
            Intent intent = new Intent(this, ControlPanelActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.stock_purchase_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDateSelectedCallBack(Date date) {
        _stockPurchaseDate = date;
        _stockPurchaseDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy", date).toString());
    }

    private class StockPurchaseHolder extends RecyclerView.ViewHolder  {
        private TextView mSymbolTextView;
        private TextView mDatePurchasedTextView;
        private TextView mPriceTextView;
        private TextView mQuantityTextView;
        private TextView mTotalTextView;

        public StockPurchaseHolder(View itemView) {
            super(itemView);
            mSymbolTextView = (TextView) itemView.findViewById(R.id.list_item_symbol);
            mDatePurchasedTextView = (TextView) itemView.findViewById(R.id.list_item_date);
            mPriceTextView = (TextView) itemView.findViewById(R.id.list_item_amount);
            mQuantityTextView = (TextView) itemView.findViewById(R.id.list_item_quantity);
            mTotalTextView = (TextView) itemView.findViewById(R.id.list_item_total);
        }

        private void bindStockPurchase(StockPurchase stockPurchase) {
            mSymbolTextView.setText(stockPurchase.Symbol);
            String datePurchased = DateFormat.format("EEEE, MMM dd, yyyy", stockPurchase.DatePurchased).toString();
            mDatePurchasedTextView.setText(datePurchased);
            mPriceTextView.setText(getString(R.string.threeDecimalPlaces_format, stockPurchase.Price));
            mQuantityTextView.setText(String.valueOf(stockPurchase.Quantity));
            mTotalTextView.setText(getString(R.string.money_format, stockPurchase.Total));
        }
    }

    private class StockPurchaseAdapter extends RecyclerView.Adapter<StockPurchaseHolder> implements ISwipeAdapter {
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

        @Override
        public void Delete(int position) {
            final StockPurchase stock = mStockPurchases.remove(position);
            StockLab.get(getParent()).deleteStockPurchase(stock.Id);
            notifyItemRemoved(position);
            Snackbar.make(findViewById(R.id.app_bar_stockPurchase), "Purchase deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StockLab.get(getParent()).addStockPurchase(stock);
                            Add(stock);
                            updateUI();
                        }
                    }).show();
            updateUI();
        }

        private void Add(StockPurchase stockPurchase){
            mStockPurchases.add(stockPurchase);
            notifyItemInserted(mStockPurchases.size());
        }
    }
}