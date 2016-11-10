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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.co.nyakeh.stacks.database.StockLab;
import uk.co.nyakeh.stacks.objects.StockPurchase;

public class FundActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IAsyncTask {
    private TextView _vmidPrice;
    private TextView _vmidChange;
    private TextView _vwrlPrice;
    private TextView _vwrlChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund);
        Toolbar toolbar = (Toolbar) findViewById(R.id.fund_toolbar);
        setSupportActionBar(toolbar);
        SetupNavigation(toolbar);

        _vmidPrice = (TextView) findViewById(R.id.fund_vmidPrice);
        _vmidChange = (TextView) findViewById(R.id.fund_vmidChange);
        _vwrlPrice = (TextView) findViewById(R.id.fund_vwrlPrice);
        _vwrlChange = (TextView) findViewById(R.id.fund_vwrlChange);
        new GoogleFinanceClient(this).execute("LON:VMID");
        new GoogleFinanceClient(this).execute("LON:VWRL");
    }

    public void PostExecute(String result) {
        try {
            JSONObject share = new JSONObject(result);
            double price = Double.parseDouble(share.get("l").toString());
            String change = share.get("c").toString();
            String symbol = share.get("t").toString();
            switch (symbol){
                case "VMID":
                    _vmidPrice.setText(getString(R.string.money_format, price));
                    _vmidChange.setText(change);
                    break;
                case "VWRL":
                    _vwrlPrice.setText(getString(R.string.money_format, price));
                    _vwrlChange.setText(change);
                    break;
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
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
        if (id == R.id.nav_main) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_dashboard) {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_stock) {
            Intent intent = new Intent(this, StockActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_purchaseHistory) {
            Intent intent = new Intent(this, StockPurchaseActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.fund_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}