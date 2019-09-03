package com.hxj.motioneventlib;

public class Activity {

    public static void main(String[] args) {
        // 构造一个跟布局视图
        MyViewGroup container = new MyViewGroup(0, 0, 1080, 1920);
        container.name = "container 容器";

        MyViewGroup parentA = new MyViewGroup(0, 0, 500, 500);
        parentA.name = "容器A";

        MyView myView = new MyView(0, 0, 300, 300);
        myView.name = "子视图1";

        parentA.addView(myView);
        container.addView(parentA);

        myView.setOnTouchListener(new MyView.OnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent event) {
                System.out.println("myView 视图的 onTouch 方法被调用.");
                return true;
            }
        });


        myView.setOnClickListener(new MyView.OnClickListener() {
            @Override
            public void onClick(MyView view) {
                System.out.println("myView 视图的 onClick 方法被调用.");
            }
        });

        MotionEvent event = new MotionEvent(30, 30, MotionEvent.ACTION_DOWN);
        container.dispatchTouchEvent(event);
    }
}
