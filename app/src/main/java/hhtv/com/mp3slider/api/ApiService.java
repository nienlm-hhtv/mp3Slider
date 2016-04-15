package hhtv.com.mp3slider.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by nienb on 12/4/16.
 */
public class ApiService {
    static final String APIENDPOINT = "https://demo3846615.mockable.io";
    static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(APIENDPOINT)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public static ApiEndpoint build() {
        return retrofit.create(ApiEndpoint.class);
    }
}
