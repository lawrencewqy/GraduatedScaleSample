package com.lawrencewqy.graduatedscale;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by lawrence on 16/1/5.
 */
public class ScaleView extends FrameLayout {

    public static final int DEFAULT_BOTTOMLINE_STROKE = 10;

    public static final int DEFAULT_LINE_STROKE = 5;
    public static final int DEFAULT_LINE_HEIGHT = 50;

    public static final int DEFAULT_CURSOR_WIDTH = 5;
    public static final int DEFAULT_CURSOR_HEIGHT = 50;

    public static final int DEFAULT_LINE_SPACE = 50;
    public static final int DEFAULT_LINE_COUNT = 10;

    ColorStateList mCursorColorList;
    ColorStateList mLineColorList;
    ColorStateList mHighLightColorList;

    Drawable mCursorDrawable;

    int mCursorColor = -1;
    int mLineColor = -1;
    int mHighLightColor = -1;

    int mBottomStrokeWidth;

    int mLineHeight;
    int mLineStrokeWidth;

    int mLineCount;
    int mLineSpace;

    int mCursorWidth;
    int mCursorHeight;

    View mCursorView;

    public ScaleView(Context context) {
        this(context,null);
    }

    public ScaleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        post(new Runnable() {
            @Override
            public void run() {
                mCursorView = findViewById(R.id.scale_cursor);
                if(mCursorView == null) {
                    mCursorView = new View(getContext());
                    LayoutParams params = new LayoutParams(mCursorWidth,mCursorHeight);
                    params.topMargin = getMeasuredHeight() - mCursorHeight;
                    params.leftMargin = mCursorHeight;
                    mCursorView.setLayoutParams(params);
                    if(mCursorDrawable != null){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mCursorView.setBackground(mCursorDrawable);
                        }else{
                            mCursorView.setBackgroundDrawable(mCursorDrawable);
                        }
                    }else{
                        if(mCursorColor != -1)
                            mCursorView.setBackgroundColor(mCursorColor);
                    }
                    addView(mCursorView);
                }else{
                    FrameLayout.LayoutParams params = (LayoutParams) mCursorView.getLayoutParams();
                    params.topMargin = getHeight() - params.height;
                }

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (mLineStrokeWidth + mLineSpace) * mLineCount + mLineStrokeWidth + getPaddingLeft() + getPaddingRight();
        int height = (mLineHeight + mBottomStrokeWidth + mCursorHeight) + getPaddingTop() + getPaddingBottom();
        if(mCursorDrawable != null){
            width = width+mCursorDrawable.getIntrinsicWidth();
            height = mLineHeight + mBottomStrokeWidth + mCursorDrawable.getIntrinsicHeight() + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(width,height);
        Log.d("ScaleView","width = "+width+"height = "+height);
//        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //if you override onDraw , you should setWillNotDraw(false)
        this.setWillNotDraw(false);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.SearchView,defStyleAttr,0);
        mLineColorList = a.getColorStateList(R.styleable.ScaleView_scaleLineColor);
        mCursorColorList = a.getColorStateList(R.styleable.ScaleView_scaleCursorColor);
        mHighLightColorList = a.getColorStateList(R.styleable.ScaleView_scaleHighLightColor);
        if(mLineColorList != null)
            mLineColor = mLineColorList.getColorForState(getDrawableState(),0);
        else
            mLineColor = Color.BLACK;
        if(mCursorColorList != null)
            mCursorColor = mCursorColorList.getColorForState(getDrawableState(),0);
        else
            mCursorColor = Color.BLUE;
        if(mHighLightColorList != null)
            mHighLightColor = mHighLightColorList.getColorForState(getDrawableState(),0);

        mBottomStrokeWidth = a.getDimensionPixelSize(R.styleable.ScaleView_scaleBottomStrokeWidth, DEFAULT_BOTTOMLINE_STROKE);

        mLineHeight = a.getDimensionPixelSize(R.styleable.ScaleView_scaleLineHeight,DEFAULT_LINE_HEIGHT);
        mLineStrokeWidth = a.getDimensionPixelSize(R.styleable.ScaleView_scaleLineStrokeWidth, DEFAULT_LINE_STROKE);

        mCursorWidth = a.getDimensionPixelSize(R.styleable.ScaleView_scaleCursorWidth,DEFAULT_CURSOR_WIDTH);
        mCursorHeight = a.getDimensionPixelSize(R.styleable.ScaleView_scaleCursorHeight,DEFAULT_CURSOR_HEIGHT);

        mLineCount = a.getInt(R.styleable.ScaleView_scaleLineCount,DEFAULT_LINE_COUNT);
        mLineSpace = a.getDimensionPixelSize(R.styleable.ScaleView_scaleLineSpace,DEFAULT_LINE_SPACE);

        mCursorDrawable = a.getDrawable(R.styleable.ScaleView_scaleCursorImage);
        a.recycle();
    }

    Paint mLinePaint;
    public Paint getLinePaint() {
        if(mLinePaint == null){
            mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLinePaint.setStrokeWidth(mLineStrokeWidth);
            if(mLineColor != -1)
                mLinePaint.setColor(mLineColor);
        }
        return mLinePaint;
    }

    Paint mBottomPaint;

    public Paint getBottomPaint() {
        if(mBottomPaint == null){
            mBottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBottomPaint.setStrokeWidth(mBottomStrokeWidth);
            if(mLineColor != -1)
                mBottomPaint.setColor(mLineColor);
        }
        return mBottomPaint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i =0;i<=mLineCount;i++){
            int left = getPaddingLeft() + (i*(mLineSpace+mLineStrokeWidth))+mLineStrokeWidth/2;
            int top = getPaddingRight() + mLineHeight;
            canvas.drawLine(left, top,left,getPaddingTop(), getLinePaint());
        }
        canvas.drawLine(getPaddingLeft(),getPaddingTop()+mLineHeight,getWidth(),getPaddingTop()+mLineHeight,getBottomPaint());
    }
}
