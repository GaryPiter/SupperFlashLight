package net.dell.supperflashlight;

import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.TransitionDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;

/**
 * 手电筒界面
 * Created by dell on 2016/5/7.
 */
public class FlashLight extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flashLight.setTag(false); //设置默认相机是关闭的

        //获取手机屏幕的像素大小，动态设置点击区域
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        ViewGroup.LayoutParams params = flashLightController.getLayoutParams();
        params.height = point.x;//点击区域的高度为屏幕的四分之三
        params.width = point.y / 4;//点击区域的宽度为屏幕的三分之一

        flashLightController.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_flashlight_controller:
                //判断设备是否支持闪光灯
                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    Toast.makeText(FlashLight.this, "该设备没有闪光灯！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((boolean) flashLight.getTag()==false) {
                    openFlashlight();
                } else {
                    closeFlashlight();
                }
                break;
            default:
                break;
        }
    }


    /**
     * 开启闪光灯
     */
    public void openFlashlight() {
        //1.获取渐变动画
        TransitionDrawable drawable = (TransitionDrawable) flashLight.getDrawable();
        drawable.startTransition(200);//设置渐变的时间
        flashLight.setTag(true);    //设置打开的标识
        try {
            //2.开启照相机
            mCamera = Camera.open();
            int textName = 0;
            mCamera.setPreviewTexture(new SurfaceTexture(textName));
            mCamera.startPreview();
            //3.设置参数模式
            mParameters = mCamera.getParameters();
            mParameters.setFlashMode(mParameters.FLASH_MODE_TORCH);
            //4.打开闪光灯
            mCamera.setParameters(mParameters);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭闪光灯
     */
    public void closeFlashlight() {
        //1.获取渐变动画
        TransitionDrawable drawable = (TransitionDrawable) flashLight.getDrawable();
        if ((boolean) flashLight.getTag()) {
            drawable.reverseTransition(200);
            flashLight.setTag(false);    //设置关闭的标识
            if (mCamera != null) {
                //2.设置参数模式
                mParameters = mCamera.getParameters();
                mParameters.setFlashMode(mParameters.FLASH_MODE_OFF);
                //3.关闭闪光灯
                mCamera.setParameters(mParameters);
                mCamera.stopPreview();
                //4.释放资源
                mCamera.release();
                mCamera = null;
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        //失去焦点时自动关闭
        closeFlashlight();
    }
}
