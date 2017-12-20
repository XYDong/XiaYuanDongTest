package xyd.com.xiayuandongtest.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.util.List;

import xyd.com.xiayuandongtest.utils.Utils;

/**
 * 描述：
 * 作者：shuiq_000
 * 邮箱：2028318192@qq.com
 *
 * @version 1.0
 * @time: 2017/12/6 21:00
 */

public class XydApplication extends Application {
    public static final String TAG = "XydApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if(runningAppProcess.pid == Process.myPid()){
                Log.i(TAG,"application start,process name:"+runningAppProcess.processName);
            }
        }
    }
}
