package net.dell.supperflashlight;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import net.dell.supperflashlight.view.HideText;

/**
 * Created by dell on 2016/5/10.
 */
public class PoliceLight  extends ColorLight{

    public HideText hideTextPolice;
    public boolean mPoliceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideTextPolice = (HideText) findViewById(R.id.tv_police);

    }

    class PoliceThread extends Thread{
        @Override
        public void run() {
            mPoliceState=true;
            while (mPoliceState) {
                handler.sendEmptyMessage(Color.BLUE);
                sleepExt(200);
                handler.sendEmptyMessage(Color.BLACK);
                sleepExt(100);
                handler.sendEmptyMessage(Color.RED);
                sleepExt(200);
                handler.sendEmptyMessage(Color.BLACK);
                sleepExt(100);
            }
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int color=msg.what;
            policeLayout.setBackgroundColor(color);
        }
    };


}

