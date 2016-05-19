package net.dell.supperflashlight;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * 主界面
 */
public class MainActivity extends SettingActivity {

    private ImageView cutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cutButton = (ImageView) findViewById(R.id.image_cut_btn);
        cutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllui();
                if (mCurrentType != UiType.UI_TYPE_MAIN) {
                    mainLayout.setVisibility(View.VISIBLE);
                    mCurrentType = UiType.UI_TYPE_MAIN;
                    startWarning=false;//关闭警告灯闪烁
                    screenBrightness(mScreenBrightness/255f);//恢复屏幕的亮度
                    //默认灯是灭的
                    if (mBulbCrossFlag) {
                        mDrawable.reverseTransition(0);
                        mBulbCrossFlag=false;
                    }
                    mPoliceState=false;
                    //保存设置参数
                    preferences.edit().putInt("warning_light", currentWarningInterval)
                            .putInt("police_light", currentPoliceInterval).commit();
                } else {
                    //判断是从哪个地方进来，再次点击返回之前的界面
                    switch (mLastType) {
                        case UI_TYPE_FLASHLIGHT:
                            flashLightLayout.setVisibility(View.VISIBLE);
                            mCurrentType = UiType.UI_TYPE_FLASHLIGHT;//记录当前的位置
                            break;
                        case UI_TYPE_WARNING:
                            warningLayout.setVisibility(View.VISIBLE);
                            mCurrentType = UiType.UI_TYPE_WARNING;//记录当前的位置
                            screenBrightness(1f);//提高系统的亮度
                            new WarningLightThread().start();
                            break;
                        case UI_TYPE_MORSE:
                            morseLayout.setVisibility(View.VISIBLE);
                            mCurrentType=UiType.UI_TYPE_MORSE;
                            break;
                        case UI_TYPE_BULB:
                            bulbLayout.setVisibility(View.VISIBLE);
                            textHide.hide();//文字显示后自动隐藏
                            mCurrentType=UiType.UI_TYPE_BULB;
                            break;
                        case UI_TYPE_COLOR:
                            colorLayout.setVisibility(View.VISIBLE);
                            hideTextColor.hide();//文字显示后自动隐藏
                            mCurrentType=UiType.UI_TYPE_COLOR;
                            break;
                        case UI_TYPE_POLICE:
                            policeLayout.setVisibility(View.VISIBLE);
                            mCurrentType=UiType.UI_TYPE_POLICE;
                            hideTextPolice.hide();//文字显示后自动隐藏
                            new PoliceThread().start();
                            break;
                        case UI_TYPE_SETTING:
                            settingLayout.setVisibility(View.VISIBLE);
                            mCurrentType=UiType.UI_TYPE_SETTING;
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        findViewById(R.id.image_main_flashlight).setOnClickListener(listener);
        findViewById(R.id.image_main_warning).setOnClickListener(listener);
        findViewById(R.id.image_main_morse).setOnClickListener(listener);
        findViewById(R.id.image_main_bulb).setOnClickListener(listener);
        findViewById(R.id.image_main_color).setOnClickListener(listener);
        findViewById(R.id.image_main_police).setOnClickListener(listener);
        findViewById(R.id.image_main_settings).setOnClickListener(listener);
    }

    /**
     * 模块的点击事件
     */
    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image_main_flashlight://显示手电筒界面，更新当前的位置
                    hideAllui();
                    flashLightLayout.setVisibility(View.VISIBLE);
                    mCurrentType = UiType.UI_TYPE_FLASHLIGHT;
                    mLastType = UiType.UI_TYPE_FLASHLIGHT;
                    break;
                case R.id.image_main_warning://显示警告灯界面，更新当前的位置
                    hideAllui();
                    warningLayout.setVisibility(View.VISIBLE);
                    mLastType = UiType.UI_TYPE_WARNING;
                    mCurrentType = mLastType;
                    screenBrightness(1f);//提高系统的亮度
                    new WarningLightThread().start();
                    break;
                case R.id.image_main_morse://显示摩尔斯界面，更新当前的位置
                    hideAllui();
                    morseLayout.setVisibility(View.VISIBLE);
                    mLastType = UiType.UI_TYPE_MORSE;
                    mCurrentType = mLastType;
                    break;
                case R.id.image_main_bulb:
                    hideAllui();
                    bulbLayout.setVisibility(View.VISIBLE);
                    textHide.hide();//文字显示后自动隐藏
                    mLastType = UiType.UI_TYPE_BULB;
                    mCurrentType = mLastType;
                    break;
                case R.id.image_main_color:
                    hideAllui();
                    colorLayout.setVisibility(View.VISIBLE);
                    hideTextColor.hide();//文字显示后自动隐藏
                    mLastType = UiType.UI_TYPE_COLOR;
                    mCurrentType = mLastType;
                    //取互补色
                    hideTextColor.setTextColor(Color.rgb(255-Color.red(mCurrentColorLight),
                            255 - Color.green(mCurrentColorLight), 255 -Color.blue(mCurrentColorLight)));
                    break;
                case R.id.image_main_police:
                    hideAllui();
                    policeLayout.setVisibility(View.VISIBLE);
                    hideTextPolice.hide();//文字显示后自动隐藏
                    screenBrightness(1f);//提高系统的亮度
                    mLastType = UiType.UI_TYPE_POLICE;
                    mCurrentType = mLastType;
                    new PoliceThread().start();
                    break;
                case R.id.image_main_settings:
                    hideAllui();
                    settingLayout.setVisibility(View.VISIBLE);
                    mLastType = UiType.UI_TYPE_SETTING;
                    mCurrentType = mLastType;
                    break;
                default:
                    break;
            }
        }
    };


}
