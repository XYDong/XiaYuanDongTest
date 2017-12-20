package xyd.com.xiayuandongtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyd.com.xiayuandongtest.R;
import xyd.com.xiayuandongtest.entity.User;
import xyd.com.xiayuandongtest.test.UserManager;
import xyd.com.xiayuandongtest.utils.LogUtils;
import xyd.com.xiayuandongtest.utils.MyUtils;

import static xyd.com.xiayuandongtest.utils.Constants.CACHE_FILE_PATH;

public class Test1Activity extends AppCompatActivity {

    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.btn_start_test2)
    Button btnStartTest2;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        ButterKnife.bind(this);
        LogUtils.i("Test1Activity+onCreate");
        LogUtils.i("Test1Activity+UserManager.sUerId=="+ UserManager.sUserId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.i("Test1Activity+onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i("Test1Activity+onResume");
        recoverFromFile();

    }
    public void recoverFromFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(CACHE_FILE_PATH);
                ObjectInputStream inputStream = null;

                try {
                    inputStream = new ObjectInputStream(new FileInputStream(file));
                    user = (User)inputStream.readObject();
                    LogUtils.i("recoverFromFile user=="+user);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    MyUtils.closeInIO(inputStream);
                }


            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.i("Test1Activity+onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i("Test1Activity+onDestroy");

    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i("Test1Activity+onPause");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.i("Test1Activity+onRestart");

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.i("Test1Activity+onNewIntent");
    }

    @OnClick(R.id.btn_start_test2)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_test2:
                startActivity(new Intent(this,Test2Activity.class));
                break;
        }

    }
}
