package io.tatsuki.wifiscan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by TI on 2015/09/26.
 */

// 画面をタッチしたときにソナーを表示
public class SonerSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    Some_param someParam = new Some_param();
    private float drawX, drawY;
    private float degree = 270;
    private ScheduledExecutorService service = null;

    public SonerSurfaceView(Context context) {
        super(context);
        initial();
    }

    public SonerSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial();
    }

    public SonerSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initial();
    }

    public SonerSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawCircle(holder);
        onResume();
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

        holder.unlockCanvasAndPost(c);
    }

    // 円の描画
    public void drawCircle(){
        Canvas c = getHolder().lockCanvas();
        if ( c!=null) {
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
        }
        getHolder().unlockCanvasAndPost(c);
    }

    // 円弧の描画
    public void draw_Arc(float degree){
        Canvas c = getHolder().lockCanvas();
        if ( c!=null) {
            // 背景色
            c.drawColor(Color.BLACK);
            Paint p = new Paint();
            // 描く色の設定
            p.setColor(Color.GREEN);
            p.setStyle(Paint.Style.FILL);
            //p.setStrokeWidth(1);
            p.setAntiAlias(true);
            ///色配列(緑，透明)
            int colors[] = new int[]{0xff00ff00, 0x00000000};
            ///それぞれの色が中心からどれだけ離れているかの比率(0.0fから1.0f)
            float positions[] = new float[]{0.0f, 0.1f};
            SweepGradient sg = new SweepGradient(drawX, drawY, colors, positions);
            p.setDither(true);
            p.setShader(sg);
            c.rotate(degree ,drawX, drawY);
            c.drawArc(new RectF(drawX-180, drawY-180, drawX+180, drawY+180), 0, 40, true, p);
        }
        getHolder().unlockCanvasAndPost(c);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            // 角度更新
            degree -= 4;
            // 画面の更新
            drawCircle();
            draw_Arc(degree);
            if (degree < -90) {
                onPause();
            }
        }
    };

    public void onResume() {
        // タイマー作成
        service = Executors.newSingleThreadScheduledExecutor();
        // 指定時間ごとにtaskの実行
        service.scheduleAtFixedRate(task, 10,10, TimeUnit.MILLISECONDS);
    }

    public void onPause() {
        // タイマーの停止
        service.shutdown();
        service = null;
        // 角度の初期化
        degree = 270;
        drawCircle();
    }
/*
    // view画面がタッチされたときの処理
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //Log.d("Touch the view" ,"center="+ drawX + "," + drawY);

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onResume();
                Log.d("Touch the view", "degree=" + degree);
                break;
            //case MotionEvent.ACTION_UP:
            //  onPause();
            //break;
        }
        return true;
    }
    */
}