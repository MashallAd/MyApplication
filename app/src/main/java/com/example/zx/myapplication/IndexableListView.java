package com.example.zx.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by zx on 2015/9/23.
 */
public class IndexableListView extends ListView {

    private boolean mIsFastScrollEnable = false;
    //绘制右侧的ScrollerBar
    private IndexScroller mScroller = null;
    private GestureDetector mGestureDetector;

    public IndexableListView(Context context) {
        super(context);
    }

    public IndexableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean getFastScrollEnable() {
        return mIsFastScrollEnable;
    }

    @Override
    public void setFastScrollEnabled(boolean enabled) {
        super.setFastScrollEnabled(enabled);
        mIsFastScrollEnable = enabled;
        if (mIsFastScrollEnable) {
            if (mScroller == null) {
                mScroller = new IndexScroller(getContext(), this);
            }
        } else {
            if (mScroller != null) {
                mScroller.hide();
                mScroller = null;
            }
        }
    }


    //用于绘制右侧索引条
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mScroller != null) {
            mScroller.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        //如果mScroll自己处理，返回true
        if (mScroller != null && mScroller.onTouchEvent(ev)) {
            return true;
        }
        //使用手势处理
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    //显示右侧显示条
//                    mScroller.show();
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });
        }

        mGestureDetector.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }

    //传递adapter给索引条
    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (mScroller != null) {
            mScroller.setAdapter(adapter);
        }
    }

    //横竖屏幕时索引条的改变
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mScroller != null) {
            mScroller.onSizeChanged(w, h, oldw, oldh);
        }
    }
}
