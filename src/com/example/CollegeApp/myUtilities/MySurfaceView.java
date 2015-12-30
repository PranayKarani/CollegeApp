package com.example.CollegeApp.myUtilities; // 28 Dec, 01:57 PM

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.example.CollegeApp.R;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    Context myContext;
    public MyViewThread mvt;

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder();

        holder.addCallback(this);

        mvt = new MyViewThread(holder, context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mvt.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mvt.setSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        try {
            mvt.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    class MyViewThread extends Thread {

        Bitmap bgImg;
        int canvasHeight = 1, canvasWidth = 1;
        Drawable crashImg;
        SurfaceHolder holder;

        Paint linePaint;
        RectF rect;

        public MyViewThread(SurfaceHolder holder, Context context){
            myContext = context;
            this.holder = holder;

            bgImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.earthrise);

            linePaint = new Paint();
            linePaint.setAntiAlias(true);
            linePaint.setARGB(255,255,0,0);

            rect = new RectF(0,0,0,0);

        }

        @Override
        public void run() {
            Canvas c= null;
            try{
                c = holder.lockCanvas();
                synchronized (holder){
                    doDraw(c);
                }
            } finally {
                if(c != null){
                    holder.unlockCanvasAndPost(c);
                }
            }
        }

        void setSize(int width, int height){
            canvasWidth = width;
            canvasHeight = height;

            bgImg = Bitmap.createScaledBitmap(bgImg, width, height, true);

        }

        void doDraw(Canvas c){

//            c.drawBitmap(bgImg, 0, 0, null);

            rect.set(4, 4, 4, 4);
            c.drawRect(rect, linePaint);
            c.drawLine(0,0,canvasWidth,canvasHeight,linePaint);

        }

    }

}
