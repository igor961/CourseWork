package com.kisit.coursework.graph;

import android.graphics.*;

/**
 * Created by Игорь on 25.01.2017.
 */
public class Edge {
    private static int counter = 0;
    public int v1, v2;
    private float[] coordsStart = new float[2];
    private float[] coordsEnd = new float[2];
    private double weight = 0;

    public Edge(Vertex v1, Vertex v2, double weight) {
        this.coordsStart = v1.getV();
        this.coordsEnd = v2.getV();
        this.v1 = v1.counter;
        this.v2 = v2.counter;
        this.weight = weight;
        counter++;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return this.weight;
    }

    public float[] getCoordsStart() {
        return coordsStart;
    }

    public float[] getCoordsEnd() {
        return coordsEnd;
    }

    public int getIndex() {
        return counter - 1;
    }

    public void drawOriented(Canvas c, int radius, boolean text) {
        Paint p = new Paint();
        p.setStrokeWidth(2);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLACK);
        Path path1 = new Path();
        path1.moveTo(coordsStart[0], coordsStart[1]);
        path1.lineTo(coordsEnd[0], coordsEnd[1]);
        PathMeasure measure = new PathMeasure(path1, false);
        float[] tanArrEnd = new float[2];
        float[] posArrEnd = new float[2];
        measure.getPosTan(measure.getLength() - radius, posArrEnd, tanArrEnd);
        Matrix matr = new Matrix();
        if (v2 - v1 > 0) {
            Path path2 = new Path();
            measure.getSegment(radius, measure.getLength() - radius, path2, true);
            matr.setSinCos(tanArrEnd[1], tanArrEnd[0], posArrEnd[0], posArrEnd[1]);
            float[] fL = new float[]{posArrEnd[0], posArrEnd[1], posArrEnd[0] - radius / 2, posArrEnd[1] - radius / 5};
            float[] sL = new float[]{posArrEnd[0], posArrEnd[1], posArrEnd[0] - radius / 2, posArrEnd[1] + radius / 5};
            matr.mapPoints(fL);
            matr.mapPoints(sL);
            path2.lineTo(fL[2], fL[3]);
            path2.moveTo(fL[0], fL[1]);
            path2.lineTo(sL[2], sL[3]);
            c.drawPath(path2, p);
            if (text) drawText(c, measure, p);
        } else {
            Path pathBack = new Path();
            float[] posArrBegin = new float[2];
            measure.getPosTan(radius, posArrBegin, null);
            pathBack.moveTo(posArrBegin[0], posArrBegin[1]);
            float[] nestP = {(posArrBegin[0] + posArrEnd[0]) / 2, (posArrBegin[1] + posArrEnd[1]) / 2};
            matr.setRotate(45, posArrBegin[0], posArrBegin[1]);
            matr.mapPoints(nestP);
            pathBack.quadTo(nestP[0], nestP[1], posArrEnd[0], posArrEnd[1]);
            float[] endTan = new float[2];
            float[] endPos = new float[2];
            PathMeasure measure2 = new PathMeasure(pathBack, false);
            measure2.getPosTan(measure2.getLength(), endPos, endTan);
            matr.reset();
            matr.setSinCos(endTan[1], endTan[0], endPos[0], endPos[1]);
            float[] fL = {endPos[0], endPos[1], endPos[0] - radius / 2, endPos[1] - radius / 5};
            float[] sL = {endPos[0], endPos[1], endPos[0] - radius / 2, endPos[1] + radius / 5};
            matr.mapPoints(fL);
            matr.mapPoints(sL);
            pathBack.lineTo(fL[2], fL[3]);
            pathBack.moveTo(fL[0], fL[1]);
            pathBack.lineTo(sL[2], sL[3]);
            c.drawPath(pathBack, p);
            if (text) drawText(c, measure2, p);
        }
    }

    public void draw(Canvas c, int radius, boolean text) {
        Paint p = new Paint();
        p.setStrokeWidth(2);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLACK);
        Path path1 = new Path();
        path1.moveTo(coordsStart[0], coordsStart[1]);
        path1.lineTo(coordsEnd[0], coordsEnd[1]);
        PathMeasure measure = new PathMeasure(path1, false);
        Path path2 = new Path();
        measure.getSegment(radius, measure.getLength() - radius, path2, true);
        c.drawPath(path2, p);
        if (text) drawText(c, measure, p);
    }

    private void drawText(Canvas c, PathMeasure measure, Paint p) {
        p.setColor(Color.RED);
        p.setTextSize(18);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        float[] posText = new float[2];
        measure.getPosTan(measure.getLength() / 2, posText, null);
        String text = String.valueOf(weight);
        if (weight == Math.rint(weight))
            text = String.valueOf((int) weight);
        c.drawText(text, posText[0], posText[1], p);
        p.reset();
    }
}
