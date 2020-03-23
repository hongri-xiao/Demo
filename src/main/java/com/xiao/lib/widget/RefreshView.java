package com.xiao.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author 沈小建
 * @data 2018/8/8
 */
public class RefreshView extends View {

    private int mWidth;
    private int mHeight;

    private Paint mLineBlackPaint;
    private Paint mLineWhitePaint;

    /**
     * 总角度360度,每次旋转30度.
     */
    private static final int ALL_ANGLE = 360;
    private static final int EVERY_ROTATE = 15;

    int degree = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (degree++ == 24) {
                degree = 0;
            }
            invalidate();
            sendEmptyMessageDelayed(0, 100);
        }
    };


    public RefreshView(Context context) {
        this(context, null);
    }

    public RefreshView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();

        mHandler.sendEmptyMessageAtTime(0, 1000);
    }

    private void initPaint() {
        mLineBlackPaint = new Paint();
        mLineBlackPaint.setColor(Color.BLACK);
        mLineBlackPaint.setAntiAlias(true);
        mLineBlackPaint.setStyle(Paint.Style.STROKE);

        mLineWhitePaint = new Paint();
        mLineWhitePaint.setColor(Color.WHITE);
        mLineWhitePaint.setAntiAlias(true);
        mLineWhitePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mWidth / 2, mHeight / 2);

        int drawNumber = 0;
        for (int angle = 0; angle < ALL_ANGLE; angle += EVERY_ROTATE) {
            onDrawLine(angle, canvas, drawNumber++);
        }
    }

    private void onDrawLine(int angle, Canvas canvas, int drawNumber) {
        int startHeight = mHeight / 2;
        int endHeight = mHeight / 4;

        canvas.rotate(angle);

        if (drawNumber >= degree) {
            canvas.drawLine(0, -startHeight, 0, -endHeight, mLineBlackPaint);
        } else {
            canvas.drawLine(0, -startHeight, 0, -endHeight, mLineWhitePaint);
        }

        canvas.rotate(-angle);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }
}
