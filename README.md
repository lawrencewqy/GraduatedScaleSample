#Graduated Scale for android

this is a custom view for graduated scale in android , like like the gif below:

![](http://ww2.sinaimg.cn/bmiddle/5357de80gw1f056h6xh2dg20b60ik4qs.gif)

##it is easy to customize the scaleview:

###from layout file:
```
<com.lawrencewqy.graduatedscale.ScaleView
        android:id="@+id/scaleview"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="50dp"
        app:scaleCursorImage="@drawable/ic_pin_red"
        app:scaleLineStrokeWidth="2dp"
        app:scaleTextColor="@color/colorPrimary"
        app:scaleTextSize="15sp" />
```

###from code:

```
    mScaleview.setContentList(getScaleList());
    mScaleview.setLineStrokeWidth(linestroke);
    mScaleview.setBottomStroke(bottomlinestroke);
    mScaleview.setLineSpace(linespace);
    mScaleview.setTextSize(textSize);
    mScaleview.setCursorImageRes(R.drawable.ic_pin);
```