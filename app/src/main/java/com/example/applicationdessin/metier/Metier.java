package com.example.applicationdessin.metier;

import android.graphics.Path;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Metier
{

    private ArrayList<Dessin> lstDessin = new ArrayList<Dessin>();
    private ArrayList<Path>   lstPath   = new ArrayList<Path>();
    private ArrayList<Object> lstObject = new ArrayList<>();
    private Object bufferedObject;
    public ArrayList<Path> getLstPath() {
        return lstPath;
    }

    public Metier (){
        
    }
    public void addDessin(int type, float fromX,float fromY, float toX, float toY,int color){
        Dessin dessin = new Dessin(type, fromX, fromY, toX, toY, color);
        lstDessin.add(dessin);
        lstObject.add(dessin);
    }
    public void addPath (Path path){
        this.lstPath.add(path);
        lstObject.add (path);
    }
    public void undo (){

    }
    public ArrayList<Dessin> getLstDessin() {
        return lstDessin;
    }
}
