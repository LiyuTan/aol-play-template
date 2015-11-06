package com.aol.videonews.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

public class MyCardView extends CardView {

    public interface UpdatePositionListener {
        void onUpdatePosition(MyCardView view);
        void onResetPosition(MyCardView view);
    }

    private UpdatePositionListener mListener;

    public MyCardView(Context context) {
        super(context);
    }
    public MyCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUpdatePositionListener(UpdatePositionListener listener) {
        mListener = listener;
    }

    public void updatePosition() {
        if (mListener != null) {
            mListener.onUpdatePosition(this);
        }
    }

    public void resetPosition() {
        if (mListener != null) {
            mListener.onResetPosition(this);
        }
    }
}
