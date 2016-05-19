package net.dell.supperflashlight.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by dell on 2016/5/9.
 */
public class HideText extends TextView {

    public HideText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 0://表示隐藏
                    setVisibility(View.GONE);
                   break;
                case 1://显示
                    setVisibility(View.VISIBLE);
                    break;
                default:
                   break;
            }
        }
    };

    public void hide() {
        new TextViewThread().start();
    }

    class TextViewThread extends Thread{
        @Override
        public void run() {
            handler.sendEmptyMessage(1);
            try {
                Thread.sleep(3000);//三秒隐藏
                handler.sendEmptyMessage(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
