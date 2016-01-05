package com.lawrencewqy.graduatedscale;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lawrence on 16/1/5.
 */
public class ScaleView extends View {

    ColorStateList mCursorColorList;
    ColorStateList mLineColorList;
    ColorStateList mHighLightColorList;

    int mCursorColor;
    int mLineColor;
    int mHighLightColor;

    int mBottomLineHeight;

    int mLineHeight;
    int mLineWidth;

    int mCursorWidth;
    int mCursorHeight;

    Drawable mCursorDrawable;

    public ScaleView(Context context) {
        this(context,null);
    }

    public ScaleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
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

        mBottomLineHeight = a.getDimensionPixelSize(R.styleable.ScaleView_scaleBottomLineHeight,1);

        mLineHeight = a.getDimensionPixelSize(R.styleable.ScaleView_scaleLineHeight,10);
        mLineWidth = a.getDimensionPixelSize(R.styleable.ScaleView_scaleLineWidth,1);

        mCursorWidth = a.getDimensionPixelSize(R.styleable.ScaleView_scaleCursorWidth,1);
        mCursorHeight = a.getDimensionPixelSize(R.styleable.ScaleView_scaleCursorHeight,10);

        mCursorDrawable = a.getDrawable(R.styleable.ScaleView_scaleCursorImage);
        a.recycle();
    }



}
