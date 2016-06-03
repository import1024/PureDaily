package com.melodyxxx.puredaily.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.melodyxxx.puredaily.utils.CommonUtils;
import com.melodyxxx.puredaily.utils.DensityUtils;

public class CircleView extends View {


    private Context mContext;
    private Paint mPaint;
    private int mMeasureWidth;
    private int mMeasureHeight;


    public void setColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        // color的默认值为主题色
        mPaint.setColor(CommonUtils.getThemePrimaryColor(context));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mMeasureWidth / 2, mMeasureHeight / 2, mMeasureHeight / 2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 测量宽
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        mMeasureWidth = 0;
        if (specMode == MeasureSpec.EXACTLY) {
            mMeasureWidth = specSize;
        } else {
            mMeasureWidth = DensityUtils.dp2px(mContext, 50);
            if (specMode == MeasureSpec.AT_MOST) {
                mMeasureWidth = Math.min(mMeasureWidth, specSize);
            }
        }
        // 测量高
        mMeasureHeight = 0;
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mMeasureHeight = specSize;
        } else {
            mMeasureHeight = DensityUtils.dp2px(mContext, 50);
            if (specMode == MeasureSpec.AT_MOST) {
                mMeasureHeight = Math.min(mMeasureHeight, specSize);
            }
        }
        // 设置测量的宽高
        setMeasuredDimension(mMeasureWidth, mMeasureHeight);
    }

}
