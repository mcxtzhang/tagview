package com.mcxtzhang.redbookeditphoto.widget;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by zhangxutong on 2018/6/21.
 */

public class TagView extends android.support.v7.widget.AppCompatTextView {
    private Context mContext;

    private boolean isRight;
    private Point mLocation;

    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

    }

    public void setOrientationAndPosition(boolean isRight, Point point) {
        this.isRight = isRight;
        this.mLocation = point;
        updateLocation();
    }

    public void changeOrientation() {
        this.isRight = !this.isRight;
        updateLocation();
    }

    public boolean isRight() {
        return isRight;
    }

    public Point getLocation() {
        return mLocation;
    }

    private void updateLocation() {
        int height = getMeasuredHeight();
        if (height == 0) {
            measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            height = getMeasuredHeight();
        }
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
        if (lp == null) {
            lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (isRight) {
            lp.leftMargin = mLocation.x;
        } else {
            lp.leftMargin = mLocation.x - getMeasuredWidth();
        }
        lp.topMargin = mLocation.y - height / 2;
        setLayoutParams(lp);
    }
}
