package uk.co.nyakeh.stacks;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

import uk.co.nyakeh.stacks.database.StockLab;
import uk.co.nyakeh.stacks.objects.StockPurchase;

public class StockPurchaseActivity extends AppCompatActivity {
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addStockPurchase() {
        StockPurchase stockPurchase = new StockPurchase(UUID.randomUUID(), mSymbolField.getText().toString(), new Date(), Double.parseDouble(mPriceField.getText().toString()), Integer.parseInt(mQuantityField.getText().toString()), Double.parseDouble(mFeeField.getText().toString()));
        StockLab.get(this).addStockPurchase(stockPurchase);
    }
}