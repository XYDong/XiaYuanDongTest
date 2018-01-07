package xyd.com.xiayuandongtest.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyd.com.xiayuandongtest.R;
import xyd.com.xiayuandongtest.entity.User2;
import xyd.com.xiayuandongtest.providers.ProviderActivity;
import xyd.com.xiayuandongtest.services.MessengerService;
import xyd.com.xiayuandongtest.tcp_service.MsgActivity;
import xyd.com.xiayuandongtest.test.UserManager;
import xyd.com.xiayuandongtest.utils.LogUtils;
import xyd.com.xiayuandongtest.utils.MyUtils;

import static xyd.com.xiayuandongtest.utils.Constants.CACHE_FILE_PATH;
import static xyd.com.xiayuandongtest.utils.Constants.MSG_FROM_CLIEN;
import static xyd.com.xiayuandongtest.utils.Constants.MSG_FROM_SERVICE;
import static xyd.com.xiayuandongtest.utils.Constants.TEST_FILE_1;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_2_test1)
    Button btn2Test1;
    @BindView(R.id.btn_repeat)
    Button btnRepeat;
    public static final String TAG = "xyd.mainactivity";
    @BindView(R.id.btn_bookmanager_activity)
    Button btnBookmanagerActivity;


    private Messenger mServie;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServie = new Messenger(service);
            Message msg = Message.obtain(null, MSG_FROM_CLIEN);
            Bundle bundle = new Bundle();
            bundle.putString("msg", "hello,this is client.");
            msg.setData(bundle);
            msg.replyTo = messenger;
            try {
                mServie.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            
        }
    };

    private Messenger messenger = new Messenger(new MyMainHanler());

    class MyMainHanler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROM_SERVICE:
                    LogUtils.i(msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }
            super.handleMessage(msg);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LogUtils.i("MainActivity+onCreate");
        UserManager.sUserId = 2;
        LogUtils.i("MainActivity+UserManager.sUerId==" + UserManager.sUserId);
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.i("MainActivity+onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i("MainActivity+onResume");
        // 申请录音权限
        AndPermission.with(this)
                .requestCode(200)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        if (requestCode == 200) {
                            persist2File();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

                    }
                })
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale((requestCode, rationale) ->
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(this, rationale).show()
                )
                .start();
    }

    public void persist2File() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User2 user = new User2(1, "hello world ", false);
                File dir = new File(TEST_FILE_1);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File cachedFile = new File(CACHE_FILE_PATH);
                ObjectOutputStream objectOutputStream = null;
                try {
                    objectOutputStream = new ObjectOutputStream(new FileOutputStream(cachedFile));
                    objectOutputStream.writeObject(user);
                    LogUtils.i("persist user==" + user);
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    MyUtils.close(objectOutputStream);
                }


            }
        }).start();
    }


    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.i("MainActivity+onStop");
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
        LogUtils.i("MainActivity+onDestroy");

    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i("MainActivity+onPause");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.i("MainActivity+onRestart");

    }

    @OnClick({R.id.btn_2_test1, R.id.btn_repeat,R.id.btn_bookmanager_activity,R.id.btn_start_provideractivity,R.id.btn_start_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_2_test1:
                startActivity(new Intent(this, Test1Activity.class));
                break;
            case R.id.btn_repeat:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btn_bookmanager_activity:
                startActivity(new Intent(this, BookManagerActivity.class));
                break;
            case R.id.btn_start_provideractivity:
                startActivity(new Intent(this, ProviderActivity.class));
                break;
            case R.id.btn_start_msg:
                startActivity(new Intent(this, MsgActivity.class));
                break;

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.i("MainActivity+onSaveInstanceState");
        outState.putString("onSaveInstanceState", "这是一条测试数据");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.i("MainActivity+onRestoreInstanceState=====" + savedInstanceState.getString("onSaveInstanceState"));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.i("MainActivity+onConfigurationChanged");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.i("MainActivity+onNewIntent");
    }
}
