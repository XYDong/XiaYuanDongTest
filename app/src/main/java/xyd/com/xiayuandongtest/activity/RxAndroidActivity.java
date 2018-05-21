package xyd.com.xiayuandongtest.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyd.com.xiayuandongtest.R;
import xyd.com.xiayuandongtest.views.HuaWeiView;


public class RxAndroidActivity extends AppCompatActivity {

    @BindView(R.id.ev_name)
    TextInputEditText evName;
    @BindView(R.id.ev_pwd)
    TextInputEditText evPwd;
    @BindView(R.id.huaWeiView)
    HuaWeiView huaWeiView;
    @BindView(R.id.ll_rx_all)
    LinearLayout ll_rx_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_android);
        ButterKnife.bind(this);
        RxTextView.textChanges(evName).subscribe(charSequence -> {
            Toast.makeText(RxAndroidActivity.this, String.valueOf(charSequence), Toast.LENGTH_SHORT).show();
        });
        huaWeiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huaWeiView.changeAngle(200);
            }
        });
        huaWeiView.setOnAngleColorListener(new HuaWeiView.OnAngleColorListener() {
            @Override
            public void colorListener(int red, int green) {
                Color color = new Color();
                int argb = Color.argb(100, red, green, 0);
                ll_rx_all.setBackgroundColor(argb);

            }
        });
    }

}
