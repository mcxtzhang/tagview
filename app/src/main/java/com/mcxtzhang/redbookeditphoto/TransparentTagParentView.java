package com.mcxtzhang.redbookeditphoto;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by zhangxutong on 2018/6/14.
 */

public class TransparentTagParentView extends FrameLayout {

    private Context mContext;

    public TransparentTagParentView(@NonNull Context context) {
        this(context, null);
    }

    public TransparentTagParentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransparentTagParentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setClickable(true);
        setBackgroundColor(Color.parseColor("#88000000"));
    }


    private PointF mDownPoint = new PointF();
    private PointF mLastPoint = new PointF();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastPoint.set(x, y);
                mDownPoint.set(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mLastPoint.set(x, y);
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(x - mDownPoint.x) + Math.abs(y - mDownPoint.y) < 10) {
                    Toast.makeText(getContext(), "点击", Toast.LENGTH_SHORT).show();
                    addTag(mDownPoint);
                }
                mLastPoint.set(0, 0);
                mDownPoint.set(0, 0);
                break;
        }

        return super.dispatchTouchEvent(ev);
    }


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        float x = ev.getX();
//        float y = ev.getY();
//
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mLastPoint.set(x, y);
//                mDownPoint.set(x, y);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mLastPoint.set(x, y);
//                break;
//            case MotionEvent.ACTION_UP:
//                if (Math.abs(x - mDownPoint.x) + Math.abs(y - mDownPoint.y) < 10) {
//                    Toast.makeText(getContext(), "点击", Toast.LENGTH_SHORT).show();
//                    addTag(mDownPoint);
//                }
//                mLastPoint.set(0, 0);
//                mDownPoint.set(0, 0);
//                break;
//        }
//
//        return super.onInterceptTouchEvent(ev);
//    }

    private void addTag(PointF pointF) {
        FrameLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = (int) pointF.x;
        lp.topMargin = (int) pointF.y;
        TextView tv = new TextView(mContext);
        tv.setText("Gucci");
        tv.setBackgroundColor(Color.BLUE);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "旋转", Toast.LENGTH_SHORT).show();
            }
        });

        tv.setOnTouchListener(new OnTouchListener() {

            private PointF downPoint = new PointF();
            private PointF lastPoint = new PointF();

            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                float x = ev.getRawX();
                float y = ev.getRawY();

                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downPoint.set(x, y);
                        lastPoint.set(x, y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveTouchView(v, x, y);


                        lastPoint.set(x, y);
                        break;
                    case MotionEvent.ACTION_UP:
                        moveTouchView(v, x, y);


                        downPoint.set(0, 0);
                        lastPoint.set(0, 0);

                        if (Math.abs(x - mDownPoint.x) + Math.abs(y - mDownPoint.y) < 10) {
                            return true;
                        }
                        break;
                }


                return false;
            }

            private void moveTouchView(View v, float x, float y) {
                int gapX = (int) (x - lastPoint.x);
                int gapY = (int) (y - lastPoint.y);
                //v.layout(v.getLeft() + gapX, v.getTop() + gapY, v.getLeft() + gapX + v.getWidth(), v.getTop() + gapY + getHeight());
                FrameLayout.LayoutParams lp = (LayoutParams) v.getLayoutParams();
                lp.leftMargin = lp.leftMargin + gapX;
                lp.topMargin = lp.topMargin + gapY;
                v.setLayoutParams(lp);
            }
        });

        addView(tv, lp);
    }
}
