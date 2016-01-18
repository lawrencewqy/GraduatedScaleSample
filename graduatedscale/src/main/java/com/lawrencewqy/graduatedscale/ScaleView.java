package com.lawrencewqy.graduatedscale;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

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

    public static final int DEFAULT_TEXT_SIZE = dm2px(12);

    ColorStateList mCursorColorList;
    ColorStateList mLineColorList;
    ColorStateList mHighLightColorList;
    ColorStateList mTextColorList;

    Drawable mCursorDrawable;

    int mCursorColor = -1;
    int mLineColor = -1;
    int mHighLightColor = -1;
    int mTextColor = -1;

    int mTextSize;

    int mBottomStrokeWidth;

    int mLineHeight;
    int mLineStrokeWidth;

    int mLineCount;
    int mLineSpace;

    int mCursorWidth;
    int mCursorHeight;

    View mCursorView;

    ViewDragHelper mViewDragHelper;

    ArrayList<String> contentList;


    public ScaleView setContentList(ArrayList<String> contentList) {
        this.contentList = contentList;
        mLineCount = contentList.size()-1;
        requestLayout();
        return this;
    }

    public ScaleView setLineSpace(int lineSpace){
        mLineSpace = lineSpace;
        requestLayout();
        return this;
    }

    public ScaleView setTextSize(int sp){
        mTextSize = (int) (sp*Resources.getSystem().getDisplayMetrics().scaledDensity);
        getTextPaint().setTextSize(mTextSize);
        requestLayout();
        return this;
    }

    public ScaleView setLineStrokeWidth(int lineStroke){
        mLineStrokeWidth = lineStroke;
        getLinePaint().setStrokeWidth(mLineStrokeWidth);
        requestLayout();
        return this;
    }

    public ScaleView setBottomStroke(int bottomStroke){
        mBottomStrokeWidth = bottomStroke;
        getBottomPaint().setStrokeWidth(mBottomStrokeWidth);
        requestLayout();
        return this;
    }

    public ScaleView setCursorImageRes(@DrawableRes int resId){
        mCursorDrawable = new BitmapDrawable(getResources(),BitmapFactory.decodeResource(getResources(),resId));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mCursorView.setBackground(mCursorDrawable);
        }else{
            mCursorView.setBackgroundDrawable(mCursorDrawable);
        }
        if(mCursorDrawable != null) {
            mCursorWidth = mCursorDrawable.getIntrinsicWidth();
            mCursorHeight = mCursorDrawable.getIntrinsicHeight();
        }
        requestLayout();
        return this;
    }

    public ScaleView(Context context) {
        this(context,null);
    }

    public ScaleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (mLineStrokeWidth + mLineSpace) * mLineCount + mLineStrokeWidth +mCursorWidth  + getPaddingLeft() + getPaddingRight();
        int height = (mLineHeight + mBottomStrokeWidth + mCursorHeight + mTextSize) + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width,height);
        Log.d("ScaleView","width = "+width+"height = "+height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(mCursorView != null){
            int leftPos = currentPos * (mLineSpace + mLineStrokeWidth);
            mCursorView.layout(leftPos,getHeight()-mCursorHeight,leftPos + mCursorWidth,getHeight());
        }
    }

    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d("ScaleView","construct called");
        this.setWillNotDraw(false);
        //in ViewGroup if you override onDraw , you should setWillNotDraw(false)
        mViewDragHelper = ViewDragHelper.create(this,1.0f,mCallback);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ScaleView);


        mLineColorList = a.getColorStateList(R.styleable.ScaleView_scaleLineColor);
        mCursorColorList = a.getColorStateList(R.styleable.ScaleView_scaleCursorColor);
        mHighLightColorList = a.getColorStateList(R.styleable.ScaleView_scaleHighLightColor);
        mTextColorList = a.getColorStateList(R.styleable.ScaleView_scaleTextColor);
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
        if(mTextColorList != null)
            mTextColor = mTextColorList.getColorForState(getDrawableState(),0);
        else
            mTextColor = Color.RED;

        mBottomStrokeWidth = a.getDimensionPixelSize(R.styleable.ScaleView_scaleBottomStrokeWidth, DEFAULT_BOTTOMLINE_STROKE);

        mLineHeight = a.getDimensionPixelSize(R.styleable.ScaleView_scaleLineHeight,DEFAULT_LINE_HEIGHT);
        mLineCount = a.getInt(R.styleable.ScaleView_scaleLineCount,DEFAULT_LINE_COUNT);
        mLineSpace = a.getDimensionPixelSize(R.styleable.ScaleView_scaleLineSpace,DEFAULT_LINE_SPACE);
        mLineStrokeWidth = a.getDimensionPixelSize(R.styleable.ScaleView_scaleLineStrokeWidth, DEFAULT_LINE_STROKE);

        mCursorWidth = a.getDimensionPixelSize(R.styleable.ScaleView_scaleCursorWidth,DEFAULT_CURSOR_WIDTH);
        mCursorHeight = a.getDimensionPixelSize(R.styleable.ScaleView_scaleCursorHeight,DEFAULT_CURSOR_HEIGHT);

        mTextSize = a.getDimensionPixelSize(R.styleable.ScaleView_scaleTextSize,DEFAULT_TEXT_SIZE);

        mCursorDrawable = a.getDrawable(R.styleable.ScaleView_scaleCursorImage);
        if(mCursorDrawable != null) {
            mCursorWidth = mCursorDrawable.getIntrinsicWidth();
            mCursorHeight = mCursorDrawable.getIntrinsicHeight();
        }
        a.recycle();
        addCursorView();
    }

    public void addCursorView(){
        if(mCursorView == null){
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
        }
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
            Toast.makeText(getContext(),"current position is " + currentPos + " content is " + (contentList != null ? contentList.get(currentPos):String.valueOf(currentPos)),Toast.LENGTH_SHORT).show();
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

    Paint mTextPaint;
    public Paint getTextPaint(){
        if(mTextPaint == null){
            mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setColor(mTextColor);
        }
        return mTextPaint;
    }

    Rect mRect = new Rect();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i =0;i<=mLineCount;i++){
            int left = getPaddingLeft() + (i*(mLineSpace+mLineStrokeWidth))+mLineStrokeWidth/2+mCursorWidth/2;
            int top = getPaddingRight() + mLineHeight + mTextSize;
            if(currentPos == i)
                getLinePaint().setColor(mHighLightColor);
            else
                getLinePaint().setColor(mLineColor);
            canvas.drawLine(left, top,left,getPaddingTop()+mTextSize, getLinePaint());
            String content = "";
            if(contentList == null){
                content = String.valueOf(i);
            }else{
                if(contentList.size() > i)
                    content = contentList.get(i);
            }
            getTextPaint().getTextBounds(content,0,content.length(),mRect);
            canvas.drawText(content,left-mRect.exactCenterX(),getPaddingTop()+mTextSize/2 - mRect.exactCenterY(),getTextPaint());
        }
        canvas.drawLine(getPaddingLeft()+mCursorWidth/2,getPaddingTop()+mLineHeight+mTextSize,getWidth()-mCursorWidth/2,getPaddingTop()+mLineHeight+mTextSize,getBottomPaint());
    }


    static class SaveState extends BaseSavedState{

        int currentPos;
        int lineCount;
        int lineSpace;
        ArrayList<String> contentList;
        int cursorColor;
        int lineColor;
        int highLightColor;
        int textColor;

        int textSize;

        int bottomStrokeWidth;

        int lineHeight;
        int lineStrokeWidth;

        public SaveState(Parcelable superState) {
            super(superState);
        }

        public SaveState(Parcel source) {
            super(source);
            currentPos = source.readInt();
            source.readList(contentList,String.class.getClassLoader());
            lineCount = source.readInt();
            lineSpace = source.readInt();
            cursorColor = source.readInt();
            highLightColor = source.readInt();
            lineColor = source.readInt();
            textColor = source.readInt();
            textSize = source.readInt();
            bottomStrokeWidth = source.readInt();
            lineHeight = source.readInt();
            lineStrokeWidth = source.readInt();

        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentPos);
            out.writeList(contentList);
            out.writeInt(lineCount);
            out.writeInt(lineSpace);
            out.writeInt(cursorColor);
            out.writeInt(highLightColor);
            out.writeInt(lineColor);
            out.writeInt(textColor);
            out.writeInt(textSize);
            out.writeInt(bottomStrokeWidth);
            out.writeInt(lineHeight);
            out.writeInt(lineStrokeWidth);
        }

        public static final Parcelable.Creator<SaveState> CREATOR = new Parcelable.Creator<SaveState>(){
            @Override
            public SaveState createFromParcel(Parcel source) {
                return new SaveState(source);
            }

            @Override
            public SaveState[] newArray(int size) {
                return new SaveState[size];
            }
        };
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d("ScaleView","onSaveInstanceState");
        Parcelable parcelable = super.onSaveInstanceState();
        SaveState saveState = new SaveState(parcelable);
        saveState.currentPos = currentPos;
        saveState.contentList = contentList;
        saveState.lineCount = mLineCount;
        saveState.lineSpace = mLineSpace;
        saveState.cursorColor = mCursorColor;
        saveState.highLightColor = mHighLightColor;
        saveState.lineColor = mLineColor;
        saveState.textColor = mTextColor;
        saveState.textSize = mTextSize;
        saveState.bottomStrokeWidth = mBottomStrokeWidth;
        saveState.lineHeight = mLineHeight;
        saveState.lineStrokeWidth = mLineStrokeWidth;
        return saveState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.d("ScaleView","onRestoreInstanceState");
        SaveState saveState = (SaveState) state;
        super.onRestoreInstanceState(saveState.getSuperState());
        currentPos = saveState.currentPos;
        contentList = saveState.contentList;
        mLineCount = saveState.lineCount;
        mLineSpace = saveState.lineSpace;
        mCursorColor = saveState.cursorColor;
        mHighLightColor = saveState.highLightColor;
        mLineColor = saveState.lineColor;
        mTextColor = saveState.textColor;
        mTextSize = saveState.textSize;
        mBottomStrokeWidth = saveState.bottomStrokeWidth;
        mLineHeight = saveState.lineHeight;
        mLineStrokeWidth = saveState.lineStrokeWidth;
    }
}
