package com.example.pulkit.sos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;




        import android.content.Context;
        import android.gesture.GestureOverlayView;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Toast;

public class Custom_Drawing extends AppCompatActivity {

    DrawingView dv ;
    private Paint mPaint;
    int c1,c2,c3;
    long presentTime;
    Thread t;
    boolean patternDrawn=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dv = new DrawingView(this);
        setContentView(dv);
        Intent i =getIntent();
        presentTime=i.getLongExtra("presentTime",0);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(16);
        c1=c2=c3=0;
        t =new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20000 - presentTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Log.d("hehe","destroying");
                if(!patternDrawn) {
//                    Intent in = new Intent(Custom_Drawing.this, SendMessage1.class);
//                    startActivity(in);
                    Intent i = new Intent();
                    i.putExtra("status",false);
                    setResult(RESULT_OK,i);
                    finish();
                }
                patternDrawn = false;
            }
        });
        t.start();
    }

    public class DrawingView extends View {

        public int width;
        public  int height;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint   mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;

        public DrawingView(Context c) {
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);


        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            width = w;
            height = h;
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //.d("hehe","onDraw");
            canvas.drawCircle(240,140,8,mPaint);
            canvas.drawCircle(340,460,8,mPaint);
            canvas.drawCircle(100,460,8,mPaint);
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath( mPath,  mPaint);
            canvas.drawPath( circlePath,  circlePaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;

                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath,  mPaint);
            // kill this so we don't double draw
            // mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();//  240,140  340,460 100,460
            float y = event.getY();

            if((x>=224 && x<=256) && (y>=124 && y<=156))
                c1=1;
            if((x>=324 && x<=356) && (y>=444 && y<=476))
                c2=1;
            if((x>=84 && x<=116) && (y>=444 && y<=476))
                c3=1;
            if(c1==1 && c2==1 && c3==1) {
                c1=c2=c3=0;
                Toast.makeText(Custom_Drawing.this, "Cancelled", Toast.LENGTH_LONG).show();
                //t.stop();
                patternDrawn = true;
                Intent i = new Intent();
                i.putExtra("status",true);
                setResult(RESULT_OK,i);
                finish();
            }
            //Log.d("hehe","x="+x+" y="+y);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    c1=c2=c3=0;
                    touch_up();
                    invalidate();
                    clearDrawing();
                    break;
            }
            return true;
        }

        public void clearDrawing()
        {

            setDrawingCacheEnabled(false);
            // don't forget that one and the match below,
            // or you just keep getting a duplicate when you save.

            onSizeChanged(width, height, width, height);
            invalidate();

            setDrawingCacheEnabled(true);
        }

    }
}