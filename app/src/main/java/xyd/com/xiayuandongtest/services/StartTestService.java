package xyd.com.xiayuandongtest.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;


/**
 - @Description:  测试start
 - @Author:  xyd
 - @Time:  2018/5/17 11:41
 */
public class StartTestService extends Service {

    public static final String TAG = "StartTestService";
    private Thread startTestService_runing;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyServiceBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startTestService_runing = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    Thread.sleep(2000);
                    LogUtils.i("StartTestService runing");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        startTestService_runing.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("StartTestService onDestroy");
    }

    class MyServiceBinder extends Binder {
        public String getService(){
            return TAG;
        }
    }
}
