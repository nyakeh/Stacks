package uk.co.nyakeh.stacks;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GainsWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.gains_widget);
        WebClient latestSharePriceTask = new WebClient(views, appWidgetManager, appWidgetId);
        latestSharePriceTask.execute("http://finance.google.com/finance/info?client=ig&q=LON:VMID");
    }

    public class WebClient extends AsyncTask<String, Integer, String> {
        private OkHttpClient mOkHttpClient = new OkHttpClient();
        private RemoteViews mViews;
        private AppWidgetManager mAppWidgetManager;
        private int mAppWidgetId;

        public WebClient(RemoteViews views, AppWidgetManager appWidgetManager, int appWidgetId) {
            mViews = views;
            mAppWidgetManager = appWidgetManager;
            mAppWidgetId = appWidgetId;
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            String url = params[0];
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                result = response.body().string();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String responseBody) {
            super.onPostExecute(responseBody);
            try {
                JSONObject share = new JSONObject(responseBody);
                double sharePrice = Double.parseDouble(share.get("l").toString());
                double portfolioSum = sharePrice * 112;
                double purchaseSum = 3062.87;
                double diff = portfolioSum - purchaseSum;
                double percentageChange = (diff / purchaseSum) * 100;
                CharSequence widgetText = String.format("↑ %.2f%%", percentageChange);

                mViews.setTextViewText(R.id.gains_widget_text, widgetText);
                mAppWidgetManager.updateAppWidget(mAppWidgetId, mViews);
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
    }
}

