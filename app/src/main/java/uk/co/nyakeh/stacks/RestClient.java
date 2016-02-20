package uk.co.nyakeh.stacks;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import uk.co.nyakeh.stacks.Objects.FinanceApi.YahooOverviewResponse;
import uk.co.nyakeh.stacks.Objects.FinanceApi.YahooStockResponse;

public class RestClient {
    private static FinanceApiInterface sFinanceApiInterface;
    private static String baseUrl = "http://query.yahooapis.com/v1/public/" ;

    public static FinanceApiInterface getClient() {
        if (sFinanceApiInterface == null) {

            OkHttpClient okClient = new OkHttpClient.Builder()
                    .addInterceptor(
                            new Interceptor() {
                                @Override
                                public Response intercept(Interceptor.Chain chain) throws IOException {
                                    Request original = chain.request();

                                    // Request customization: add request headers
                                    Request.Builder requestBuilder = original.newBuilder()
                                            //.header("Authorization", token)
                                            .method(original.method(), original.body());

                                    Request request = requestBuilder.build();
                                    return chain.proceed(request);
                                }
                            })
                    .build();

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            sFinanceApiInterface = client.create(FinanceApiInterface.class);
        }
        return sFinanceApiInterface;
    }

    public interface FinanceApiInterface {
        @Headers({"Content-Type: application/json"})
        @GET("yql?env=http%3A%2F%2Fdatatables.org%2Falltables.env&format=json&q=select%20*%20from%20yahoo.finance.historicaldata%20where%20startDate=%272016-01-01%27%20and%20endDate=%272017-01-10%27%20and%20symbol=%27vmid.l%27")
        Call<YahooStockResponse> getStockHistory();

        @Headers({"Content-Type: application/json"})
        @GET("yql?q=select%20%2a%20from%20yahoo.finance.quotes%20where%20symbol%20in%20%28%22VMID.L%22%29%0A%09%09&env=http%3A%2F%2Fdatatables.org%2Falltables.env&format=json")
        Call<YahooOverviewResponse> getStockOverview();
    }
}
