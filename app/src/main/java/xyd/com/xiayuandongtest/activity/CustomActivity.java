package xyd.com.xiayuandongtest.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyd.com.xiayuandongtest.R;
import xyd.com.xiayuandongtest.entity.StepBean;
import xyd.com.xiayuandongtest.views.StepView;


//自定义view
public class CustomActivity extends AppCompatActivity {

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.rl_stepview)
    RelativeLayout rlStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        ButterKnife.bind(this);

        StepView stepView = new StepView(this);

        stepView.setUnCompletedLineColor(Color.WHITE);
        stepView.setCompletedLineColor(Color.GREEN);
        stepView.setDefaultIcon(getResources().getDrawable(R.mipmap.default_icon));
        stepView.setCompleteIcon(getResources().getDrawable(R.mipmap.complted));
        stepView.setAttentionIcon(getResources().getDrawable(R.mipmap.attention));
        List<StepBean> stepBeans = new ArrayList<>();
        StepBean stepBean1  = new StepBean("接单",1);
        StepBean stepBean2  = new StepBean("打包",0);
        StepBean stepBean3  = new StepBean("出发",-1);
        stepBeans.add(stepBean1);
        stepBeans.add(stepBean2);
        stepBeans.add(stepBean3);
        stepView.setStepNum(stepBeans);
        rlStep.addView(stepView);

    }
}
