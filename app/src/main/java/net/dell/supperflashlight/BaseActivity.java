package net.dell.supperflashlight;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * Created by dell on 2016/5/7.
 */
public class BaseActivity extends Activity {

    public int mScreenBrightness;
    public ImageView flashLight;//手电筒
    public ImageView flashLightController;//手电筒点击事件区域
    //警告界面控件
    public ImageView warningLightOn, warningLightOff;
    //摩尔斯界面控件
    public EditText etMorse;
    //灯泡控件
    public ImageView imageBulb;
    //设置界面控件标识.频率
    public int currentWarningInterval = 500;
    public int currentPoliceInterval = 100;
    public SeekBar mSeekBarWarning;
    public SeekBar mSeekBarPolice;
    public Button btnAddShortcut, btnDelShortcut;
    public SharedPreferences preferences;
    public Camera mCamera;
    public Camera.Parameters mParameters;

    public FrameLayout flashLightLayout;//手电筒界面
    public LinearLayout mainLayout;     //主界面
    public LinearLayout warningLayout;//警告界面
    public LinearLayout morseLayout;//摩登密码界面
    public FrameLayout bulbLayout;//灯泡界面
    public FrameLayout colorLayout;//调色板界面
    public FrameLayout policeLayout;//警灯界面
    public LinearLayout settingLayout;//设置界面

    public UiType mCurrentType = UiType.UI_TYPE_FLASHLIGHT;//记录当前的位置
    public UiType mLastType = UiType.UI_TYPE_FLASHLIGHT; //记录最后一次点击的位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScreenBrightness = getScreenBrightness();

        flashLight = (ImageView) findViewById(R.id.image_flashlight);
        flashLightController = (ImageView) findViewById(R.id.image_flashlight_controller);
        warningLightOn = (ImageView) findViewById(R.id.warning_light_on);
        warningLightOff = (ImageView) findViewById(R.id.warning_light_off);
        imageBulb = (ImageView) findViewById(R.id.image_bulb);
        etMorse = (EditText) findViewById(R.id.et_morse_code);
        mSeekBarWarning = (SeekBar) findViewById(R.id.sbar_warning);
        mSeekBarPolice = (SeekBar) findViewById(R.id.sbar_police);
        btnAddShortcut = (Button) findViewById(R.id.btn_add_shortcut);
        btnDelShortcut = (Button) findViewById(R.id.btn_delete_shortcut);
        //设置初始值
        mSeekBarWarning.setProgress(currentWarningInterval - 100);
        mSeekBarPolice.setProgress(currentPoliceInterval - 50);
        //初始化界面
        flashLightLayout = (FrameLayout) findViewById(R.id.ui_flashlight);
        mainLayout = (LinearLayout) findViewById(R.id.fragment_ui_main);
        warningLayout = (LinearLayout) findViewById(R.id.fragment_ui_warning);
        morseLayout = (LinearLayout) findViewById(R.id.fragment_ui_morse);
        bulbLayout = (FrameLayout) findViewById(R.id.fragment_ui_bulb);
        colorLayout = (FrameLayout) findViewById(R.id.fragment_ui_color_light);
        policeLayout = (FrameLayout) findViewById(R.id.fragment_ui_police);
        settingLayout = (LinearLayout) findViewById(R.id.fragment_ui_setting);
        preferences = getSharedPreferences("config", Context.MODE_PRIVATE);

        currentWarningInterval = preferences.getInt("warning_light", 200);
        currentPoliceInterval = preferences.getInt("police_light", 100);
    }

    /**
     * 隐藏所有界面
     */
    public void hideAllui() {
        flashLightLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);
        warningLayout.setVisibility(View.GONE);
        morseLayout.setVisibility(View.GONE);
        bulbLayout.setVisibility(View.GONE);
        colorLayout.setVisibility(View.GONE);
        policeLayout.setVisibility(View.GONE);
        settingLayout.setVisibility(View.GONE);
    }

    /**
     * 设置屏幕的亮度，0最暗；1最亮
     *
     * @param value
     */
    public void screenBrightness(float value) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = value;
        getWindow().setAttributes(params);
    }

    /**
     * 获取系统的屏幕亮度
     * 查询系统的数据库获得
     *
     * @return
     */
    public int getScreenBrightness() {
        int value = 0;
        try {
            value = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 枚举，记录选择的类型
     */
    public enum UiType {
        UI_TYPE_FLASHLIGHT,//手电筒
        UI_TYPE_WARNING,//警告灯
        UI_TYPE_MORSE,//摩登密码
        UI_TYPE_BULB,//灯泡
        UI_TYPE_COLOR,//调色板
        UI_TYPE_POLICE,//警灯
        UI_TYPE_SETTING,//设置
        UI_TYPE_MAIN //主界面
    }

    private long firstTime;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstTime < 3000) {
            super.finish();
        } else {
            firstTime = System.currentTimeMillis();
            Toast.makeText(BaseActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
    }

}
