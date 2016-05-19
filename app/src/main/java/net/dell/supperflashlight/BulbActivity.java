package net.dell.supperflashlight;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;

import net.dell.supperflashlight.view.HideText;

/**
 * 灯泡界面
 * Created by dell on 2016/5/9.
 */
public class BulbActivity extends MorseActivity {

    public boolean mBulbCrossFlag;//设置灯的状态
    public TransitionDrawable mDrawable;
    public HideText textHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textHide= (HideText) findViewById(R.id.tv_custom_hide);

        mDrawable= (TransitionDrawable) imageBulb.getDrawable();
        imageBulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //默认false是关闭的
                if (!mBulbCrossFlag) {
                    mDrawable.startTransition(500);
                    mBulbCrossFlag = true;
                    screenBrightness(1f);//点亮屏幕变亮
                }else {
                    mDrawable.reverseTransition(500);
                    mBulbCrossFlag = false;
                    screenBrightness(0f);
                }
            }
        });


    }
}
