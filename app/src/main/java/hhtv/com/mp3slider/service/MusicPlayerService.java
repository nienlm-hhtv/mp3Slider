package hhtv.com.mp3slider.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.devbrackets.android.exomedia.EMAudioPlayer;
import com.devbrackets.android.exomedia.event.EMMediaProgressEvent;
import com.devbrackets.android.exomedia.listener.EMProgressCallback;
import com.devbrackets.android.exomedia.listener.ExoPlayerListener;
import com.devbrackets.android.exomedia.util.MediaUtil;

import hhtv.com.mp3slider.model.itemdetail.ItemDetail;

/**
 * Created by nienb on 12/4/16.
 */
public class MusicPlayerService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, ExoPlayerListener , EMProgressCallback{
    private static final String TAG2 = "TAG2";
    public EMAudioPlayer player;
    private IServiceCallback callback;
    private final IBinder musicBind = new MusicBinder();
    private ItemDetail model;
    private static final String TAG = "MusicPlayerService";
    //current position
    private int songPosn;
    @Override
    public void onCreate() {
        //create the service
        super.onCreate();
        //initialize position
        songPosn=0;
        //create player
        player = new EMAudioPlayer(getApplicationContext());
        //initialize
        initMusicPlayer();
    }

    public void setCallback(IServiceCallback callback) {
        this.callback = callback;
    }

    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.addExoPlayerListener(this);
        player.setProgressCallback(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        callback.onMediaCompleted();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG2, "onError: " + what + " - " + extra);
        mp.reset();
        return false;
    }

    boolean playAfterPrepared = false;
    @Override
    public void onPrepared(MediaPlayer mp) {
        //Toast.makeText(getApplicationContext(), "onPrepared", Toast.LENGTH_SHORT).show();
        callback.onMediaPrepared(player.getDuration(), this.playAfterPrepared);
        //mp.start();
        //notification
        /*Intent notIntent = new Intent(this, MediaPlayerActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.ic_play_circle_filled_white)
                .setTicker(model.getId() + "")
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(model.getAudio());
        Notification not = builder.build();
        startForeground(NOTIFY_ID, not);*/
    }

    public void setSong(ItemDetail itemDetail, boolean playAfterPrepared){
        //Log.d(TAG2, "setSong: ");
        initMusicPlayer();
        this.playAfterPrepared = playAfterPrepared;
        this.model = itemDetail;

        try {
            player.setDataSource(getApplicationContext(),
                    Uri.parse(model.getAudio()),
                    MediaUtil.MediaType.MP3);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        try {
            player.prepareAsync();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //this.model = (ItemDetail) intent.getSerializableExtra("model");
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        try {
            player.stopPlayback();
            player.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private int playbackState = 1;
    private boolean playWhenReady = false;
    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        callback.onMediaStatusChanged(playWhenReady, playbackState);
        this.playbackState = playbackState;
        this.playWhenReady = playWhenReady;
    }

    @Override
    public void onError(Exception e) {
        Log.d(TAG, "onError() returned: " + e);
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unAppliedRotationDegrees, float pixelWidthHeightRatio) {

    }

    @Override
    public boolean onProgressUpdated(EMMediaProgressEvent progressEvent) {
        callback.onMediaProgressChanged(progressEvent.getPosition());
        return false;
    }

    public class MusicBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    /*public void playSong(){
        player.reset();
        try {
            player.setDataSource(model.getAudio());
        } catch (IOException e) {
            Log.e(TAG, "playSong: ",e );
            e.printStackTrace();
        }
    }*/

    public int getPosn(){
        return (int)player.getCurrentPosition();
    }

    public int getDur(){
        return (int)player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public int getPlaybackState(){
        return playbackState;
    }
    public boolean getPlayOnReady(){
        return getPlayOnReady();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }

    public void stopAndRelease(){
        player.stopPlayback();
        player.setOnPreparedListener(null);
        player.setOnCompletionListener(null);
        player.setOnErrorListener(null);
        player.addExoPlayerListener(null);
        player.setProgressCallback(null);
        player.release();
        player = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}
