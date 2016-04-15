package hhtv.com.mp3slider.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hhtv.com.mp3slider.R;
import hhtv.com.mp3slider.adapter.ItemListAdapter;
import hhtv.com.mp3slider.api.ApiEndpoint;
import hhtv.com.mp3slider.api.ApiService;
import hhtv.com.mp3slider.helper.CommonErrorDialogBuilder;
import hhtv.com.mp3slider.model.itemlist.ItemList;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by nienb on 14/4/16.
 */
public class ItemListActivity extends Activity implements ItemListAdapter.IOnItemClick{


    @Bind(R.id.mainlist)
    RecyclerView mainlist;
    ItemListAdapter mAdapter;
    MaterialDialog d;
    CommonErrorDialogBuilder e;
    ApiEndpoint api = ApiService.build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        d = new MaterialDialog.Builder(this)
                .title("Loading....")
                .content("Please wait")
                .progress(true, 1).build();
        e = new CommonErrorDialogBuilder(this);
        mainlist.setLayoutManager(new LinearLayoutManager(this));
        processLoadItemList();
    }


    private void processLoadItemList(){
        d.show();
        Call<List<ItemList>> call = api.getItem();
        call.enqueue(new Callback<List<ItemList>>() {
            @Override
            public void onResponse(Response<List<ItemList>> response, Retrofit retrofit) {
                d.hide();
                mAdapter = new ItemListAdapter(response.body(), ItemListActivity.this, ItemListActivity.this);
                mainlist.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Throwable t) {
                d.hide();
                e.show();
            }
        });
    }

    @Override
    public void onItemClick(ItemList i) {
        Intent in = new Intent(ItemListActivity.this, MediaPlayerActivity.class);
        startActivity(in);
    }
}
