package uk.co.nyakeh.stacks;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

public interface IApiMethods {
    @GET("/yql?env=http%3A%2F%2Fdatatables.org%2Falltables.env&format=json&q=select%20*%20from%20yahoo.finance.historicaldata%20where%20startDate=%272016-01-01%27%20and%20endDate=%272017-01-10%27%20and%20symbol=%27vmid.l%27")
    void getWeatherCity(Callback<YahooStockQuery> callback);

    @GET("/yql?env=http%3A%2F%2Fdatatables.org%2Falltables.env&format=json&q=select%20*%20from%20yahoo.finance.historicaldata%20where%20startDate=%272016-01-01%27%20and%20endDate=%272017-01-10%27%20and%20symbol=%27vmid.l%27")
    Call<YahooStockResponse> getStockHistory();
}
