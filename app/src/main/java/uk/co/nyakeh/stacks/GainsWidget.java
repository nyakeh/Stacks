package uk.co.nyakeh.stacks;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import org.json.JSONException;
import org.json.JSONObject;

public class GainsWidget extends AppWidgetProvider implements IAsyncTask {
    private RemoteViews mViews;
    private AppWidgetManager mAppWidgetManager;
    private int mAppWidgetId;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        mViews = new RemoteViews(context.getPackageName(), R.layout.gains_widget);
        mAppWidgetManager = appWidgetManager;
        mAppWidgetId = appWidgetId;
        new GoogleFinanceClient(this).execute("LON:VMID");
    }

    @Override
    public void PostExecute(String response) {
        try {
            JSONObject share = new JSONObject(response);
            double sharePrice = Double.parseDouble(share.get("l").toString());
            double portfolioSum = sharePrice * 112;
            double purchaseSum = 3062.87;
            double diff = portfolioSum - purchaseSum;
            double percentageChange = (diff / purchaseSum) * 100;
            CharSequence formattedDiff = String.format("£%,.2f", diff);
            CharSequence formattedPercentageChange = String.format("↑%.2f%%", percentageChange);

            mViews.setTextViewText(R.id.gains_widget_diff, formattedDiff);
            mViews.setTextViewText(R.id.gains_widget_percentage, formattedPercentageChange);
            mAppWidgetManager.updateAppWidget(mAppWidgetId, mViews);
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }
}