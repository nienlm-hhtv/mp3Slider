package hhtv.com.mp3slider.helper;

import android.os.Handler;
import android.os.Message;

import java.util.Stack;

/**
 * Created by nienb on 12/4/16.
 */
public class MyHandler extends Handler {
    Stack<Message> s = new Stack<Message>();
    boolean is_paused = false;

    public synchronized void pause() {
        is_paused = true;
    }

    public synchronized void resume() {
        is_paused = false;
        while (!s.empty()) {
            sendMessageAtFrontOfQueue(s.pop());
        }
    }

    @Override
    public void handleMessage(Message msg) {
        if (is_paused) {
            s.push(Message.obtain(msg));
            return;
        }

        // otherwise handle message as normal
        // ...
    }
    //...
}
