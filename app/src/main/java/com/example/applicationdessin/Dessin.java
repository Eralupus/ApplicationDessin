package com.example.applicationdessin;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;


import androidx.appcompat.app.AppCompatActivity;

public class Dessin extends AppCompatActivity {

    ViewDraw whatIdraw;

    private Paint paint;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dessin);

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
        private int x;
        private int y;


        public ViewDraw(Context context)
        {
            super(context);
            paint.setColor(Color.BLACK);

        }





        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }

        public void onClick(View view) {}


    }


    public void rouge() {paint.setColor(Color.RED);}

    public void vert() {paint.setColor(Color.GREEN);}

    public void bleu() {paint.setColor(Color.BLUE);}

    public void jaune() {paint.setColor(Color.YELLOW);}

    public void noir() {paint.setColor(Color.BLACK);}



}

