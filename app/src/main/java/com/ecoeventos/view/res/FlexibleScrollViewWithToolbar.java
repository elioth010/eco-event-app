package com.ecoeventos.view.res;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

public class FlexibleScrollViewWithToolbar extends ObservableScrollView {

	public static final String TAG = "FlexibleScrollViewWithToolbar";

    private static final int DELAY_MILLIS = 50;

    public interface OnFlingListener {
        public void onFlingStarted();
        public void onFlingStopped();
    }
    
    private OnScrollChangedListener mOnScrollChangedListener;
	private boolean mIsOverScrollEnabled = true;

	public interface OnScrollChangedListener {
		void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);
	}

    private OnFlingListener mFlingListener;
    private Runnable mScrollChecker;
    private int mPreviousPosition;

    public FlexibleScrollViewWithToolbar(Context context) {
        this(context, null, 0);
    }

    public FlexibleScrollViewWithToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexibleScrollViewWithToolbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mScrollChecker = new Runnable() {
            @Override
            public void run() {
                int position = getScrollY();
                if (mPreviousPosition - position == 0) {
                    mFlingListener.onFlingStopped();
                    removeCallbacks(mScrollChecker);
                } else {
                    mPreviousPosition = getScrollY();
                    postDelayed(mScrollChecker, DELAY_MILLIS);
                }
            }
        };
    }
    
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    	super.onScrollChanged(l, t, oldl, oldt);super.onScrollChanged(l, t, oldl, oldt);
		if (mOnScrollChangedListener != null) {
			mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
		}
	}

	public void setOnScrollChangedListener(OnScrollChangedListener listener) {
		mOnScrollChangedListener = listener;
	}
	
	public void setOverScrollEnabled(boolean enabled) {
		mIsOverScrollEnabled = enabled;
	}

	public boolean isOverScrollEnabled() {
		return mIsOverScrollEnabled;
	}

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);

        if (mFlingListener != null) {
            mFlingListener.onFlingStarted();
            post(mScrollChecker);
        }
    }

    public OnFlingListener getOnFlingListener() {
        return mFlingListener;
    }

    public void setOnFlingListener(OnFlingListener mOnFlingListener) {
        this.mFlingListener = mOnFlingListener;
    }

}
