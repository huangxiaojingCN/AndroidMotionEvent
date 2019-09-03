package com.hxj.motioneventlib;


public class MotionEvent {

    public static final int ACTION_DOWN             = 0;

    public static final int ACTION_UP               = 1;

    public static final int ACTION_MOVE             = 2;

    public static final int ACTION_CANCEL           = 3;

    private int mActionMasked;

    private float x;

    private float y;

    public MotionEvent(float x, float y, int actionMasked) {
        this.x = x;
        this.y = y;
        this.mActionMasked = actionMasked;
    }

    public int getActionMasked() {
        return mActionMasked;
    }

    public void setmActionMasked(int mActionMasked) {
        this.mActionMasked = mActionMasked;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
