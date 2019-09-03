package com.hxj.motioneventlib;

public class MyViewGroup extends MyView {

    private MyView[] mChildren;

    private static final int ARRAY_INITIAL_CAPACITY = 12;

    private static final int ARRAY_CAPACITY_INCREMENT = 12;

    private int mChildrenCount;

    private TouchTarget mFirstTouchTarget;

    public MyViewGroup(int left, int top, int right, int bottom) {
        super(left, top, right, bottom);
        initViewGroup();
    }

    private void initViewGroup() {
        if (mChildren == null) {
            mChildren = new MyView[ARRAY_INITIAL_CAPACITY];
        }
    }

    public void addView(MyView child) {
        addView(child, 0);
    }

    public void addView(MyView child, int index) {
        if (child == null) {
            throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
        }

        addInArray(child, index);
    }

    private void addInArray(MyView child, int index) {
        MyView[] children = mChildren;
        final int count = mChildrenCount;
        final int size = children.length;
        if (index == count) {
            if (size == count) {
                mChildren = new MyView[size + ARRAY_CAPACITY_INCREMENT];
                System.arraycopy(children, 0, mChildren, 0, size);
                children = mChildren;
            }
            children[mChildrenCount++] = child;
        } else if (index < count) {
            if (size == count) {
                mChildren = new MyView[size + ARRAY_CAPACITY_INCREMENT];
                System.arraycopy(children, 0, mChildren, 0, index);
                System.arraycopy(children, index, mChildren, index + 1, count - index);
                children = mChildren;
            } else {
                System.arraycopy(children, index, children, index + 1, count - index);
            }
            children[index] = child;
            mChildrenCount++;
        } else {
            throw new IndexOutOfBoundsException("index=" + index + " count=" + count);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        System.out.println("当前视图容器的名称： " + super.name);
        int actionMasked = event.getActionMasked();
        boolean intercepted = false;

        boolean handled = false;

        if (actionMasked == MotionEvent.ACTION_DOWN) {
            intercepted = onInterceptTouchEvent(event);
        }

        boolean alreadyDispatchedToNewTouchTarget = false;
        if (!intercepted && actionMasked != MotionEvent.ACTION_CANCEL) {
            float x = event.getX();
            float y = event.getY();
            int childrenCount = mChildrenCount;
            MyView[] children = mChildren;

            for (int i = childrenCount - 1; i >= 0; i--) {
                MyView child = children[i];

                // 检查按下的坐标是否在 view 上.
                if (!isTransformedTouchPointInView(x, y, child)) {
                    System.out.println("未点击的view上： " + child.name);
                    continue;
                }

                // 将事件交给 child 来处理，由 child 决定是否消费
                TouchTarget newTouchTarget;
                if (dispatchTransformedTouchEvent(event, child)) {
                    newTouchTarget = addTouchTarget(child);
                    alreadyDispatchedToNewTouchTarget = true;
                    break;
                }
            }
        }

        // 如果没有事件被消费就会将事件交给容器来处理.
        if (mFirstTouchTarget == null) {
            handled = dispatchTransformedTouchEvent(event, null);
        } else {
            if (alreadyDispatchedToNewTouchTarget) {
                handled = true;
            }
        }

        return handled;
    }

    private TouchTarget addTouchTarget(MyView child) {
        TouchTarget target = TouchTarget.obtain(child);
        target.next = mFirstTouchTarget;
        mFirstTouchTarget = target;

        return target;
    }

    private boolean dispatchTransformedTouchEvent(MotionEvent event, MyView child) {
        boolean handled = false;

        if (child == null) {
            handled = super.dispatchTouchEvent(event);
        } else {
            handled = child.dispatchTouchEvent(event);
        }

        return handled;
    }

    /**
     * ViewGroup 是否拦截事件. 可以重写此方法返回 true 将事件进行拦截。
     *
     * @param event
     */
    public boolean onInterceptTouchEvent(MotionEvent event) {

        return false;
    }

    private boolean isTransformedTouchPointInView(float x, float y, MyView child) {
        if (x >= child.mLeft && x <= child.mRight && y >= child.mTop && y <= child.mBottom) {
            return true;
        }

        return false;
    }

    protected static final class TouchTarget {
        private static final int MAX_RECYCLED = 32;
        private static final Object sRecycleLock = new Object[0];
        private static TouchTarget sRecycleBin;
        private static int sRecycledCount;

        public MyView child;

        public TouchTarget next;

        private TouchTarget() {
        }

        public static TouchTarget obtain(MyView child) {
            if (child == null) {
                throw new IllegalArgumentException("child must be non-null");
            }

            final TouchTarget target;
            synchronized (sRecycleLock) {
                if (sRecycleBin == null) {
                    target = new TouchTarget();
                } else {
                    target = sRecycleBin;
                    sRecycleBin = target.next;
                    sRecycledCount--;
                    target.next = null;
                }
            }
            target.child = child;
            return target;
        }

        public void recycle() {
            if (child == null) {
                throw new IllegalStateException("already recycled once");
            }

            synchronized (sRecycleLock) {
                if (sRecycledCount < MAX_RECYCLED) {
                    next = sRecycleBin;
                    sRecycleBin = this;
                    sRecycledCount += 1;
                } else {
                    next = null;
                }
                child = null;
            }
        }
    }
}
