package uk.co.nyakeh.stacks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.nyakeh.stacks.database.StockLab;
import uk.co.nyakeh.stacks.records.Metadata;

public class FundActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IAsyncTask {
    private RecyclerView _recyclerView;
    private FundAdapter _fundAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund);
        Toolbar toolbar = (Toolbar) findViewById(R.id.fund_toolbar);
        setSupportActionBar(toolbar);
        SetupNavigation(toolbar);

        _recyclerView = (RecyclerView) findViewById(R.id.fundWatchlist_recycler_view);
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
        _fundAdapter = new FundAdapter();
        _recyclerView.setAdapter(_fundAdapter);

        Metadata metadata = StockLab.get(this).getMetadata();
        String[] fundsWatchlist = metadata.FundsWatchlist.split(",");
        for (String fund : fundsWatchlist) {
            new GoogleFinanceClient(this, this).execute(metadata.StockExchangePrefix + fund);
        }
    }

    public void PostExecute(String response) {
        try {
            JSONObject share = new JSONObject(response);
            String symbol = share.get("t").toString();
            double price = Double.parseDouble(share.get("l").toString());
            String change = share.get("cp").toString();
            Fund fund = new Fund(symbol, price, change);
            _fundAdapter.Add(fund);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    private class FundHolder extends RecyclerView.ViewHolder {
        private TextView _symbol;
        private TextView _price;
        private TextView _change;

        public FundHolder(View itemView) {
            super(itemView);
            _symbol = (TextView) itemView.findViewById(R.id.fund_symbol);
            _price = (TextView) itemView.findViewById(R.id.fund_price);
            _change = (TextView) itemView.findViewById(R.id.fund_change);
        }

        private void bindFund(Fund fund) {
            _symbol.setText(fund.Symbol);
            _price.setText(getString(R.string.money_format, fund.Price));
            _change.setText(getString(R.string.percentage_string_format, fund.Change));
        }
    }

    private class FundAdapter extends RecyclerView.Adapter<FundHolder> {
        private List<Fund> _funds;

        public FundAdapter() {
            _funds = new ArrayList();
        }

        @Override
        public FundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(FundActivity.this);
            View view = layoutInflater.inflate(R.layout.list_item_fund, parent, false);
            return new FundHolder(view);
        }

        @Override
        public void onBindViewHolder(FundHolder fundHolder, int position) {
            Fund fund = _funds.get(position);
            fundHolder.bindFund(fund);
        }

        @Override
        public int getItemCount() {
            return _funds.size();
        }

        public void Add(Fund fund) {
            _funds.add(fund);
            notifyItemInserted(_funds.size());
        }
    }

    private void SetupNavigation(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.fund_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.fund_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_dashboard) {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_purchaseHistory) {
            Intent intent = new Intent(this, StockPurchaseActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_controlPanel) {
            Intent intent = new Intent(this, ControlPanelActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.fund_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}