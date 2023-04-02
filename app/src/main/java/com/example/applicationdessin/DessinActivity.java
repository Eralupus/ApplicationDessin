package com.example.applicationdessin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;
import android.widget.FrameLayout;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationdessin.metier.Dessin;
import com.example.applicationdessin.metier.Metier;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DessinActivity extends AppCompatActivity {

    ViewDraw whatIdraw;

    private Paint paint;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dessin);
        this.paint = new Paint();
        FrameLayout container = findViewById(R.id.custom_view_container);
        whatIdraw = new ViewDraw(this);
        container.addView(whatIdraw);
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
        private Metier  metier = new Metier();
        private final int TOOL_RECTANGLE = 1;
        private final int TOOL_ROND      = 2;
        private final int TOOL_TRACE     = 3;
        private final int TOOL_ERASER    = 4;
        private int currentTool = TOOL_RECTANGLE;
        private int currentColor = Color.BLACK;
        private Path path = new Path();
        private ArrayList<Path> lstPath = new ArrayList<Path>();
        private float xA;
        private float yA;
        private float xB;
        private float yB;
        public ViewDraw(Context context)
        {
            super(context);
            this.setOnTouchListener(this);
            setFocusable(true);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void onDraw (Canvas canvas)
        {
            switch (this.currentTool)
            {
                case TOOL_RECTANGLE :
                    canvas.drawRect(xA,yA,xB,yB,paint);
                    break;
                case TOOL_ROND:
                    canvas.drawOval(xA,yA,xB,yB,paint);
                    break;
            }

            for (Dessin dessin : this.metier.getLstDessin())
            {
                switch (dessin.getType())
                {
                    case TOOL_RECTANGLE:
                        canvas.drawRect(dessin.getFromX(),dessin.getFromY(),dessin.getToX(),dessin.getToY(),paint);
                        break;
                    case TOOL_ROND:
                        canvas.drawOval(dessin.getFromX(),dessin.getFromY(),dessin.getToX(),dessin.getToY(),paint);
                        break;
                }
            }
            for (Path path : this.metier.getLstPath()){
                switch (this.currentTool)
                {
                    case TOOL_TRACE:
                        paint.setColor(this.currentColor);
                        break;
                    case TOOL_ERASER:
                        paint.setColor(Color.WHITE);
                        break;
                }
                paint.setDither(true);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeJoin(Paint.Join.ROUND);
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setStrokeWidth(3);
                canvas.drawPath(path,paint);
            }
        }



        public boolean onTouch(View view, MotionEvent motionEvent)
        {
            //Log.d ("Tag",this.lstPath+"");
           //Log.d("TAG", "On Touch:"+ "X;"+motionEvent.getX()+" Y:"+motionEvent.getY());
            float x = motionEvent.getX();
            float y = motionEvent.getY();

            switch (motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    this.xA =  motionEvent.getX();
                    this.yA =  motionEvent.getY();
                    if (this.currentTool == TOOL_TRACE || this.currentTool==TOOL_ERASER){
                        path.moveTo(x,y);
                        invalidate();
                    }
                    return true;
                case MotionEvent.ACTION_MOVE:
                    //Log.d("TAG", "On Touch Move:"+ "X;"+motionEvent.getX()+" Y:"+motionEvent.getY());
                    this.xB =  motionEvent.getX();
                    this.yB =  motionEvent.getY();
                    if (this.currentTool == TOOL_TRACE){
                        path.lineTo(x,y);
                        this.metier.addPath(path);
                    }
                    invalidate();
                    return true;
                case MotionEvent.ACTION_UP:
                    if (this.currentTool != TOOL_TRACE || this.currentTool!=TOOL_ERASER)
                    {
                        this.metier.addDessin(currentTool, this.xA, this.yA, this.xB, this.yB, this.currentColor);
                        xA = yA = xB = yB = 0;
                    }
                    invalidate();
                    return true;
                default:
                    return true;
            }

        }

        public void onClick(View view) {}


    }


    public void rouge(View view) {paint.setColor(Color.RED);}

    public void vert(View view) {paint.setColor(Color.GREEN);}

    public void bleu(View view) {paint.setColor(Color.BLUE);}

    public void jaune(View view) {paint.setColor(Color.YELLOW);}

    public void noir(View view) {paint.setColor(Color.BLACK);}



}

