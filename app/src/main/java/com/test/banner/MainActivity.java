package com.test.banner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

//第四次提交
public class MainActivity extends AppCompatActivity{
    AdapterViewFlipper flipper;
    LinearLayout main;
    int [] imgs;
    BaseAdapter adapter;
    Timer timer;
    TimerTask timerTask;
    int count = 0;
    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                flipper.showNext();
            }
            if (msg.what == 2){
                flipper.setVisibility(View.INVISIBLE);
                main.setVisibility(View.VISIBLE);
            }
            if(msg.what == 3){
                flipper.setVisibility(View.VISIBLE);
                main.setVisibility(View.INVISIBLE);
            }
            super.handleMessage(msg);
        }
    };


    public void InitView(){
        flipper = (AdapterViewFlipper) findViewById(R.id.viewFlip);
        main = (LinearLayout) findViewById(R.id.Main);
        imgs = new int[]{
                R.drawable.a1,R.drawable.b1,R.drawable.c1
        };
        adapter = new BaseAdapter()
        {
            @Override
            public int getCount()
            {
                return imgs.length;
            }
            @Override
            public Object getItem(int position)
            {
                return position;
            }
            @Override
            public long getItemId(int position)
            {
                return position;
            }
            // 该方法的返回的View就是代表了每个列表项
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                // 创建一个ImageView
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setImageResource(imgs[position]);
                // 设置ImageView的缩放类型
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                // 为imageView设置布局参数
                imageView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        };
        flipper.setAdapter(adapter);
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        };
    }
    private String getRunningActivityName(){
        ActivityManager activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();
        timer.schedule(timerTask,1000,5000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //2是不可见,3是可见
                while (true) {
                    if(getRunningActivityName().equals("com.test.banner.MainActivity")){
                        count ++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(count > 30){
                    Message message = new Message();
                    message.what = 3;
                    mHandler.sendMessage(message);
                    }

                }
            }
        }).start();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Message message = new Message();
        message.what = 2;
        mHandler.sendMessage(message);
        count = 0 ;
        return super.onTouchEvent(event);
    }
}
