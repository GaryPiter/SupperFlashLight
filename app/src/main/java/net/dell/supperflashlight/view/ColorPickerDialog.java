package net.dell.supperflashlight.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 调色板的对话框
 * Created by dell on 2016/5/9.
 */
public class ColorPickerDialog extends Dialog {

    //对话框的宽高
    private final int COLOR_DIALOG_WIDTH = 400;
    private final int COLOR_DIALOG_HEIGHT = 400;
    //中心点
    private final int CENTER_X = COLOR_DIALOG_WIDTH / 2;
    private final int CENTER_Y = COLOR_DIALOG_HEIGHT / 2;
    //小圆的半径
    private final int CENTER_READIUS = 32;
    private int mInitialColor;//初始的颜色
    private OnColorChangedListener mListener;

    //回调接口
    public interface OnColorChangedListener {
        void colorChanged(int color);
    }

    public ColorPickerDialog(Context context, OnColorChangedListener listener, int initialColor) {
        super(context);
        mListener = listener;
        mInitialColor = initialColor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnColorChangedListener listener = new OnColorChangedListener() {
            @Override
            public void colorChanged(int color) {
                mListener.colorChanged(color);
                dismiss();
            }
        };
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new ColorPickerView(getContext(), listener, mInitialColor));
        //设置对话框的背景色
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.BLACK);
        getWindow().setBackgroundDrawable(colorDrawable);
        //设置宽和高
        getWindow().setAttributes(new WindowManager.LayoutParams(COLOR_DIALOG_WIDTH, COLOR_DIALOG_HEIGHT, 0, 0, 0));

    }

    /**
     * 内部类绘制圆
     */
    private class ColorPickerView extends View {

        private Paint mPaint;//外圆画笔
        private Paint mCenterPaint;//内小圆画笔
        private final int[] colors; //颜色条的起始颜色
        private OnColorChangedListener mLintener;
        private boolean mTrackingCenter;//
        private boolean mHeightlightCenter;//判断透明度
        private static final float PI = 3.1415926f;//用于处理三角函数

        public ColorPickerView(Context context, OnColorChangedListener listener, int color) {
            super(context);
            mLintener = listener;
            colors = new int[]{0xFFFF0000, 0xFFFF00FF, 0xFF0000FF,
                    0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000};
            //颜色渲染
            Shader shader = new SweepGradient(0, 0, colors, null);

            //外圈环形画笔
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setShader(shader);
            mPaint.setStyle(Paint.Style.STROKE);//设置为实线
            mPaint.setStrokeWidth(32);
            //小圆的画笔
            mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCenterPaint.setColor(color);
            mCenterPaint.setStrokeWidth(5);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //计算外圈的半径
            float radius = CENTER_X - mPaint.getStrokeWidth() * 0.5f - 20;
            canvas.translate(CENTER_X, CENTER_Y);//将坐标原点移到中心位置
            canvas.drawCircle(0, 0, radius, mPaint);//绘制出外圈的圆环
            canvas.drawCircle(0, 0, CENTER_READIUS, mCenterPaint);
            //
            if (mTrackingCenter) {
                int c = mCenterPaint.getColor();
                mCenterPaint.setStyle(Paint.Style.STROKE);
                //设置高亮--透明度
                if (mHeightlightCenter) {
                    mCenterPaint.setAlpha(0xFF);
                } else {
                    mCenterPaint.setAlpha(0x00);
                }
                //圆环的外圆
                canvas.drawCircle(0, 0, CENTER_READIUS + mCenterPaint.getStrokeWidth(), mCenterPaint);
                mCenterPaint.setStyle(Paint.Style.FILL);
                mCenterPaint.setColor(c);
            }
        }

        /**
         * 求平均值
         *
         * @param s
         * @param d
         * @param p
         * @return
         */
        private int ave(int s, int d, float p) {
            //计算两个颜色之间的偏移量
            return s + Math.round(p * (d - s));
        }


        private int interpColor(int colors[], float unit) {
            if (unit <= 0) {
                return colors[0];
            }
            if (unit >= 1) {
                return colors[colors.length - 1];
            }
            float p = unit * (colors.length - 1);
            int i = (int) p;
            p -= i;
            //
            int c0 = colors[i];
            int c1 = colors[i + 1];
            int a = ave(Color.alpha(c0), Color.alpha(c1), p);
            int r = ave(Color.red(c0), Color.red(c1), p);
            int g = ave(Color.green(c0), Color.green(c1), p);
            int b = ave(Color.blue(c0), Color.blue(c1), p);
            return Color.argb(a, r, g, b);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX() - CENTER_X;
            float y = event.getY() - CENTER_Y;
            boolean inCenter = Math.sqrt(x * x + y * y) <= CENTER_READIUS;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //判断点击的是中心位置
                    mTrackingCenter = inCenter;
                    if (inCenter) {
                        mHeightlightCenter = true;
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    //计算
                    float angle = (float) Math.atan2(y, x);
                    float unit = angle / (2 * PI);
                    if (unit < 0) {
                        unit += 1;
                    }
                    mCenterPaint.setColor(interpColor(colors, unit));
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    if (mTrackingCenter) {
                        if (inCenter) {
                            mLintener.colorChanged(mCenterPaint.getColor());
                        }
                        mTrackingCenter = false;
                        invalidate();
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    }


}
