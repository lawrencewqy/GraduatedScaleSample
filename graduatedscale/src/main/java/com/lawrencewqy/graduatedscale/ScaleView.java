package com.lawrencewqy.graduatedscale;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by lawrence on 16/1/5.
 */
public class ScaleView extends FrameLayout {

    public static final int DEFAULT_BOTTOMLINE_HEIGHT = 1;

    public static final int DEFAULT_LINE_WIDTH = 1;
    public static final int DEFAULT_LINE_HEIGHT = 10;

    public static final int DEFAULT_CURSOR_WIDTH = 1;
    public static final int DEFAULT_CURSOR_HEIGHT = 10;

    public static final int DEFAULT_LINE_SPACE = 50;
    public static final int DEFAULT_LINE_COUNT = 10;

    ColorStateList mCursorColorList;
    ColorStateList mLineColorList;
    ColorStateList mHighLightColorList;

    Drawable mCursorDrawable;

    int mCursorColor;
    int mLineColor;
    int mHighLightColor;

    int mBottomLineHeight;

    int mLineHeight;
    int mLineWidth;

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
        mCursorView = findViewById(R.id.scale_cursor);
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
                mCursorView.setBackgroundColor(mCursorColor);
            }
            addView(mCursorView);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.SearchView,defStyleAttr,0);
        mLineColorList = a.getColorStateList(R.styleable.ScaleView_scaleLineColor);
        mCursorColorList = a.getColorStateList(R.styleable.ScaleView_scaleCursorColor);
        mHighLightColorList = a.getColorStateList(R.styleable.ScaleView_scaleHighLightColor);

        mLineColor = mLineColorList.getColorForState(getDrawableState(),0);
        mCursorColor = mCursorColorList.getColorForState(getDrawableState(),0);
        mHighLightColor = mHighLightColorList.getColorForState(getDrawableState(),0);

        mBottomLineHeight = a.getDimensionPixelSize(R.styleable.ScaleView_scaleBottomLineHeight,DEFAULT_BOTTOMLINE_HEIGHT);

        mLineHeight = a.getDimensionPixelSize(R.styleable.ScaleView_scaleLineHeight,DEFAULT_LINE_HEIGHT);
        mLineWidth = a.getDimensionPixelSize(R.styleable.ScaleView_scaleLineWidth,DEFAULT_LINE_WIDTH);

        mCursorWidth = a.getDimensionPixelSize(R.styleable.ScaleView_scaleCursorWidth,DEFAULT_CURSOR_WIDTH);
        mCursorHeight = a.getDimensionPixelSize(R.styleable.ScaleView_scaleCursorHeight,DEFAULT_CURSOR_HEIGHT);

        mLineCount = a.getInt(R.styleable.ScaleView_scaleLineCount,DEFAULT_LINE_COUNT);
        mLineSpace = a.getDimensionPixelSize(R.styleable.ScaleView_scaleLineSpace,DEFAULT_LINE_SPACE);

        mCursorDrawable = a.getDrawable(R.styleable.ScaleView_scaleCursorImage);
        a.recycle();
    }



}
