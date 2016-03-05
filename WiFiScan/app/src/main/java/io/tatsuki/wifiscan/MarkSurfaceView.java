package io.tatsuki.wifiscan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * Created by TI on 2015/10/17.
 */
public class MarkSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    Some_param SomeParam = new Some_param();
    private float drawX, drawY;
    private String[] result;
    private int[] frequency;
    private int[] level;
    private int fre_value;
    private int level_value;

    public MarkSurfaceView(Context context) {
        super(context);
        initial();
    }

    public MarkSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial();
    }

    public MarkSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initial();
    }

    public MarkSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawCircle(holder);
        //drawPoint(holder, drawX,drawY);
        Log.d("Frequency", "frequency=" + SomeParam.getFrequency());
        Log.d("Level", "Level=" + SomeParam.getLevel());
        //Log.d("Frequency", "fre_value=" + SomeParam.getFre_value());
        //Log.d("Level", "Level_value=" + SomeParam.getLevel_value());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    // 初期設定
    private void initial() {
        SurfaceHolder holder = this.getHolder();
        holder.addCallback(this);
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point point = new Point();
        disp.getSize(point);
        // 描画の中心点
        drawX = (float) (point.x / 2);
        drawY = (float) (point.y / 4);


    }

    // 円の描画
    public void drawCircle(SurfaceHolder holder) {
        Canvas c = holder.lockCanvas();
        // 背景色
        c.drawColor(Color.BLACK);
        Paint p = new Paint();
        // 描く色の設定
        p.setColor(Color.GREEN);
        // 輪郭線の太さ
        p.setStrokeWidth(1);
        p.setAntiAlias(true);
        // 輪郭のみの描画
        p.setStyle(Paint.Style.STROKE);

        for (int i=0; i<= 3; i++) {
            c.drawCircle(drawX, drawY, 60+40*i, p);
        }

        Paint p_point = new Paint();
        // 描く色の設定
        p_point.setColor(Color.GREEN);
        // 輪郭線の太さ
        p_point.setStrokeWidth(1);
        p_point.setAntiAlias(true);
        p_point.setStyle(Paint.Style.FILL);
        p_point.setAlpha(125);

        frequency = SomeParam.getFrequency();
        level = SomeParam.getLevel();
        //Log.d("Frequency", "frequency=" + frequency[0]);
        //Log.d("Level", "level=" + level[0]);

        if (frequency != null && level != null) {
            for (int i=0; i< level.length; i++) {
                float lev = distance(level[i]);
                float fre = degree(frequency[i]);
                float p_x = point_x(lev, fre);
                float p_y = point_y(lev, fre);

                float x = drawX + (p_x / 15);
                float y = drawY + (p_y / 15);

                //Log.d("point:", "x" + x + "y" + y);
                //Log.d("distance", "lev:" + lev + "fre" + fre);
                c.drawCircle(x, y, 10, p_point);
            }
        }

        //Log.d("Frequency", "fre_value=" + SomeParam.getFre_value());
        //Log.d("Level", "Level_value=" + SomeParam.getLevel_value());
        fre_value = SomeParam.getFre_value();
        level_value = SomeParam.getLevel_value();
        // 以下の部分で点が描けない(10/27)
        // 値の受け取りはできている
        Paint circle_point = new Paint();
        // 描く色の設定
        circle_point.setColor(Color.YELLOW);
        // 輪郭線の太さ
        circle_point.setStrokeWidth(1);
        circle_point.setAntiAlias(true);
        circle_point.setStyle(Paint.Style.FILL);
        //circle_point.setAlpha(125);

        if (fre_value != 0 && level_value != 0) {
            float lev_dis = distance(level_value);
            float fre_deg = degree(fre_value);
            float c_x = point_x(lev_dis, fre_deg);
            float c_y = point_y(lev_dis, fre_deg);

            float circle_x = drawX + (c_x / 15);
            float circle_y = drawY + (c_y / 15);

            c.drawCircle(circle_x, circle_y, 10, circle_point);
            //Log.d("distance", "lev_dis" + lev_dis + "fre_deg" + fre_deg);
            //Log.d("point:", "x" + circle_x + "y" + circle_y);
        }
        //Log.d("point", "X:" + x + ",Y:" + y);
        //Log.d("center", "drawX:" + drawX + ",drawY:" + drawY);

        holder.unlockCanvasAndPost(c);
    }

    /*
    // 座標の描画
    public void drawPoint(SurfaceHolder holder, float x, float y) {
        Canvas c = holder.lockCanvas();
        // 背景色
        c.drawColor(Color.BLACK);
        Paint p = new Paint();
        // 描く色の設定
        p.setColor(Color.GREEN);
        // 輪郭線の太さ
        p.setStrokeWidth(1);
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        c.drawCircle(x, y, 5, p);

        holder.unlockCanvasAndPost(c);
    }
    */
    public float distance(int level) {
        float dist = -1 * ((level + 40) * 40);
        return dist;
    }

    public float degree(int frequency) {
        float deg = (float) ((frequency - 2412) * (Math.PI / 30));
        return deg;
    }

    public float point_x(float dist, float deg) {
        float x = (float) (dist * Math.cos(deg));
        return x;
    }

    public float point_y(float dist, float deg) {
        float y = (float) (dist * Math.sin(deg));
        return y;
    }
}
