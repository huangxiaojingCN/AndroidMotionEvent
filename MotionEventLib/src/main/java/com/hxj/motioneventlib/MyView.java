package com.hxj.motioneventlib;

/**
 *  为了仿照安卓事件机制，这里的 MyView 类似于事件机制源码中的 View
 */
public class MyView {

    public int mLeft;

    public int mTop;

    public int mRight;

    public int mBottom;

    public String name;

    private OnTouchListener mOnTouchListener;

    private OnClickListener mClickListener;

    public MyView(int left, int top, int right, int bottom) {
        this.mLeft = left;
        this.mTop = top;
        this.mRight = right;
        this.mBottom = bottom;

    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        System.out.println("当前视图是子视图：" + name);
        boolean result = false;
        if (mOnTouchListener != null && mOnTouchListener.onTouch(event)) {
            result = true;
        }

        if (!result && onTouchEvent(event)) {
            result = true;
        }

        return result;
    }

    private boolean onTouchEvent(MotionEvent event) {
        System.out.println(name + " 的onTouchEvent方法被调用 ");
        if (mClickListener != null) {
            mClickListener.onClick(this);
            return true;
        }

        return false;
    }

    public interface OnTouchListener {
        boolean onTouch(MotionEvent event);
    }

    public interface OnClickListener {
        void onClick(MyView view);
    }

    public void setOnTouchListener(OnTouchListener l) {
        this.mOnTouchListener = l;
    }

    public void setOnClickListener(OnClickListener l) {
        this.mClickListener = l;
    }

    @Override
    public String toString() {
        return "name: " + name;
    }
}
