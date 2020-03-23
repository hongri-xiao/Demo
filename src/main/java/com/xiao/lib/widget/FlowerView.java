package com.xiao.lib.widget;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;

public class FlowerView extends View {

    private TextPaint mPaint;

    private int strokeWidth = 1;
    private int textSize = 12;

    private int minContentSize = 0;
    private Path mPath;
    private int mPetalNumbers = 7;
    private float degree;
    private int defaultPetalColor = 0xFFFF1493;
    private int[] colorSet = new int[]{0xffFF1493,0xffFFD700,0xffFFFF00,0xff87CEFA,0xff00FA9A,0xffBA55D3,0xffE0FFFF};

    private boolean isPlaying = false;

    public FlowerView(Context context) {
        this(context,null);
    }

    public FlowerView(Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlowerView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        initPaint();
        minContentSize =  ViewConfiguration.get(context).getScaledTouchSlop() * 2;
        mPath = new Path();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    private void initPaint() {
        // 实例化画笔并打开抗锯齿
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG );
        mPaint.setAntiAlias(true);
        mPaint.setPathEffect(new CornerPathEffect(10)); //设置线条类型
        mPaint.setStrokeWidth(dip2px(strokeWidth));
        mPaint.setTextSize(dip2px((textSize)));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode!=MeasureSpec.EXACTLY){
            width = (int) dip2px(210);
        }
        if(heightMode!=MeasureSpec.EXACTLY){
            height = (int) dip2px(210);
        }
        setMeasuredDimension(width,height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int width  = getWidth();
        final int height = getHeight();

        if(width<minContentSize || height<minContentSize) return;
        int contentSize  =  Math.min(width,height);  //取最小边长，防止画出边界


        clearCanvas(canvas);

        canvas.drawColor(0xffffffff);

        int centerX = width/2;
        int centerY = height/2;

        final int restoreId = canvas.save();
        canvas.translate(centerX,centerY);  //将坐标系移到中心
        mPaint.setStyle(Paint.Style.STROKE);
        drawFlower(canvas,contentSize);
        canvas.restoreToCount(restoreId);

    }

    public void setPetalNumber(int num,int[] colorSet){
        this.mPetalNumbers = num;
        this.colorSet = colorSet;
        invalidate();
    }

    ValueAnimator animator  = null;
    public void startRotate(){
        stopRotate();
        isPlaying = true;
        if(animator==null) {
            animator = ValueAnimator.ofFloat(0, 360);
            animator.setEvaluator(new TypeEvaluator<Float>() {
                @Override
                public Float evaluate(float fraction, Float startValue, Float endValue) {
                    return new Float(fraction * 360);
                }
            });
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(300)
                    .setRepeatMode(ValueAnimator.RESTART);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setDegree((Float) animation.getAnimatedValue());

                }
            });
        }
        animator.start();
    }



    public void stopRotate(){
        isPlaying = false;
        if(animator!=null){
            animator.cancel();
            animator = null;
        }
    }

    private void drawFlower(Canvas canvas, int contentSize) {
        int N = this.mPetalNumbers;
        for (int i=0;i< N;i++){
            drawFlowerPath(canvas,contentSize,N,i);
        }
    }

//花瓣算法

    private void drawFlowerPath(Canvas canvas, int contentSize,int N,int pos) {

        float perDegree = 360f/N;

        final  float R = contentSize/2f;
        final  float degree = perDegree*pos + this.degree;

        float endY = (float) (R * Math.sin(Math.toRadians(degree)));
        float endX = (float) (R * Math.cos(Math.toRadians(degree)));;

        float firstCtlLength = (float) ((R / 2f) / Math.cos(Math.PI / N));
        float leftY = (float) ((firstCtlLength) * Math.sin(degree* Math.PI/180 - Math.PI/N));
        float leftX = (float) ((firstCtlLength) * Math.cos(degree* Math.PI/180 - Math.PI/N));

        float rightY = (float) ((firstCtlLength) * Math.sin(degree* Math.PI/180 + Math.PI/N));
        float rightX = (float) ((firstCtlLength) * Math.cos(degree* Math.PI/180 + Math.PI/N));

        float topLeftY = leftY + (float) ( R/2f * Math.sin(degree* Math.PI/180));  //左侧第二控制点
        float topLeftX = leftX + (float)( R/2f * Math.cos(degree* Math.PI/180)) ;

        float topRightY = rightY + (float) ( R/2f * Math.sin(degree* Math.PI/180));  //右侧第二控制点
        float topRightX = rightX + (float)( R/2f * Math.cos(degree* Math.PI/180)) ;

        mPath.reset();
        mPath.moveTo(0,0);

        mPath.cubicTo(leftX,leftY,topLeftX,topLeftY,endX,endY);
        mPath.cubicTo(topRightX,topRightY,rightX,rightY,0,0);
        mPath.close();
        if(colorSet==null || colorSet.length==0) {
            mPaint.setColor(0x9aFB2222);
        }else{
            mPaint.setColor(colorSet[pos%N]);
        }
        mPath.setFillType(Path.FillType.WINDING);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPath,mPaint);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath,mPaint);
    }


    private synchronized void clearCanvas(Canvas canvas) {

        final Xfermode xfermode = mPaint.getXfermode();
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(mPaint);
        mPaint.setXfermode(xfermode);
    }


    public float dip2px(int dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setDegree(float degree) {
        this.degree = degree;
        invalidate();
    }
}
