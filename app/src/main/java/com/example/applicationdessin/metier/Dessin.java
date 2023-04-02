package com.example.applicationdessin.metier;

public class Dessin
{
    private int type;
    private float fromX;
    private float fromY;
    private float toX;
    private float toY;
    private int color;
    public int getType() {
        return type;
    }
    public int getColor() {
        return color;
    }

    public float getFromX() {
        return fromX;
    }

    public float getFromY() {
        return fromY;
    }

    public float getToX() {
        return toX;
    }

    public float getToY() {
        return toY;
    }

    public Dessin(int type, float fromX, float fromY, float toX, float toY, int color) {
        this.type = type;
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.color = color;
    }
}
