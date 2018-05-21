package xyd.com.xiayuandongtest.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * @author xyd
 */
public class HuaWeiView extends View {


    private int len;
    /**
     * //oval是一个RectF对象为一个矩形
     */
    private RectF oval;

    /**
     * startAngle为圆弧的起始角度
     */
    private float startAngle=120;
    /**
     * startAngle为圆弧的起始角度
     */
    private float sweepAngle=300;
    boolean useCenter = false;

    /**
     * 圆弧画笔
     */
    private Paint paint;
    /**
     * 半径
     */
    private int radius;
    private float targetAngle = 300;

    //判断是否在动
    private boolean isRunning;
    //判断是回退的状态还是前进状态
    private int state = 1;

    private OnAngleColorListener onAngleColorListener;

    public HuaWeiView(Context context) {
        super(context);
    }

    /**
     * 用来初始化画笔等
     * @param context
     * @param attrs
     */
    public HuaWeiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 数据初始化
     */
    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        //让画出的图形是空心的(不填充)
        paint.setStyle(Paint.Style.STROKE);
        setBackgroundColor(Color.WHITE);

    }


    public HuaWeiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 用来测量限制view为正方形
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        len = Math.min(width,height);
        radius = len / 2;

        oval = new RectF(0,0,len,len);
        setMeasuredDimension(len,len);
    }


    /**
     * 实现各种绘制功能
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画圆弧
        canvas.drawArc(oval,startAngle,sweepAngle,useCenter,paint);
        //画刻度线
        drawViewLine(canvas);
    }

    private void drawViewLine(Canvas canvas) {
        //先保存之前的canvas的内容
        canvas.save();
        //移动canvas（X轴移动距离，Y轴移动距离）
        canvas.translate(radius,radius);
        //旋转坐标系
        canvas.rotate(30);

        Paint paintLine = new Paint();
        //设置画笔颜色
        paintLine.setColor(Color.WHITE);
        paintLine.setAntiAlias(true);
        paintLine.setStrokeWidth(2);
        float rotateAngle = sweepAngle / 100;

        //绘制有色部分的画笔
        Paint targetLinePatin=new Paint();
        targetLinePatin.setColor(Color.GREEN);
        targetLinePatin.setAntiAlias(true);
        targetLinePatin.setStrokeWidth(2);

        //记录已绘制的有色部分范围
        float hasDraw = 0;
//        for (int i = 0; i <= 100; i++) {
//            //需要绘制有色部分的时候
//            if(hasDraw <= rotateAngle && targetAngle!=0){
//                canvas.drawLine(0,radius,0,radius - 20 ,targetLinePatin);
//            }else {
//                canvas.drawLine(0,radius,0,radius-20,paintLine);
//            }
//            //累计绘制过的部分
//            hasDraw += rotateAngle;
//            //继续旋转
//            canvas.rotate(rotateAngle);
//        }
        for (int i = 0; i <= 100; i++) {
            if(hasDraw <= targetAngle && targetAngle != 0){
                //计算已绘制的比例
                float percent  = hasDraw / sweepAngle;
                int red = 255 - (int) (255 * percent);
                int green = (int) (255 * percent);
                if(onAngleColorListener != null){
                    onAngleColorListener.colorListener(red,green);
                }

                targetLinePatin.setARGB(255,red,green,0);
                canvas.drawLine(0,radius,0,radius-20,targetLinePatin);
            }else {
                canvas.drawLine(0,radius,0,radius - 20,paintLine);
            }
            hasDraw += rotateAngle;
            canvas.rotate(rotateAngle);

        }
        //操作完成后要回复状态
        canvas.restore();
    }


    public void changeAngle(final float trueAngle){
        //如果在动，直接返回
        if (isRunning) {
            return;
        }
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                switch (state){
                    //后退状态
                    case 1:
                        isRunning =true;
                        targetAngle -= 3;
                        //如果回退到0
                        if(targetAngle <= 0){
                            targetAngle = 0;
                            //改为前进状态
                            state =2;
                        }
                        break;
                    //前进状态
                    case 2:
                        targetAngle += 3;
                        //如果增加到指定角度
                        if(targetAngle >= trueAngle){
                            targetAngle = trueAngle;
                            //改为后退状态
                            state = 1;
                            isRunning = false;
                            //结束本次运动
                            executorService.shutdown();
                        }

                        break;
                    default:break;
                }
                //重绘view（子线程中使用的方法）
                postInvalidate();
            }
        }, 500, 30, TimeUnit.MILLISECONDS);
    }

    public interface OnAngleColorListener {
        void colorListener(int red ,int green);
    }

    public void setOnAngleColorListener(OnAngleColorListener onAngleColorListener) {
        this.onAngleColorListener = onAngleColorListener;
    }
}
