package xyd.com.xiayuandongtest.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

/**
 - @Description:  测试bind
 - @Author:  xyd
 - @Time:  2018/5/17 11:39
 */

public class BindTestService extends Service {

    public static final String TAG = "BindTestService";
    private Thread bindTestService_runing;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBindServiceBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bindTestService_runing = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    Thread.sleep(2000);
                    LogUtils.i("BindTestService runing");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        bindTestService_runing.start();
    }

    public class MyBindServiceBinder extends Binder {
        public String getService(){
         return TAG;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("BindTestService onDestroy");
        bindTestService_runing.stop();
    }
}
