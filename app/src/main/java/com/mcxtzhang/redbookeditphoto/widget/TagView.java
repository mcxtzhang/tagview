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

    private boolean isLeft;
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

    public void setOrientationAndPosition(boolean isLeft, Point point) {
        this.isLeft = isLeft;
        this.mLocation = point;
        updateLocation();
    }

    public void changeOrientation() {
        this.isLeft = !this.isLeft;
        updateLocation();
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
        if (isLeft) {
            lp.leftMargin = mLocation.x - getMeasuredWidth();
        } else {
            lp.leftMargin = mLocation.x;
        }
        lp.topMargin = mLocation.y - height / 2;
        setLayoutParams(lp);
    }
}
