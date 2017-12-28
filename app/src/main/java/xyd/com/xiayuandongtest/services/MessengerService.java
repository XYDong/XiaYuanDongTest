package xyd.com.xiayuandongtest.services;


import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import xyd.com.xiayuandongtest.utils.LogUtils;

import static xyd.com.xiayuandongtest.utils.Constants.MSG_FROM_CLIEN;
import static xyd.com.xiayuandongtest.utils.Constants.MSG_FROM_SERVICE;

/**
 * Created by Administrator on 2017/12/24.
 */

public class MessengerService extends Service {

    public static final String TAG = "MessengerServie";

    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROM_CLIEN:
                    LogUtils.i("receive msg Client:" + msg.getData().getString("msg"));
                    Messenger messenger = msg.replyTo;
                    Message obtain = Message.obtain(null, MSG_FROM_SERVICE);
                    try {
                        Bundle bundle = new Bundle();
                        bundle.putString("reply","这是服务器的响应");
                        obtain.setData(bundle);
                        messenger.send(obtain);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                    default:
                        super.handleMessage(msg);
            }
        }

    }


    private final Messenger messengerHandler = new Messenger(new MessengerHandler());
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messengerHandler.getBinder();
    }
}
