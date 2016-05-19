package net.dell.supperflashlight;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * 摩尔斯电码界面
 * Created by dell on 2016/5/9.
 */
public class MorseActivity extends WarningLight {

    private final int DOT_TIME = 200;//点的时间，毫秒
    private final int LINE_TIME = DOT_TIME * 4; //划线的时间
    private final int DOT_LINE_TIME = DOT_TIME;//点和线的停顿时间
    private final int CHAR_CHAR_TIME = DOT_TIME * 4;//字符之间的停顿时间
    private final int WORD_WORD_TIME = DOT_TIME * 7;//单词之间的停顿时间
    private String mMorseCode;// 存储密码文本
    private Map<Character, String> codeMap = new HashMap<>();

    public ImageButton morseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDatas();

        morseBtn = (ImageButton) findViewById(R.id.image_btn_morse);
        morseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断设备是否支持闪光灯
                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    Toast.makeText(MorseActivity.this, "该设备没有闪光灯！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (verifMorseCode()) {
                    sendSentense(mMorseCode);
                }
            }
        });
    }

    /**
     * 休眠时间
     *
     * @param i
     */
    public void sleepExt(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送点.
     */
    private void sendDot() {
        openFlashlight();
        sleepExt(DOT_TIME);
        closeFlashlight();
    }

    /**
     * 发送-划线
     */
    private void sendLine() {
        openFlashlight();
        sleepExt(LINE_TIME);
        closeFlashlight();
    }

    /**
     * 发送字符
     *
     * @param c
     */
    private void sendChar(char c) {
        //根据字符查询数据表
        String morsecode = codeMap.get(c);
        if (morsecode != null) {
            char lastChar = ' ';
            for (int i = 0; i < morsecode.length(); i++) {
                //分别获取点或者线
                char dotline = morsecode.charAt(i);
                if (dotline == '.') {
                    sendDot();
                } else if (dotline == '-') {
                    sendLine();
                }
                //从点到线需要停顿
                if (i > 0 && i < morsecode.length() - 1) {
                    if (lastChar == '.' && dotline == '-') {
                        sleepExt(DOT_LINE_TIME);
                    }
                }
                lastChar = dotline;
            }
        }
    }

    /**
     * 发送单词
     *
     * @param s
     */
    private void sendWord(String s) {
        for (int i = 0; i < s.length(); i++) {
            //取出逐个字符发送
            char c = s.charAt(i);
            sendChar(c);
            if (i < s.length() - 1) {
                sleepExt(CHAR_CHAR_TIME);
            }
        }
    }

    /**
     * 发送语句，以空格区分
     *
     * @param s
     */
    private void sendSentense(String s) {
        String[] words = s.split(" +");
        for (int i = 0; i < words.length; i++) {
            //提取逐个单词发送
            sendWord(words[i]);
            if (i < words.length - 1) {
                sleepExt(WORD_WORD_TIME);
            }
        }
        Toast.makeText(MorseActivity.this, "摩尔斯电码已发送", Toast.LENGTH_SHORT).show();
    }

    /**
     * 进行输入验证
     *
     * @return
     */
    private boolean verifMorseCode() {
        mMorseCode = etMorse.getText().toString().toLowerCase();
        if ("".equals(mMorseCode)) {
            Toast.makeText(MorseActivity.this, "请输入摩尔斯电码", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (int i = 0; i < mMorseCode.length(); i++) {
            char c = mMorseCode.charAt(i);
            if (!(c >= 'a' && c <= 'z') && !(c >= '0' && c <= '9') && c != ' ') {
                Toast.makeText(MorseActivity.this, "摩尔斯电码只能是字母和数字！", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    /**
     *
     */
    public void getDatas() {
        codeMap.put('a', ".-");
        codeMap.put('b', "-...");
        codeMap.put('c', "-.-.");
        codeMap.put('d', "-..");
        codeMap.put('e', ".");

        codeMap.put('f', "..-.");
        codeMap.put('g', "--.");
        codeMap.put('h', "....");
        codeMap.put('i', "..");
        codeMap.put('j', ".---");

        codeMap.put('k', "-.-");
        codeMap.put('l', ".-..");
        codeMap.put('m', "--");
        codeMap.put('n', "-.");
        codeMap.put('o', "---");

        codeMap.put('p', ".--.");
        codeMap.put('q', "--.-");
        codeMap.put('r', "-.-");
        codeMap.put('s', "...");
        codeMap.put('t', "-");

        codeMap.put('u', "..-");
        codeMap.put('v', "...-");
        codeMap.put('w', ".--");
        codeMap.put('x', "-..-");
        codeMap.put('y', "-.--");
        codeMap.put('z', "--..");

        codeMap.put('0', "-----");
        codeMap.put('1', ".----");
        codeMap.put('2', "..---");
        codeMap.put('3', "...--");
        codeMap.put('4', "....-");

        codeMap.put('5', ".....");
        codeMap.put('6', "-....");
        codeMap.put('7', "--...");
        codeMap.put('8', "---..");
        codeMap.put('9', "----.");
    }
}
