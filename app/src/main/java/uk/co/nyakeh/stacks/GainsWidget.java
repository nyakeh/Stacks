package uk.co.nyakeh.stacks;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import uk.co.nyakeh.stacks.database.StockLab;
import uk.co.nyakeh.stacks.records.StockPurchase;

public class GainsWidget extends AppWidgetProvider implements IAsyncTask {
    private AppWidgetManager _appWidgetManager;
    private int _appWidgetId;
    private Context _context;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        _context = context;
        _appWidgetManager = appWidgetManager;
        _appWidgetId = appWidgetId;
        new GoogleFinanceClient(this, context).execute("LON:VMID");
    }

    @Override
    public void PostExecute(String response) {
        RemoteViews layout = new RemoteViews(_context.getPackageName(), R.layout.gains_widget);
        try {
            JSONObject share = new JSONObject(response);
            double sharePrice = Double.parseDouble(share.get("l").toString());
            double purchaseCost = 0;
            int stockQuantity = 0;
            List<StockPurchase> stockPurchaseHistory = StockLab.get(_context).getStockPurchaseHistory("VMID.L");
            for (StockPurchase stockPurchase : stockPurchaseHistory) {
                purchaseCost += stockPurchase.Total;
                stockQuantity += stockPurchase.Quantity;
            }
            double currentPortfolioValue = sharePrice * stockQuantity;
            double diff = currentPortfolioValue - purchaseCost;
            double percentageChange = (diff / purchaseCost) * 100;
            CharSequence formattedDiff = String.format("£%,.2f", diff);
            CharSequence formattedPercentageChange = String.format("↑%.2f%%", percentageChange);

            layout.setTextViewText(R.id.gains_widget_diff, formattedDiff);
            layout.setTextViewText(R.id.gains_widget_percentage, formattedPercentageChange);
            _appWidgetManager.updateAppWidget(_appWidgetId, layout);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}