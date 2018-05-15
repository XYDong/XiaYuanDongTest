package xyd.com.xiayuandongtest.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import xyd.com.xiayuandongtest.R;
import xyd.com.xiayuandongtest.entity.StepBean;

public class StepView extends View {

    private Path mPath;
    private List<StepBean> mStepBeanList ;//当前有几部流程    there are currently few step
    private DashPathEffect mEffects;
    private Paint mUnCompletedPaint;//未完成Paint  definition mUnCompletedPaint
    private Paint mCompletedPaint;//完成paint      definition mCompletedPaint
    private int mUnCompletedLineColor = ContextCompat.getColor(getContext(), R.color.uncompleted_color);//定义默认未完成线的颜色  definition
    private int mCompletedLineColor = Color.WHITE;//定义默认完成线的颜色      definition mCompletedLineColor

    //定义默认的高度   definition default height
    private int defaultStepIndicatorNum = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
    private float mCompletedLineHeight;//完成线的高度     definition completed line height
    private float mCircleRadius;//圆的半径  definition circle radius
    private float mLinePadding;//两条连线之间的间距  definition the spacing between the two circles


    private Drawable mCompleteIcon;//完成的默认图片    definition default completed icon
    private Drawable mAttentionIcon;//正在进行的默认图片     definition default underway icon
    private Drawable mDefaultIcon;//默认的背景图  definition default unCompleted icon

    private float mCenterY;//该view的Y轴中间位置     definition view centerY position
    private float mLeftY;//左上方的Y位置  definition rectangle LeftY position
    private float mRightY;//右下方的位置  definition rectangle RightY position

    /**
     * 得到所有圆点所在的位置
     *
     * @return
     */
    public List<Float> getmCircleCenterPointPositionList() {
        return mCircleCenterPointPositionList;
    }

    public void setmCircleCenterPointPositionList(List<Float> mCircleCenterPointPositionList) {
        this.mCircleCenterPointPositionList = mCircleCenterPointPositionList;
    }

    private List<Float> mCircleCenterPointPositionList;//定义所有圆的圆心点位置的集合 definition all of circles center point list

    private int mStepNum = 0;
    private int screenWidth;//this screen width
    private int mComplectingPosition;//正在进行position   underway position

    private OnDrawIndicatorListener mOnDrawListener;


