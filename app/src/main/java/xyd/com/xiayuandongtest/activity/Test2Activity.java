package xyd.com.xiayuandongtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyd.com.xiayuandongtest.R;
import xyd.com.xiayuandongtest.utils.LogUtils;

public class Test2Activity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.btn_start_main)
    Button btnStartMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        ButterKnife.bind(this);
        LogUtils.i("Test2Activity+onCreate");

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.i("Test2Activity+onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i("Test2Activity+onResume");

    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.i("Test2Activity+onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i("Test2Activity+onDestroy");

    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i("Test2Activity+onPause");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.i("Test2Activity+onRestart");

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.i("Test2Activity+onNewIntent");
    }

    @OnClick(R.id.btn_start_main)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_main:
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }
}
