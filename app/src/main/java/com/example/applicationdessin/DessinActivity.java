package com.example.applicationdessin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;
import android.widget.FrameLayout;


import androidx.appcompat.app.AppCompatActivity;

public class DessinActivity extends AppCompatActivity {

    ViewDraw whatIdraw;
    private Bitmap mBitmap;
    private Bitmap mTempBitmap;
    private Canvas mCanvas,mTempCanvas;
    private Paint paint;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dessin);
        this.paint = new Paint();

        FrameLayout container = findViewById(R.id.custom_view_container);
        whatIdraw = new ViewDraw(this);
        container.addView(whatIdraw);

        // Set up the Bitmap object to store the drawing
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mTempBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mTempCanvas = new Canvas(mTempBitmap);
    }


    public void ZoneDessin(View view)
    {

    }

    public void effacerTout(View view)
    {

    }



    public void quitter(View view) {
        finish();
    }


    class ViewDraw extends View implements View.OnTouchListener, View.OnClickListener
    {
        private Path path = new Path();
        private final int TOOL_RECTANGLE = 1;
        private final int TOOL_ROUND     = 2;
        private final int TOOL_GOMME     = 3;
        private final int TOOL_TRACE     = 4;
        private final int currentTool = TOOL_RECTANGLE;

        private float xA;
        private float yA;
        private float xB;
        private float yB;
        private boolean isDrawing = false;
        private int currentColor = Color.BLACK;

        public ViewDraw(Context context)
        {
            super(context);
            setOnTouchListener(this);
            setOnClickListener(this);
            paint.setColor(Color.BLACK);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mTempBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mTempCanvas = new Canvas(mTempBitmap);
        }
        @Override
        public void onClick(View view) {
            Log.d("Tag","On Click");

        }

        @Override
        public boolean onTouch(View view,MotionEvent event) {
            float touchX = event.getX();
            float touchY = event.getY();
            //respond to down, move and up events
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isDrawing = true;
                    xA = touchX;
                    yA = touchY;
                    if (currentTool == TOOL_TRACE) {
                        path.moveTo(touchX, touchY);
                    }
                    invalidate();

                    break;
                case MotionEvent.ACTION_MOVE:
                    isDrawing = true;
                    xB = touchX;
                    yB = touchY;
                    switch (currentTool)
                    {
                        case TOOL_RECTANGLE:
                            Paint paintTemp = new Paint();
                            paintTemp.setColor(Color.parseColor("#330000FF"));
                            mTempCanvas.drawRect(xA,yA,xB,yB,paintTemp);
                            break;
                        case TOOL_TRACE:
                            path.lineTo(touchX, touchY);
                            mCanvas.drawPath(path, paint);
                            break;
                        case TOOL_ROUND:
                            Paint paintTemp2 = new Paint();
                            paintTemp.setColor(Color.parseColor("#330000FF"));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mTempCanvas.drawOval(xA,yA,xB,yB,paintTemp2);
                            }
                            break;

                    }

                    invalidate();

                    break;
                case MotionEvent.ACTION_UP:
                    isDrawing = false;

                    switch (currentTool)
                    {
                        case TOOL_RECTANGLE:
                            mTempBitmap.eraseColor(Color.TRANSPARENT);
                            paint.setColor(this.currentColor);
                            mCanvas.drawRect(xA,yA,xB,yB,paint);
                            break;
                        case TOOL_TRACE:
                            paint.setColor(this.currentColor);
                            path.lineTo(touchX, touchY);
                            mCanvas.drawPath(path, paint);
                            path.reset();
                            break;
                        case TOOL_ROUND:
                            paint.setColor(this.currentColor);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mCanvas.drawOval(xA,yA,xB,yB,paint);
                            }
                            break;

                    }
                    xA=xB= touchX;
                    yA=yB= touchY;
                    invalidate();

                    break;
                default:
                    return true;
            }
            //redraw
            invalidate();
            return true;
        }
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, 0 , 0, paint);
            if (isDrawing)
            {
                switch (currentTool)
                {
                    case TOOL_RECTANGLE:
                        canvas.drawBitmap(mTempBitmap, 0, 0, null);
                        break;
                }

            }
        }
        public void setColor(int color){
            this.currentColor = color;
            paint.setColor(color);
        }
    }


    public void rouge(View view) {whatIdraw.setColor(Color.RED);}

    public void vert(View view) {whatIdraw.setColor(Color.GREEN);}

    public void bleu(View view) {whatIdraw.setColor(Color.BLUE);}

    public void jaune(View view) {whatIdraw.setColor(Color.YELLOW);}

    public void noir(View view) {whatIdraw.setColor(Color.BLACK);}



}

