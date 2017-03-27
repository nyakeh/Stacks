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
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import uk.co.nyakeh.stacks.database.StockLab;
import uk.co.nyakeh.stacks.records.Dividend;

public class DividendActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DateDialogCallbackInterface  {
    private RecyclerView _recyclerView;
    private DividendAdapter _dividendAdapter;
    private EditText _dividendAmount;
    private Button _dividendDateButton;
    private static final String MONEY_VALUE_REGEX = "^(\\d*\\.\\d{1,2}|\\d+)$";
    private Date _dividendDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dividend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.dividend_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dividend_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.dividend_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        _dividendAmount = (EditText) findViewById(R.id.dividend_amount);
        _dividendDateButton = (Button) findViewById(R.id.dividend_date);

        _dividendDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy", _dividendDate).toString());
        _dividendDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(_dividendDate);
                dialog.show(manager, "DialogDate");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDividend();
            }
        });

        _recyclerView = (RecyclerView) findViewById(R.id.dividend_recycler_view);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        updateUI();
    }

    private void addDividend() {
        if (dividendInputValid()){
            Dividend dividend = new Dividend(UUID.randomUUID(), _dividendDate, Double.parseDouble(_dividendAmount.getText().toString()));
            StockLab.get(this).addDividend(dividend);
            updateUI();
            Snackbar.make(findViewById(R.id.app_bar_dividend), "Dividend stored", Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean dividendInputValid() {
        if (!Pattern.matches(MONEY_VALUE_REGEX, _dividendAmount.getText().toString())){
            Snackbar.make(findViewById(R.id.app_bar_dividend), "Please fill in the dividend amount", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void updateUI() {
        List<Dividend> dividendHistory = StockLab.get(this).getDividendHistory();

        if (_dividendAdapter == null) {
            _dividendAdapter = new DividendAdapter(dividendHistory);
            _recyclerView.setAdapter(_dividendAdapter);
            ItemTouchHelper.Callback callback = new SwipeCallback(_dividendAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(_recyclerView);
        } else {
            _dividendAdapter.setDividends(dividendHistory);
            _dividendAdapter.notifyDataSetChanged();
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
        } else if (id == R.id.nav_controlPanel) {
            Intent intent = new Intent(this, ControlPanelActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_purchaseHistory) {
            Intent intent = new Intent(this, StockPurchaseActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dividend_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDateSelectedCallBack(Date date) {
        _dividendDate = date;
        _dividendDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy", date).toString());
    }

    private class DividendHolder extends RecyclerView.ViewHolder  {
        private TextView _dateTextView;
        private TextView _amountTextView;

        public DividendHolder(View itemView) {
            super(itemView);
            _dateTextView = (TextView) itemView.findViewById(R.id.list_item_date);
            _amountTextView = (TextView) itemView.findViewById(R.id.list_item_amount);
        }

        private void bindDividend(Dividend dividend) {
            String datePurchased = DateFormat.format("EEEE, MMM dd, yyyy", dividend.Date).toString();
            _dateTextView.setText(datePurchased);
            _amountTextView.setText(getString(R.string.money_format, dividend.Amount));
        }
    }

    private class DividendAdapter extends RecyclerView.Adapter<DividendHolder> implements ISwipeAdapter {
        private List<Dividend> _dividends;

        public DividendAdapter(List<Dividend> dividends) {
            _dividends = dividends;
        }

        @Override
        public DividendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(DividendActivity.this);
            View view = layoutInflater.inflate(R.layout.list_item_dividend, parent, false);
            return new DividendHolder(view);
        }

        @Override
        public void onBindViewHolder(DividendHolder dividendHolder, int position) {
            Dividend dividend = _dividends.get(position);
            dividendHolder.bindDividend(dividend);
        }

        @Override
        public int getItemCount() {
            return _dividends.size();
        }

        public void setDividends(List<Dividend> dividends) {
            _dividends = dividends;
        }

        @Override
        public void Delete(int position) {
            final Dividend dividend = _dividends.remove(position);
            StockLab.get(getParent()).deleteDividend(dividend.Id);
            notifyItemRemoved(position);
            Snackbar.make(findViewById(R.id.app_bar_dividend), "Dividend deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StockLab.get(getParent()).addDividend(dividend);
                            Add(dividend);
                        }
                    })
                    .show();
        }

        private void Add(Dividend dividend){
            _dividends.add(dividend);
            notifyItemInserted(_dividends.size());
        }
    }
}