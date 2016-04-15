package hhtv.com.mp3slider.api;

import java.util.List;

import hhtv.com.mp3slider.model.itemdetail.ItemDetail;
import hhtv.com.mp3slider.model.itemlist.ItemList;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by nienb on 12/4/16.
 */
public interface ApiEndpoint {
    @GET("/list")
    Call<List<ItemList>> getItem();
    @GET("/detail")
    Call<ItemDetail> getItemDetail(@Query("id") int id);
}
