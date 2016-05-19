package net.dell.supperflashlight;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import net.dell.supperflashlight.view.ColorPickerDialog;
import net.dell.supperflashlight.view.HideText;

/**
 * Created by dell on 2016/5/9.
 */
public class ColorLight extends BulbActivity implements ColorPickerDialog.OnColorChangedListener {

    public int mCurrentColorLight=Color.RED;
    public HideText hideTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideTextColor = (HideText)findViewById(R.id.tv_color_light);
    }

    /**
     * 点击事件
     * @param view
     */
    public void showColorPicker(View view){
        Toast.makeText(ColorLight.this, "点击屏幕切换背景色", Toast.LENGTH_SHORT).show();
        new ColorPickerDialog(this, this, Color.RED).show();
    }

    @Override
    public void colorChanged(int color) {
        colorLayout.setBackgroundColor(color);
        mCurrentColorLight=color;
    }
}
