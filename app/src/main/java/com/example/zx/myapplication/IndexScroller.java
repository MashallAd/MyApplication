package com.example.zx.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

/**
 * Created by zx on 2015/9/23.
 */
public class IndexScroller {

    private float mIndexbarWidth;
    private float mIndexbarMargin;  // 距离右侧的距离
    private float mPreviewPadding;  // 在中心显示的文本padding
    private float mDensity;         // 当前屏幕和标准屏幕密度比，dp的比值
    private float mScaledDensity;   // sp的比值
    private float mAlphaRate = 1;       // 透明度（用于显示和隐藏）
    private int mState = STATE_HIDDEN;
    private int mListViewWidth;
    private int mListViewHeight;
    private int mCurrentSection = -1;
    private boolean mIsIndexing = false;
    private ListView mListView;
    private SectionIndexer mSectionIndexer;
    private String[] mSections;
    private RectF mIndexbarRect;
    private float sectionHeight;

    private static final int STATE_HIDDEN = 0;
    private static final int STATE_SHOWING = 1;
    private static final int STATE_SHOWN = 2;
    private static final int STATE_HIDING = 3;

    // 索引条的初始化及尺寸本地化
    public IndexScroller(Context context, ListView listView) {

        mDensity = context.getResources().getDisplayMetrics().density;
        mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        this.mListView = listView;
        setAdapter(mListView.getAdapter());
        // 根据屏幕密度计算索引条宽度
        mIndexbarWidth = mDensity * 20;
        mIndexbarMargin = mDensity * 10;
        mPreviewPadding = mDensity * 5;
    }

    public void hide() {

    }

