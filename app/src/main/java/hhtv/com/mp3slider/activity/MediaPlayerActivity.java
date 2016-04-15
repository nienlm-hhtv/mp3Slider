package hhtv.com.mp3slider.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.exoplayer.ExoPlayer;
import com.squareup.picasso.Picasso;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hhtv.com.mp3slider.R;
import hhtv.com.mp3slider.api.ApiEndpoint;
import hhtv.com.mp3slider.api.ApiService;
import hhtv.com.mp3slider.helper.CommonErrorDialogBuilder;
import hhtv.com.mp3slider.model.itemdetail.Data;
import hhtv.com.mp3slider.model.itemdetail.ItemDetail;
import hhtv.com.mp3slider.service.IServiceCallback;
import hhtv.com.mp3slider.service.MusicPlayerService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by nienb on 12/4/16.
 */
public class MediaPlayerActivity extends Activity implements DiscreteSeekBar.OnProgressChangeListener, BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener, IServiceCallback{

    private static final String TAG = "MediaPlayerActivity";
    private static final String TAG2 = "TAG2";
    @Bind(R.id.mediaplayer_sliderpanel)
    LinearLayout mediaplayerSliderpanel;
    @Bind(R.id.mediaplayer_txt_currenttime)
    TextView mediaplayerTxtCurrenttime;
    @Bind(R.id.mediaplayer_seekbar)
    DiscreteSeekBar mediaplayerSeekbar;
    @Bind(R.id.mediaplayer_txt_totaltime)
    TextView mediaplayerTxtTotaltime;
    @Bind(R.id.mediaplayer_detailpanel)
    RelativeLayout mediaplayerDetailpanel;
    @Bind(R.id.mediaplayer_playbtn)
    ImageButton mediaplayerPlaybtn;
    @Bind(R.id.mediaplayer_loading)
    ProgressBar mediaplayerLoading;
    @Bind(R.id.mediaplayer_slider)
    SliderLayout mSlider;
    private int mediaId = 1;
    private CommonErrorDialogBuilder errorDialogBuilder;
    private ApiEndpoint api = ApiService.build();
    private Handler myHandler = new Handler();
    private List<Data> datas = new ArrayList<>();
    //service
    private MusicPlayerService musicSrv;
    private Intent playIntent;
    //binding
    private boolean musicBound=false;