    /**
     * 设置监听
     *
     * @param onDrawListener
     */
    public void setOnDrawListener(OnDrawIndicatorListener onDrawListener)
    {
        mOnDrawListener = onDrawListener;
    }
    public StepView(Context context) {
        super(context);
        init();

    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {

        mStepBeanList = new ArrayList<>();
        mPath = new Path();
        mEffects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);

        mCircleCenterPointPositionList = new ArrayList<>();//初始化


        //未完成画笔
        mUnCompletedPaint = new Paint();
        mUnCompletedPaint.setAntiAlias(true);
        mUnCompletedPaint.setStyle(Paint.Style.STROKE);
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mUnCompletedPaint.setStrokeWidth(2);
        mUnCompletedPaint.setPathEffect(mEffects);

        mCompletedPaint = new Paint();
        mCompletedPaint.setAntiAlias(true);
        mCompletedPaint.setStyle(Paint.Style.STROKE);
        mCompletedPaint.setColor(mCompletedLineColor);
        mCompletedPaint.setStrokeWidth(2);
        mCompletedPaint.setStyle(Paint.Style.FILL);

        //已经完成线的宽高 set mCompletedLineHeight
        mCompletedLineHeight = 0.05f * defaultStepIndicatorNum;
        //圆的半径  set mCircleRadius
        mCircleRadius = 0.28f * defaultStepIndicatorNum;
        //线与线之间的间距    set mLinePadding
        mLinePadding = 0.85f * defaultStepIndicatorNum;
        mCompleteIcon = ContextCompat.getDrawable(getContext(), R.mipmap.complted);//已经完成的icon
        mAttentionIcon = ContextCompat.getDrawable(getContext(), R.mipmap.attention);//正在进行的icon
        mDefaultIcon = ContextCompat.getDrawable(getContext(), R.mipmap.default_icon);//未完成的icon
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = defaultStepIndicatorNum * 2;
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)){
            screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        int height = defaultStepIndicatorNum;
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)){
            height = Math.min(height,MeasureSpec.getSize(heightMeasureSpec));
        }
        width = (int) (mStepNum * mCircleRadius * 2 - (mStepNum - 1) * mLinePadding);
        super.onMeasure(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取中间的高度,目的是为了让该view绘制的线和圆在该view垂直居中   get view centerY，keep current stepview center vertical
        mCenterY  = 0.5f * getHeight();
        //获取左上方Y的位置，获取该点的意义是为了方便画矩形左上的Y位置
        mLeftY = mCenterY - (mCompletedLineHeight / 2);
        //获取右下方Y的位置，获取该点的意义是为了方便画矩形右下的Y位置
        mRightY = mCenterY + mCompletedLineHeight / 2;

        mCircleCenterPointPositionList.clear();

        for (int i = 0; i < mStepNum; i++) {
            //先计算全部最左边的padding值（getWidth()-（圆形直径+两圆之间距离）*2）
            float paddingLeft = (screenWidth - mStepNum * mCircleRadius * 2 - (mStepNum - 1) * mLinePadding) / 2;
            mCircleCenterPointPositionList.add(paddingLeft + mCircleRadius + i * mCircleRadius * 2 + i * mLinePadding);
        }

        if(mOnDrawListener != null){
            mOnDrawListener.ondrawIndicator();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mOnDrawListener != null)
        {
            mOnDrawListener.ondrawIndicator();
        }
        mUnCompletedPaint.setColor(mUnCompletedLineColor);
        mCompletedPaint.setColor(mCompletedLineColor);

        //--------------画线-----------draw line ----------------
        for (int i = 0; i < mCircleCenterPointPositionList.size(); i++) {
            //前一个ComplectedXPosition
            Float preComplectedXPosition = mCircleCenterPointPositionList.get(i);
            //后一个ComplectedXPosition
            final float afterComplectedXPosition = mCircleCenterPointPositionList.get(i + 1);

            //判断在完成之前的所有点
            if (i <= afterComplectedXPosition && mStepBeanList.get(0).getState() != StepBean.STEP_UNDO)
            {
                canvas.drawRect(preComplectedXPosition + mCircleRadius -10 ,mLeftY,afterComplectedXPosition - mCircleRadius +10,mRightY,mCompletedPaint);

            }else
            {
                mPath.moveTo(preComplectedXPosition + mCircleRadius,mCenterY);
                mPath.lineTo(afterComplectedXPosition - mCircleRadius,mCenterY);
                canvas.drawPath(mPath,mUnCompletedPaint);
            }
        }
        //-----------------------画线-------draw line-----------------------------------------------

        //-----------------------画图标-----draw icon-----------------------------------------------
        for (int i = 0; i < mCircleCenterPointPositionList.size(); i++)
        {
            Float currentComplectedXPosition = mCircleCenterPointPositionList.get(i);
            @SuppressLint("DrawAllocation") Rect rect = new Rect((int) (currentComplectedXPosition - mCircleRadius), (int) (mCenterY - mCircleRadius), (int) (currentComplectedXPosition + mCircleRadius), (int) (mCenterY + mCircleRadius));
            StepBean stepBean = mStepBeanList.get(i);

            switch (stepBean.getState()) {
                case StepBean.STEP_COMPLETED:
                    mCompleteIcon.setBounds(rect);
                    mCompleteIcon.draw(canvas);
                    break;
                case StepBean.STEP_CURRENT:
                    mCompletedPaint.setColor(mCompletedLineColor);
                    canvas.drawCircle(currentComplectedXPosition,mCenterY,mCircleRadius*1.1f,mCompletedPaint);
                    mAttentionIcon.setBounds(rect);
                    mAttentionIcon.draw(canvas);
                    break;
                case StepBean.STEP_UNDO:
                    mDefaultIcon.setBounds(rect);
                    mDefaultIcon.draw(canvas);
                    break;
                default:break;

            }
        }
        //-----------------------画图标-----draw icon-----------------------------------------------

    }

    /**
     * 设置流程步数
     *
     * @param stepsBeanList 流程步数
     */
    public void setStepNum(List<StepBean> stepsBeanList)
    {
        this.mStepBeanList = stepsBeanList;
        if(stepsBeanList != null && stepsBeanList.size() > 0){
            mStepNum = stepsBeanList.size();
            for (int i = 0; i < mStepNum; i++) {
                if(stepsBeanList.get(i).getState() == StepBean.STEP_COMPLETED){
                    mComplectingPosition = i;
                }
            }
        }
        requestLayout();
    }


    /**
     * 设置未完成线的颜色
     *
     * @param unCompletedLineColor
     */
    public void setUnCompletedLineColor(int unCompletedLineColor)
    {
        this.mUnCompletedLineColor = unCompletedLineColor;
    }

    /**
     * 设置已完成线的颜色
     *
     * @param completedLineColor
     */
    public void setCompletedLineColor(int completedLineColor)
    {
        this.mCompletedLineColor = completedLineColor;
    }

    /**
     * 设置默认图片
     *
     * @param defaultIcon
     */
    public void setDefaultIcon(Drawable defaultIcon)
    {
        this.mDefaultIcon = defaultIcon;
    }

    /**
     * 设置已完成图片
     *
     * @param completeIcon
     */
    public void setCompleteIcon(Drawable completeIcon)
    {
        this.mCompleteIcon = completeIcon;
    }

    /**
     * 设置正在进行中的图片
     *
     * @param attentionIcon
     */
    public void setAttentionIcon(Drawable attentionIcon)
    {
        this.mAttentionIcon = attentionIcon;
    }


    /**
     * 设置对view监听
     */
    public interface OnDrawIndicatorListener
    {
        void ondrawIndicator();
    }
}