    public void draw(Canvas canvas) {

        // 1.   绘制索引条背景和文本
        // 2.   绘制预览文本和背景

        // 如果索引条隐藏，则不绘制
        /*
        if (mState == STATE_HIDDEN) {
            return;
        }
           */

        // 设置索引条背景
        Paint indexbarPaint = new Paint();
        indexbarPaint.setColor(Color.BLACK);
        indexbarPaint.setAlpha((int) (64 * mAlphaRate));

        // 绘制索引条
        canvas.drawRoundRect(mIndexbarRect, 5 * mDensity, 5 * mScaledDensity, indexbarPaint);

        if (mSections != null && mSections.length > 0) {
            // 绘制预览文本和背景
            if (mCurrentSection >= 0) {
                Paint previewPaint = new Paint();
                previewPaint.setColor(Color.BLACK);
                previewPaint.setAlpha(96);

                Paint previewTextPaint = new Paint();
                previewTextPaint.setColor(Color.WHITE);
                previewTextPaint.setAntiAlias(true); // 设置抗锯齿
                previewTextPaint.setTextSize(50 * mScaledDensity);
                float previewTextWidth = previewTextPaint.measureText(mSections[mCurrentSection]);
                // 预览文本和背景的大小
                float previewSize = 2 * mPreviewPadding + previewTextPaint.descent() - previewTextPaint.ascent();
                RectF previewRect = new RectF((mListViewWidth - previewSize) / 2, (mListViewHeight - previewSize) / 2,
                        (mListViewWidth - previewSize) / 2 + previewSize, (mListViewHeight - previewSize) / 2 + previewSize);
                // 预览背景
                canvas.drawRoundRect(previewRect, 5 * mDensity, 5 * mScaledDensity, previewPaint);
                // 预览文本
                canvas.drawText(mSections[mCurrentSection], previewRect.left + (previewSize - previewTextWidth) / 2 - 1,
                        previewRect.top + mPreviewPadding - previewTextPaint.ascent() - 1, previewTextPaint);

//                Log.i("info", "previewSize = " + previewSize + "; previewTextWidth = " + previewTextWidth + "; previewRect.left = " + previewRect.left);
            }

            // 绘制右侧索引条字母
            Paint indexPaint = new Paint();
            indexPaint.setColor(Color.WHITE);
            indexPaint.setAlpha((int)(255 * mAlphaRate));
            indexPaint.setAntiAlias(true);
            indexPaint.setTextSize(12 * mScaledDensity);


            sectionHeight = (mListViewHeight - 2 * mIndexbarMargin) / mSections.length;
            float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2;
            for (int i = 0; i < mSections.length; i++) {
                float paddingLeft = (mIndexbarWidth - indexPaint.measureText(mSections[i])) / 2;
                canvas.drawText(mSections[i], mIndexbarRect.left + paddingLeft, mIndexbarRect.top + i * sectionHeight + paddingTop + mIndexbarMargin, indexPaint);
            }
        }


    }


    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        mListViewWidth = w;
        mListViewHeight = h;
        mIndexbarRect = new RectF(w - mIndexbarMargin - mIndexbarWidth,
                mIndexbarMargin, w - mIndexbarMargin, h - mIndexbarMargin);
    }

    /**
     * 管理触摸索引条事件
     * @param ev
     * @return
     */
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 判断是否点击到了右侧的索引栏
//                if (mState != STATE_HIDDEN && contains(ev.getX(), ev.getY())) {
                if (contains(ev.getX(), ev.getY())) {
//                    setState(STATE_SHOWN);

                    mIsIndexing = true;
                    // 返回当前点击所在的索引栏
                    mCurrentSection = getSectionByPoint(ev.getY());
                    // 将ListView定位到当前的Section索引
                    mListView.setSelection(mSectionIndexer.getPositionForSection(mCurrentSection));
                    return true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mIsIndexing && contains(ev.getX(), ev.getY())) {

                    // 返回当前点击所在的索引栏
                    mCurrentSection = getSectionByPoint(ev.getY());
                    // 将ListView定位到当前的Section索引
                    mListView.setSelection(mSectionIndexer.getPositionForSection(mCurrentSection));
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mIsIndexing) {
                    mIsIndexing = false;
                    mCurrentSection = -1;
                }
                /*
                if (mState == STATE_SHOWN) {
                    Log.i("info", "-------state shown");
                    setState(STATE_HIDING);
                }
                */
                break;
        }
        return false;
    }

    /**
     * 通过点击的左边返回点击的索引
     * @param y 坐标
     * @return  返回mSections中的索引
     */
    private int getSectionByPoint(float y) {

        int loc = (int) ((y - mIndexbarMargin) / sectionHeight);
//        Log.i("info", "------y = " + y + ";;;mIndexbarMargin = " + mIndexbarMargin + ";;;mListView.height = " + mListViewHeight);
        return loc;
    }

    /**
     * 通过x, y的位置，判断是否点击在了索引栏中
     * @param x
     * @param y
     * @return
     */
    private boolean contains(float x, float y) {
        if (x >= mIndexbarRect.left && x <= mIndexbarRect.right && y >= mIndexbarRect.top && y <= mIndexbarRect.bottom) {
            return true;
        }
        return false;
    }

    public void show() {
        setState(STATE_SHOWING);
    }

    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof SectionIndexer) {
            mSectionIndexer = (SectionIndexer) adapter;
            mSections = (String[]) mSectionIndexer.getSections();
        }
    }

    private void fade(long delay) {
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, delay);
//        mHandler.sendEmptyMessageAtTime(0, System.currentTimeMillis() + delay);
    }

    private void setState(int state) {
        mState = state;
        switch (mState) {
            case STATE_HIDDEN:
                mHandler.removeMessages(0);
                break;
            case STATE_HIDING:
                mAlphaRate = 0;
                fade(5000);
                break;
            case STATE_SHOWING:
                mAlphaRate = 0;
                fade(0);
                break;
            case STATE_SHOWN:
                mHandler.removeMessages(0);
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (mState) {
                case STATE_HIDING:
                    mAlphaRate -= (1 - mAlphaRate) * 0.2;
                    if (mAlphaRate < 0.1) {
                        mAlphaRate = 0;
                        setState(STATE_HIDDEN);
                    }
                    // 当透明度未到达0.9时，会一直调用draw方法
                    mListView.invalidate();
                    fade(10);
                    break;
                case STATE_SHOWING:
                    mAlphaRate += (1 - mAlphaRate) * 0.2;
                    if (mAlphaRate > 0.9) {
                        mAlphaRate = 1;
                        setState(STATE_SHOWN);
                    }
                    // 当透明度未到达0.9时，会一直调用draw方法
                    mListView.invalidate();
                    fade(10);
                    break;
                case STATE_SHOWN:
                    setState(STATE_HIDING);
                    break;

                default:
                    break;
            }
        }

    };



}
