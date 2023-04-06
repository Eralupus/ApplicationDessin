package com.example.applicationdessin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageButton;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.content.SharedPreferences;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;


public class DessinActivity extends AppCompatActivity {

    public static final String SHARE_PREFS = "sharedPref";
    public static final String AL_FORMES   = "alformes";
    public static final String AL_COUL   = "alcoul";
    public static final String AL_XA   = "alxA";
    public static final String AL_YA  = "alyA";
    public static final String AL_XB   = "alxB";
    public static final String AL_YB   = "alyB";

    ViewDraw whatIdraw;
    private Bitmap mBitmap;
    private Bitmap mTempBitmap;
    private Canvas mCanvas,mTempCanvas;
    private Paint paint;

    private ImageButton btnCarre;
    private ImageButton btnCarrePlein;
    private ImageButton btnCercle;
    private ImageButton btnCerclePlein;
    private ImageButton btnLigne;
    private ImageButton btnUndo;
    private ImageButton btnChoixCouleur;

    private ArrayList<String> alFormes;
    private ArrayList<Integer> alCoul;
    private ArrayList<Integer> alXA;
    private ArrayList<Integer> alXB;
    private ArrayList<Integer> alYA;
    private ArrayList<Integer> alYB;

    private int height;
    private int width;
    private int defaultColor;

