package xyd.com.xiayuandongtest.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import xyd.com.xiayuandongtest.R;
import xyd.com.xiayuandongtest.services.BindTestService;
import xyd.com.xiayuandongtest.services.StartTestService;

public class ServiceTestActivity extends AppCompatActivity implements ServiceConnection{

    private Intent intent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent1 = new Intent(this,BindTestService.class);
        this.bindService(intent1,this, Context.BIND_AUTO_CREATE);
        intent2 = new Intent(this,StartTestService.class);
        startService(intent2);
    }

    @Override
    protected void onDestroy() {
//        this.unbindService(this);
//        stopService(intent2);
        super.onDestroy();

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        String service1 = ((BindTestService.MyBindServiceBinder) service).getService();
        System.out.println(service1);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        System.out.println("BindTestService onServiceDisconnected");
    }
}
