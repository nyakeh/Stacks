package uk.co.nyakeh.stacks;

import retrofit2.Retrofit;

public class APIHandler {
    private static final String BASE_API_URL = "http://query.yahooapis.com/v1/public";
    private static Retrofit restAdapter;

    private static  Retrofit getRestAdapter(){
        if(restAdapter==null){
            restAdapter = new Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
                    .build();
        }
        return restAdapter;
    }

    public static IApiMethods getApiInterface(){

        // Create an instance of our  API interface.
        IApiMethods weatherAPI =null;
        try {
            if(restAdapter==null){
                restAdapter=getRestAdapter();
            }
            weatherAPI = restAdapter.create(IApiMethods.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weatherAPI;
    }

}