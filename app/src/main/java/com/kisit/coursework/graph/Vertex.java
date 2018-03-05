package com.kisit.coursework.graph;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Игорь on 25.01.2017.
 */
public class Vertex {
    private float[] coords = new float[2];
    private final float WIDTH;
    public final int counter;

    private RectF r;

    public Vertex(float x, float y, int number, float width) {
        WIDTH = width;
        coords[0] = x;
        coords[1] = y;
        float dev = width/15;
        r = new RectF(x-dev, y-dev, x+dev, y+dev);
        counter = number;
    }

    public float[] getV() {
        return coords;
    }

    public boolean verV(float x, float y) {
        if (r.contains(x, y)) {
            return true;
        }
        return false;
    }
    public RectF getR() {
        return r;
    }

    public void draw(Canvas c, int radius) {
        Paint p = new Paint();
        p.setStrokeWidth(2);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLACK);
        c.drawCircle(coords[0], coords[1], radius, p);
        p.setTextSize(18);
        c.drawText(String.valueOf(counter), coords[0], coords[1], p);
    }
}
