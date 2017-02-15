package uk.co.nyakeh.stacks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import uk.co.nyakeh.stacks.database.StockLab;
import uk.co.nyakeh.stacks.records.Metadata;

public class ControlPanelActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText _yearlyExpenses;
    private EditText _safeWithdrawalRate;
    private TextView _financialIndependenceNumber;
    private EditText _fundsWatchlist;
    private EditText _stockExchangePrefix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.controlPanel_toolbar);
        setSupportActionBar(toolbar);
        SetupNavigation(toolbar);

        Metadata metadata = StockLab.get(this).getMetadata();
        _yearlyExpenses = (EditText) findViewById(R.id.metadata_yearlyExpenses);
        _safeWithdrawalRate = (EditText) findViewById(R.id.metadata_safeWithdrawalRate);
        _financialIndependenceNumber = (TextView) findViewById(R.id.metadata_financialIndependenceNumber);
        _fundsWatchlist = (EditText) findViewById(R.id.metadata_fundsWatchlist);
        _stockExchangePrefix = (EditText) findViewById(R.id.metadata_stockExchangePrefix);

        _yearlyExpenses.setText(String.valueOf(metadata.YearlyExpenses));
        _safeWithdrawalRate.setText(String.valueOf(metadata.SafeWithdrawalRate));
        _financialIndependenceNumber.setText(String.valueOf(metadata.FinancialIndependenceNumber));
        _fundsWatchlist.setText(metadata.FundsWatchlist);
        _stockExchangePrefix.setText(metadata.StockExchangePrefix);
    }

    private void SetupNavigation(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.controlPanel_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.controlPanel_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        } else if (id == R.id.nav_purchaseHistory) {
            Intent intent = new Intent(this, StockPurchaseActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.controlPanel_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}