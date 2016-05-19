package net.dell.supperflashlight;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 警告灯界面
 * Created by dell on 2016/5/7.
 */
public class WarningLight extends FlashLight {

    public boolean startWarning=true;//true--闪烁; false--不闪烁
    public boolean stateWarning;//true:on--off; false:off--on

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 内部线程，控制不断的闪烁
     */
    class WarningLightThread extends Thread{
        @Override
        public void run() {
            startWarning=true;//启动就闪烁
            while (startWarning){
                try {
                    Thread.sleep(300);
                    mHandle.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Handler mHandle=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //不断的切换位置
            if (stateWarning) {
                warningLightOn.setImageResource(R.mipmap.warning_light_on);
                warningLightOff.setImageResource(R.mipmap.warning_light_off);
                stateWarning=false;
            }else {
                warningLightOn.setImageResource(R.mipmap.warning_light_off);
                warningLightOff.setImageResource(R.mipmap.warning_light_on);
                stateWarning=true;
            }
        }
    };
}
