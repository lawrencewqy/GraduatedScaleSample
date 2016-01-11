package com.lawrencewqy.graduatedscale;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by lawrence on 16/1/5.
 */
public class ScaleView extends ViewGroup {


    public static int dm2px(int dm){
        return (int) (Resources.getSystem().getDisplayMetrics().density * dm);
    }

    public static final int DEFAULT_BOTTOMLINE_STROKE = dm2px(5);

    public static final int DEFAULT_LINE_STROKE = dm2px(5);
    public static final int DEFAULT_LINE_HEIGHT = dm2px(25);

    public static final int DEFAULT_CURSOR_WIDTH = dm2px(10);
    public static final int DEFAULT_CURSOR_HEIGHT = dm2px(25);

    public static final int DEFAULT_LINE_SPACE = dm2px(25);
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

    ViewDragHelper mViewDragHelper;



    public ScaleView(Context context) {
        this(context,null);
    }

    public ScaleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCursorView = getChildAt(0);
        if(mCursorView == null) {
            mCursorView = new View(getContext());
            LayoutParams params = new LayoutParams(mCursorWidth,mCursorHeight);
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
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (mLineStrokeWidth + mLineSpace) * mLineCount + mLineStrokeWidth +mCursorWidth+ getPaddingLeft() + getPaddingRight();
        int height = (mLineHeight + mBottomStrokeWidth + mCursorHeight) + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width,height);
        Log.d("ScaleView","width = "+width+"height = "+height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(mCursorView != null){
            mCursorView.layout(0,getHeight()-mCursorHeight,mCursorWidth,getHeight());
        }
    }

    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setWillNotDraw(false);
        //in ViewGroup if you override onDraw , you should setWillNotDraw(false)
        mViewDragHelper = ViewDragHelper.create(this,1.0f,mCallback);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ScaleView);


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
        else
            mHighLightColor = Color.RED;

        mBottomStrokeWidth = a.getDimensionPixelSize(R.styleable.ScaleView_scaleBottomStrokeWidth, DEFAULT_BOTTOMLINE_STROKE);

        mLineHeight = a.getDimensionPixelSize(R.styleable.ScaleView_scaleLineHeight,DEFAULT_LINE_HEIGHT);
        mLineCount = a.getInt(R.styleable.ScaleView_scaleLineCount,DEFAULT_LINE_COUNT);
        mLineSpace = a.getDimensionPixelSize(R.styleable.ScaleView_scaleLineSpace,DEFAULT_LINE_SPACE);
        mLineStrokeWidth = a.getDimensionPixelSize(R.styleable.ScaleView_scaleLineStrokeWidth, DEFAULT_LINE_STROKE);

        mCursorWidth = a.getDimensionPixelSize(R.styleable.ScaleView_scaleCursorWidth,DEFAULT_CURSOR_WIDTH);
        mCursorHeight = a.getDimensionPixelSize(R.styleable.ScaleView_scaleCursorHeight,DEFAULT_CURSOR_HEIGHT);



        mCursorDrawable = a.getDrawable(R.styleable.ScaleView_scaleCursorImage);
        if(mCursorDrawable != null) {
            mCursorWidth = mCursorDrawable.getIntrinsicWidth();
            mCursorHeight = mCursorDrawable.getIntrinsicHeight();
        }
        a.recycle();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mViewDragHelper.cancel();
            return false;
        }
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    int currentPos = 0;

    ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mCursorView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.d("ScaleView","left = "+left);
            final int leftBound = getPaddingLeft();
            final int rightBound = getWidth() - mCursorView.getWidth();
            return Math.min(Math.max(left, leftBound), rightBound);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return getHeight()-mCursorHeight;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            int currpos = Math.round(left*1f/(mLineSpace+mLineStrokeWidth));
            Log.d("ScaleView","left = "+left+"currpos = "+currpos);
            if(currentPos != currpos){
                currentPos = currpos;
                invalidate();
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            int left = getPaddingLeft() + currentPos*(mLineSpace + mLineStrokeWidth)+mLineStrokeWidth/2;
            int top = getHeight()-mCursorHeight;
            mViewDragHelper.smoothSlideViewTo(mCursorView,left,top);
            ViewCompat.postInvalidateOnAnimation(ScaleView.this);
            Toast.makeText(getContext(),"current position is "+currentPos,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
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
            int left = getPaddingLeft() + (i*(mLineSpace+mLineStrokeWidth))+mLineStrokeWidth/2+mCursorWidth/2;
            int top = getPaddingRight() + mLineHeight;
            if(currentPos == i)
                getLinePaint().setColor(mHighLightColor);
            else
                getLinePaint().setColor(mLineColor);
            canvas.drawLine(left, top,left,getPaddingTop(), getLinePaint());
        }
        canvas.drawLine(getPaddingLeft()+mCursorWidth/2,getPaddingTop()+mLineHeight,getWidth()-mCursorWidth/2,getPaddingTop()+mLineHeight,getBottomPaint());
    }
}