    //flags
    private int playerState = 1;
    private boolean isNeedToChangeSlide = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mediaplayer);
        ButterKnife.bind(this);
        errorDialogBuilder = new CommonErrorDialogBuilder(this);
        mediaplayerPlaybtn.setTag(false);
        mSlider.addOnPageChangeListener(this);
        mediaplayerSeekbar.setOnProgressChangeListener(this);
        mediaplayerSeekbar.setIndicatorPopupEnabled(true);
    }




    //region Service binding
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayerService.MusicBinder binder = (MusicPlayerService.MusicBinder)service;
            musicSrv = binder.getService();
            musicBound = true;
            musicSrv.setCallback(MediaPlayerActivity.this);
            processLoadingMediaDetail();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            playIntent = null;
            musicBound = false;
        }
    };

    //endregion


    //region Activity lifecycle callback
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG2, "onStart: !! " + musicBound  + " - " + (musicSrv == null) + " - " + (playIntent == null));
        if(playIntent==null){


        }else{

        }
        playIntent = new Intent(this, MusicPlayerService.class);
        bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        startService(playIntent);
    }

    @Override
    protected void onStop() {
        Log.d(TAG2, "onStop: ");
        /*if (musicBound){
            unbindService(musicConnection);
            musicBound = false;
        }*/
        //unbindService(musicConnection);
        //musicSrv=null;
        /*if (musicSrv != null){
            musicSrv.pausePlayer();
        }*/
        musicSrv.pausePlayer();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG2, "onDestroy: ");
        if (musicBound){
            unbindService(musicConnection);
            musicBound = false;
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG2, "onConfigurationChanged: ");
        super.onConfigurationChanged(newConfig);

        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            Log.d(TAG2, "Portrait");
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            Log.d(TAG2, "Landscape");
    }

   /* @Override
    public void onBackPressed() {
        musicSrv.pausePlayer();
        musicSrv.stopAndRelease();
        *//*if (playIntent != null){
            stopService(playIntent);
        }
        unbindService(musicConnection);*//*
        musicSrv = null;
        finish();
    }*/

    //endregion


    //region UI interactive methods
    @OnClick(R.id.mediaplayer_playbtn)
    public void onMediaPlayerClick() {
        //boolean isPlaying = (boolean) mediaplayerPlaybtn.getTag();
        showControl(true);
        if (musicSrv.isPng()) {
            musicSrv.pausePlayer();
            Picasso.with(this).load(R.drawable.ic_play_circle_filled_white).into(mediaplayerPlaybtn);
            mediaplayerPlaybtn.setTag(false);
        } else {
            switch (musicSrv.getPlaybackState()){
                case ExoPlayer.STATE_IDLE:
                    if (model != null){
                        musicSrv.setSong(model, true);
                    }
                    return;
                case ExoPlayer.STATE_BUFFERING:
                    break;
                case ExoPlayer.STATE_READY:
                    break;
                case ExoPlayer.STATE_ENDED:
                    if (model != null){
                        musicSrv.setSong(model, true);
                    }
                    break;
                case ExoPlayer.STATE_PREPARING:
                    //toggleDisplayPlaybtn(false);
                    return;
                default:
                    return;
            }
            musicSrv.go();
            Picasso.with(this).load(R.drawable.ic_pause_circle_filled).into(mediaplayerPlaybtn);
            mediaplayerPlaybtn.setTag(true);
        }
    }
    private void toggleDisplayPlaybtn(boolean show) {
        mediaplayerPlaybtn.setVisibility(show ? View.VISIBLE : View.GONE);
        mediaplayerLoading.setVisibility(show ? View.GONE : View.VISIBLE);
    }


    private Handler handler = new Handler();
    private Runnable runable = new Runnable() {
        @Override
        public void run() {
            mediaplayerDetailpanel.setVisibility(View.GONE);
            mediaplayerPlaybtn.setVisibility(View.GONE);
        }
    };
    public void showControl(boolean autoHide){
        mediaplayerDetailpanel.setVisibility(View.VISIBLE);
        mediaplayerPlaybtn.setVisibility(View.VISIBLE);
        if (autoHide){
            handler.postDelayed(runable, TimeUnit.SECONDS.toMillis(5));
        }else{
            handler.removeCallbacks(runable);
        }
    }

    //endregion


    //region Service callback
    @Override
    public void onMediaPrepared(long duration, boolean playAfterPrepared) {
        mediaplayerTxtTotaltime.setText(timeToDisplay(duration));
        mediaplayerSeekbar.setMin(0);
        mediaplayerSeekbar.setMax((int)duration);
        mediaplayerSeekbar.setProgress(0);
        mSlider.setCurrentPosition(0, true);
        toggleDisplayPlaybtn(true);
        if (playAfterPrepared){
            Picasso.with(this).load(R.drawable.ic_pause_circle_filled).into(mediaplayerPlaybtn);
            musicSrv.go();
        }else{
            Picasso.with(this).load(R.drawable.ic_play_circle_filled_white).into(mediaplayerPlaybtn);
        }
    }

    @Override
    public void onMediaCompleted() {
        this.finish();
        /*Picasso.with(this).load(R.drawable.ic_play_circle_filled_white).into(mediaplayerPlaybtn);
        //musicSrv.player.prepareAsync();
        mediaplayerPlaybtn.setTag(false);
        musicSrv.seek(0);
        showControl(false);*/
    }

    @Override
    public void onMediaProgressChanged(long current) {
        mediaplayerTxtCurrenttime.setText(timeToDisplay(current));
        mediaplayerSeekbar.setProgress((int)current);
        int currentSlideToDisplay = getCurrentSlideToDisplay();
        if (!isNeedToChangeSlide){
            //currentSlideToDisplay = mSlider.getCurrentPosition();
            //musicSrv.seek((int)TimeUnit.SECONDS.toMillis(model.getData().get(currentSlideToDisplay).getStart()));
            //isNeedToChangeSlide = true;
            return;
        }else
        if (currentSlideToDisplay != mSlider.getCurrentPosition() && currentSlideToDisplay != -1){
            mSlider.setCurrentPosition(currentSlideToDisplay, true);
        }
    }

    @Override
    public void onMediaStatusChanged(boolean play, int status) {
        Log.d(TAG, "onMediaStatusChanged: " + play + " - " + status);
        playerState = status;
    }
    //endregion


    //region seekbar callback

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
        //Log.d(TAG, "onProgressChanged: " + value + " - " + fromUser);
        mediaplayerSeekbar.setIndicatorFormatter(timeToDisplay(seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
        showControl(false);
    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
        int pos = seekBar.getProgress();
        musicSrv.seek(pos);
        showControl(true);
    }
    //endregion


    //region Slider callback
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Log.e(TAG, "onPageScrolled: " + position +  " - " + positionOffset );
    }

    @Override
    public void onPageSelected(int position) {
        Log.e(TAG, "onPageSelected: " + position + " - " + isNeedToChangeSlide);
        if (!isNeedToChangeSlide){
            int currentSlideToDisplay = mSlider.getCurrentPosition();
            musicSrv.seek((int)TimeUnit.SECONDS.toMillis(model.getData().get(currentSlideToDisplay).getStart()));
            isNeedToChangeSlide = true;
        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.e(TAG, "onPageScrollStateChanged: " + state );
        if (state == 1)
            isNeedToChangeSlide = false;

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Log.e(TAG, "onSliderClick: ");
        showControl(true);
    }
    //endregion


    //region Common methods
    @NonNull
    private String timeToDisplay(long millis) {
        StringBuffer buf = new StringBuffer();
        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);
        if (hours > 0) {
            buf
                    .append(String.format("%02d", hours))
                    .append(":");
        }
        buf.append(String.format("%02d", minutes)).append(":").append(String.format("%02d", seconds));

        return buf.toString();
    }

    private int getCurrentSlideToDisplay(){
        int currentMediaposition = musicSrv.getPosn();
        for (int i = 0; i < datas.size(); i++){
            Data d = datas.get(i);
            if (TimeUnit.SECONDS.toMillis(d.getStart()) <= currentMediaposition &&
                    TimeUnit.SECONDS.toMillis(d.getEnd()) >= currentMediaposition){
                return i;
            }
        }
        return -1;
    }


    //endregion

    ItemDetail model;
    public void processLoadingMediaDetail() {
        Call<ItemDetail> call = api.getItemDetail(mediaId);
        call.enqueue(new Callback<ItemDetail>() {
            @Override
            public void onResponse(Response<ItemDetail> response, Retrofit retrofit) {
                model = response.body();
                //Toast.makeText(MediaPlayerActivity.this, "processLoadingMediaDetail", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onResponse: " + model.toString());
                musicSrv.setSong(model, false);
                datas.clear();
                datas.addAll(model.getData());
                for (Data d : model.getData()) {
                    DefaultSliderView sliderView = new DefaultSliderView(MediaPlayerActivity.this);
                    sliderView.setScaleType(BaseSliderView.ScaleType.CenterInside);
                    sliderView.image(d.getImage());
                    sliderView.setOnSliderClickListener(MediaPlayerActivity.this);
                    mSlider.addSlider(sliderView);
                    mSlider.stopAutoCycle();
                }



            }

            @Override
            public void onFailure(Throwable t) {
                errorDialogBuilder.show();
            }
        });
    }
}
