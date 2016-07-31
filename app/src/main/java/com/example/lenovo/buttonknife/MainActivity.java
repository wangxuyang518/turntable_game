package com.example.lenovo.buttonknife;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    boolean flag=true;
    int p=0;//每跑一次，把最停止的位置记录下来。默认为0
    Handler handler;
    @InjectView(R.id.gridview1)
    GridView gridView;
    Thread thread;
    int randNum=0;
    int time=300;//初始化的时间
    int state=1;//开始默认为加速
    static final int ACCELERATE=1;
    static final int DECELERATIOnN=2;
    int number=0;//当time==0的时候，让他多转72步，也就是9圈

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        gridView.setAdapter(new MyAdapter());
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int position=msg.arg1;
                switch (position)
                {
                    case 0:
                        gridView.getChildAt(3).setSelected(false);
                        gridView.getChildAt(0).setSelected(true);
                        break;
                    case 1:
                        gridView.getChildAt(0).setSelected(false);
                        gridView.getChildAt(1).setSelected(true);
                        break;
                    case 2:
                        gridView.getChildAt(1).setSelected(false);
                        gridView.getChildAt(2).setSelected(true);
                        break;
                    case 3:
                        gridView.getChildAt(2).setSelected(false);
                        gridView.getChildAt(5).setSelected(true);
                        break;
                    case 4:
                        gridView.getChildAt(5).setSelected(false);
                        gridView.getChildAt(8).setSelected(true);
                        break;
                    case 5:
                        gridView.getChildAt(8).setSelected(false);
                        gridView.getChildAt(7).setSelected(true);
                        break;
                    case 6:
                        gridView.getChildAt(7).setSelected(false);
                        gridView.getChildAt(6).setSelected(true);
                        break;
                    case 7:
                        gridView.getChildAt(6).setSelected(false);
                        gridView.getChildAt(3).setSelected(true);
                        break;
                }

                if (time<=0&&state==1)
                {
                    if (number<=72) {
                        time = 15;
                        number++;
                        thread.run();
                    }
                    else{
                        state = 2;
                        thread.run();
                    }

                }
                else
                {
                    if (state==2&&time>=250)
                    {
                        if (position==randNum)
                        {
                            p=position;
                            time=300;
                            state=1;
                            number=0;
                            return;
                        }
                        else {
                            thread.run();
                        }
                    }
                    else
                    {
                        thread.run();
                    }
                }
            }
        };


        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (true) {
                    gridView.getChildAt(0).setSelected(true);
                }
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 4) {

                    Random rand = new Random();
                    randNum = rand.nextInt(8);//随机产生中奖的数
                    Toast.makeText(MainActivity.this, ""+randNum, Toast.LENGTH_SHORT).show();
                    //开启线程设置gridview的item
                    thread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            switch (state)
                            {
                                case ACCELERATE:
                                    Message message=new Message();
                                    p++;
                                    message.arg1=(p)%8;
                                    time=time-15;
                                    handler.sendMessageDelayed(message,time);
                                    break;
                                case DECELERATIOnN:
                                    Message message1=new Message();
                                    p++;
                                    message1.arg1=(p)%8;
                                    time=time+15;
                                    handler.sendMessageDelayed(message1,time);
                                    break;
                            }
                        }
                    });
                    thread.start();
                }
            }
        });
    }



    public class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int style=getItemViewType(position);
            if (style==1)
            {
                return View.inflate(MainActivity.this,R.layout.gridview_item1,null);
            }
            else {
                return View.inflate(MainActivity.this,R.layout.gridview_item2,null);
            }
        }
        @Override
        public int getItemViewType(int position) {
            if (position==4)
            {
                return 1;
            }
            else {
                return 2;
            }
        }
    }

}
