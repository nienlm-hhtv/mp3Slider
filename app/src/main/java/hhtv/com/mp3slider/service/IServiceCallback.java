package hhtv.com.mp3slider.service;

/**
 * Created by nienb on 14/4/16.
 */
public interface IServiceCallback {
    void onMediaPrepared(long duration, boolean playAfterPrepared);
    void onMediaProgressChanged(long current);
    void onMediaStatusChanged(boolean play, int status);
    void onMediaCompleted();
}
