package net.dell.supperflashlight;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * 设置界面
 * Created by dell on 2016/5/10.
 */
public class SettingActivity extends PoliceLight implements SeekBar.OnSeekBarChangeListener {

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add_shortcut://添加
                    Log.e("----------------", "" + shortCutInScreen());
                    if (!shortCutInScreen()) {
                        Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
                        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "超级手电筒");
                        Parcelable icon = Intent.ShortcutIconResource.fromContext(SettingActivity.this, R.mipmap.icon);
                        //点击快捷方式记得跳转
                        Intent flashIntent = new Intent();
                        flashIntent.setClassName("net.dell.supperflashlight", "net.dell.supperflashlight.MainActivity");
                        flashIntent.setAction(Intent.ACTION_MAIN);
                        flashIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, flashIntent);
                        sendBroadcast(addIntent);
                        Toast.makeText(SettingActivity.this, "已成功添加快捷方式", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingActivity.this, "快捷方式已存在，不用再次添加！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_delete_shortcut://移除
                    if (shortCutInScreen()) {
                        Intent delIntent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
                        delIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "超级手电筒");

                        Intent intent = new Intent();
                        intent.setClassName("net.dell.supperflashlight", "net.dell.supperflashlight.MainActivity");
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        delIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
                        sendBroadcast(delIntent);
                    } else {
                        Toast.makeText(SettingActivity.this, "您还未添加快捷方式呢，亲！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSeekBarWarning.setOnSeekBarChangeListener(this);
        mSeekBarPolice.setOnSeekBarChangeListener(this);
        //添加和删除快捷方式
        btnDelShortcut.setOnClickListener(listener);
        btnAddShortcut.setOnClickListener(listener);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sbar_police:
                currentPoliceInterval = progress + 50;
                break;
            case R.id.sbar_warning:
                currentWarningInterval = progress + 100;
                break;
            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 查询系统数据库，判断是否存在快捷方式
     * @return
     */
    private boolean shortCutInScreen() {
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.cyanogenmod.trebuchet.settings/favorites"),
                null, "intent like ?", new String[]{"%component=net.dell.supperflashlight/.MainActivity%"}, null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


}