    private LinearLayout linearLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dessin);
        this.paint = new Paint();

        FrameLayout container = findViewById(R.id.custom_view_container);
        whatIdraw = new ViewDraw(this);
        container.addView(whatIdraw);

        /*
        * Nouveau ou Reprendre
        * */
        Intent i = getIntent();
        String type = i.getStringExtra("TYPE");
        if (type.equals("NEW"))
        {
            this.alFormes = new ArrayList<String>();
            this.alCoul = new ArrayList<Integer>();
            this.alXA = new ArrayList<Integer>();
            this.alYA = new ArrayList<Integer>();
            this.alXB = new ArrayList<Integer>();
            this.alYB = new ArrayList<Integer>();
        }
        if (type.equals("RESUME"))
        {
            loadData();
        }

        // Set up the Bitmap object to store the drawing
        this.width = getResources().getDisplayMetrics().widthPixels;
        this.height = getResources().getDisplayMetrics().heightPixels;
        this.initGraphics();


        /* Récupère les IDS des boutons de la vue */
        this.btnUndo = findViewById(R.id.btnUndo);
        this.btnCarre = findViewById(R.id.btnCarre);
        this.btnCarre = findViewById(R.id.btnCarrePlein);
        this.btnCercle = findViewById(R.id.btnCercle);
        this.btnCercle = findViewById(R.id.btnCerclePlein);
        this.btnLigne = findViewById(R.id.btnLigne);
        this.btnChoixCouleur = findViewById(R.id.btnChromatique);

        this.linearLayout = findViewById(R.id.rootId);
        this.defaultColor = ContextCompat.getColor(DessinActivity.this, R.color.white);

        btnChoixCouleur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
    }

    private void openColorPicker(){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                whatIdraw.setColor(color);
            }
        });
        colorPicker.show();
    }

    public void initGraphics()
    {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mTempBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mTempCanvas = new Canvas(mTempBitmap);
    }

    public void onSaveInstanceState(Bundle bagOfData)
    {
        bagOfData.putStringArrayList("formes", this.alFormes);
        bagOfData.putIntegerArrayList("coul", this.alCoul);
        bagOfData.putIntegerArrayList("xa", this.alXA);
        bagOfData.putIntegerArrayList("ya", this.alYA);
        bagOfData.putIntegerArrayList("xb", this.alXB);
        bagOfData.putIntegerArrayList("yb", this.alYB);
        super.onSaveInstanceState(bagOfData);
    }

    public void onRestoreInstanceState(Bundle bagOfData)
    {
        super.onRestoreInstanceState(bagOfData);
        this.alFormes = bagOfData.getStringArrayList("formes");
        this.alCoul = bagOfData.getIntegerArrayList("coul");
        this.alXA = bagOfData.getIntegerArrayList("xa");
        this.alYA = bagOfData.getIntegerArrayList("ya");
        this.alXB = bagOfData.getIntegerArrayList("xb");
        this.alYB = bagOfData.getIntegerArrayList("yb");

        this.whatIdraw.recreate(this.alFormes, this.alCoul, this.alXA, this.alYA, this.alXB, this.alYB);
    }

    public void addForme(String forme){ this.alFormes.add(forme);}
    public void addCoul(Integer coul){ this.alCoul.add(coul);}
    public void addXA(Integer x){ this.alXA.add(x);}
    public void addXB(Integer x){ this.alXB.add(x);}
    public void addYA(Integer y){ this.alYA.add(y);}
    public void addYB(Integer y){ this.alYB.add(y);}

    public void quitter(View view)
    {
        Log.d("TAG","saveDATA");
        saveData();
        finish();
    }
    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor     = sharedPreferences.edit();
        try {

            editor.putString (AL_FORMES,ObjectSerializer.serialize(this.alFormes));
            editor.putString (AL_COUL,ObjectSerializer.serialize(this.alCoul));
            editor.putString (AL_XA,ObjectSerializer.serialize(this.alXA));
            editor.putString (AL_YA,ObjectSerializer.serialize(this.alYA));
            editor.putString (AL_XB,ObjectSerializer.serialize(this.alXB));
            editor.putString (AL_YB,ObjectSerializer.serialize(this.alYB));

        }
        catch (IOException e){
            e.printStackTrace();
        }
        editor.commit();


    }
    public void loadData() {
        SharedPreferences prefs = getSharedPreferences(SHARE_PREFS, Context.MODE_PRIVATE);
        Log.d("TAG","LOAD DATA");

        try {
            this.alFormes = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString(AL_FORMES,ObjectSerializer.serialize(new ArrayList<String>())));
            this.alCoul = (ArrayList<Integer>) ObjectSerializer.deserialize(prefs.getString(AL_COUL,ObjectSerializer.serialize(new ArrayList<Integer>())));
            this.alXA = (ArrayList<Integer>) ObjectSerializer.deserialize(prefs.getString(AL_XA,ObjectSerializer.serialize(new ArrayList<Integer>())));
            this.alYA = (ArrayList<Integer>) ObjectSerializer.deserialize(prefs.getString(AL_YA,ObjectSerializer.serialize(new ArrayList<Integer>())));
            this.alXB = (ArrayList<Integer>) ObjectSerializer.deserialize(prefs.getString(AL_XB,ObjectSerializer.serialize(new ArrayList<Integer>())));
            this.alYB = (ArrayList<Integer>) ObjectSerializer.deserialize(prefs.getString(AL_YB,ObjectSerializer.serialize(new ArrayList<Integer>())));


        }
        catch (IOException|ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.whatIdraw.recreate(this.alFormes, this.alCoul, this.alXA, this.alYA, this.alXB, this.alYB);

    }
    public void effacerTout(View view)
    {
        this.alFormes = new ArrayList<String>();
        this.alCoul = new ArrayList<Integer>();
        this.alXA = new ArrayList<Integer>();
        this.alYA = new ArrayList<Integer>();
        this.alXB = new ArrayList<Integer>();
        this.alYB = new ArrayList<Integer>();
        this.initGraphics();
        this.whatIdraw.invalidate();
    }

    public void undo(View view)
    {
        int indice = this.alFormes.size()-1;
        if ( indice >= 0 ) {
            this.alFormes.remove(indice);
            this.alCoul.remove(indice);
            this.alXA.remove(indice);
            this.alYA.remove(indice);
            this.alXB.remove(indice);
            this.alYB.remove(indice);
            this.initGraphics();
            this.whatIdraw.recreate(this.alFormes, this.alCoul, this.alXA, this.alYA, this.alXB, this.alYB);
        }
    }

    public void setCarre (View view) {this.whatIdraw.setCarre(view);}

    public void setCarrePlein (View view) {this.whatIdraw.setCarrePlein(view);}

    public void setCercle (View view) {this.whatIdraw.setCercle(view);}

    public void setCerclePlein (View view) {this.whatIdraw.setCerclePlein(view);}

    public void setLigne (View view) {this.whatIdraw.setLigne(view);}

    class ViewDraw extends View implements View.OnTouchListener, View.OnClickListener {
        private final int TOOL_RECTANGLE = 1;
        private final int TOOL_ROUND = 2;
        private final int TOOL_GOMME = 3;
        private final int TOOL_TRACE = 4;
        private int currentTool;

        private float xA;
        private float yA;
        private float xB;
        private float yB;
        private ArrayList<String> alFormes;
        private ArrayList<Integer> alCoul;
        private ArrayList<Integer> alXA;
        private ArrayList<Integer> alXB;
        private ArrayList<Integer> alYA;
        private ArrayList<Integer> alYB;
        private boolean isDrawing = false;
        private int currentColor = Color.BLACK;
        private boolean recreate= false;
        private String style;
        private int w;
        private int h;

        public ViewDraw(Context context) {
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

        public boolean recreate(ArrayList<String> alFormes, ArrayList<Integer> alCoul, ArrayList<Integer> alXA, ArrayList<Integer> alYA, ArrayList<Integer> alXB, ArrayList<Integer> alYB) {

            this.alFormes = alFormes;
            this.alCoul = alCoul;
            this.alXA = alXA;
            this.alYA = alYA;
            this.alXB = alXB;
            this.alYB = alYB;

            this.recreate=true;
            this.invalidate();
            return false;
        }



        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mTempBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            this.w =w;
            this.h =h;
            mTempCanvas = new Canvas(mTempBitmap);
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            float touchX = event.getX();
            float touchY = event.getY();
            //respond to down, move and up events
            recreate = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isDrawing = true;
                    xA = touchX;
                    yA = touchY;
                    break;

                case MotionEvent.ACTION_MOVE:
                    isDrawing = true;
                    xB = touchX;
                    yB = touchY;
                    switch (currentTool)
                    {
                        case TOOL_RECTANGLE:
                            Paint paintTemp = new Paint();
                            mTempBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                            mTempCanvas = new Canvas(mTempBitmap);
                            paintTemp.setColor(Color.parseColor("#330000FF"));
                            mTempCanvas.drawRect(xA,yA,xB,yB,paintTemp);
                            break;
                        case TOOL_TRACE:
                            Paint paintTemp1 = new Paint();
                            mTempBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                            mTempCanvas = new Canvas(mTempBitmap);
                            paintTemp1.setColor(Color.parseColor("#330000FF"));
                            mTempCanvas.drawLine(xA,yA, xB,yB, paintTemp1);
                            break;
                        case TOOL_ROUND:
                            Paint paintTemp2 = new Paint();
                            mTempBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                            mTempCanvas = new Canvas(mTempBitmap);
                            paintTemp2.setColor(Color.parseColor("#330000FF"));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mTempCanvas.drawOval(xA,yA,xB,yB,paintTemp2);
                            }
                            break;

                    }
                    invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                    isDrawing = false;

                    switch (currentTool) {
                        case TOOL_RECTANGLE:
                            mTempBitmap.eraseColor(Color.TRANSPARENT);
                            paint.setColor(this.currentColor);
                            mCanvas.drawRect(xA,yA,xB,yB,paint);
                            if ( style=="vide" )
                                addForme("rectangle");
                            else if ( style=="plein" )
                                addForme("rectangleplein");

                            addCoul(currentColor);
                            addXA((int) xA);
                            addXB((int) xB);
                            addYA((int) yA);
                            addYB((int) yB);
                            break;

                        case TOOL_TRACE:
                            paint.setColor(this.currentColor);
                            mCanvas.drawLine(xA,yA, xB,yB, paint);
                            addForme("ligne");
                            addCoul(currentColor);
                            addXA((int) xA);
                            addXB((int) xB);
                            addYA((int) yA);
                            addYB((int) yB);
                            break;

                        case TOOL_ROUND:
                            paint.setColor(this.currentColor);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mCanvas.drawOval(xA,yA,xB,yB,paint);
                            }
                            if ( style=="vide" )
                                addForme("cercle");
                            else if ( style=="plein" )
                                addForme("cercleplein");

                            addCoul(currentColor);
                            addXA((int) xA);
                            addXB((int) xB);
                            addYA((int) yA);
                            addYB((int) yB);
                            break;

                    }
                    xA = xB = touchX;
                    yA = yB = touchY;

                    break;

                default:
                    break;
            }
            //redraw
            this.invalidate();
            return false;
        }

        @Override
        protected void onDraw(Canvas canvas) {

            if (!recreate) {
                canvas.drawBitmap(mBitmap, 0 , 0, paint);
                if (isDrawing)
                {
                    switch (currentTool)
                    {
                        case TOOL_RECTANGLE:
                            canvas.drawBitmap(mTempBitmap, 0, 0, null);
                            break;

                        case TOOL_ROUND:
                            canvas.drawBitmap(mTempBitmap, 0, 0, null);
                            break;

                        case TOOL_TRACE:
                            canvas.drawBitmap(mTempBitmap, 0, 0, null);
                            break;
                    }
                }
            }

            if (recreate) {
                for (int cpt = 0; cpt < alFormes.size(); cpt++) {
                    switch (alFormes.get(cpt)) {
                        case "rectangle":
                            paint.setColor(alCoul.get(cpt));
                            paint.setStyle(Paint.Style.STROKE);
                            mCanvas.drawRect((float) alXA.get(cpt), (float) alYA.get(cpt), (float) alXB.get(cpt), (float) alYB.get(cpt), paint);
                            break;

                        case "rectangleplein":
                            paint.setColor(alCoul.get(cpt));
                            paint.setStyle(Paint.Style.FILL_AND_STROKE);
                            mCanvas.drawRect((float) alXA.get(cpt), (float) alYA.get(cpt), (float) alXB.get(cpt), (float) alYB.get(cpt), paint);
                            break;

                        case "cercle":
                            paint.setColor(alCoul.get(cpt));
                            paint.setStyle(Paint.Style.STROKE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mCanvas.drawOval((float) alXA.get(cpt), (float) alYA.get(cpt), (float) alXB.get(cpt), (float) alYB.get(cpt), paint);
                            }
                            break;

                        case "cercleplein":
                            paint.setColor(alCoul.get(cpt));
                            paint.setStyle(Paint.Style.FILL_AND_STROKE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mCanvas.drawOval((float) alXA.get(cpt), (float) alYA.get(cpt), (float) alXB.get(cpt), (float) alYB.get(cpt), paint);
                            }
                            break;

                        case "ligne":
                            paint.setColor(alCoul.get(cpt));
                            mCanvas.drawLine((float) alXA.get(cpt), (float) alYA.get(cpt), (float) alXB.get(cpt), (float) alYB.get(cpt), paint);
                            break;

                        default:
                            break;
                    }
                    canvas.drawBitmap(mBitmap, 0 , 0, paint);
                }
                recreate = false;
            }
        }

        @Override
        public void onClick(View view) {Log.d("Tag", "On Click");}

        public void setCarre(View view) {
            this.currentTool = TOOL_RECTANGLE;
            paint.setStyle(Paint.Style.STROKE);
            style = "vide";
        }

        public void setCarrePlein(View view) {
            this.currentTool = TOOL_RECTANGLE;
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            style = "plein";
        }

        public void setCercle(View view) {
            this.currentTool = TOOL_ROUND;
            paint.setStyle(Paint.Style.STROKE);
            style = "vide";
        }

        public void setCerclePlein(View view) {
            this.currentTool = TOOL_ROUND;
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            style = "plein";
        }

        public void setLigne(View view) {
            this.currentTool = TOOL_TRACE;
        }

        public void setColor(int color) {
            this.currentColor = color;
            paint.setColor(color);
        }
    }

    public void rouge(View view) {
        whatIdraw.setColor(Color.RED);
    }

    public void vert(View view) {
        whatIdraw.setColor(Color.GREEN);
    }

    public void bleu(View view) {
        whatIdraw.setColor(Color.BLUE);
    }

    public void jaune(View view) {
        whatIdraw.setColor(Color.YELLOW);
    }
    public void noir(View view) {
        whatIdraw.setColor(Color.BLACK);
    }
}