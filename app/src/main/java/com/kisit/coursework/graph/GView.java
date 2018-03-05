package com.kisit.coursework.graph;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import com.kisit.coursework.AdjMatrix;
import com.kisit.coursework.DetailsActivity;
import com.kisit.coursework.R;
import com.kisit.coursework.control.elem.AbstractButton;
import com.kisit.coursework.control.elem.DetailsButton;
import com.kisit.coursework.control.elem.OKButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Игорь on 25.01.2017.
 */
public class GView extends View {

    public enum Mode {
        INPUT_MODE, OUTPUT_MODE, MST_MODE
    }

    public static List<Vertex> vertices = new ArrayList<>();
    public static List<Edge> edges = new ArrayList<>();
    public static List<Edge> mstEdges = new ArrayList<>();
    private boolean oriented;
    private RectF drawingZone;
    private AbstractButton okBtn;
    private AbstractButton detailsBtn;
    private int vertexCounter = 0;
    private Paint p;

    private Mode currentMode;
    private boolean showTextLabels;

    public GView(Context context, Mode mode, boolean oriented, boolean showTextLabels) {
        super(context);
        this.oriented = oriented;
        this.showTextLabels = showTextLabels;
        this.currentMode = mode;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int vertRadius = getWidth() / 25;
        if (!currentMode.equals(Mode.MST_MODE)) {
            okBtn.draw(canvas);
            if (currentMode.equals(Mode.OUTPUT_MODE)) {
                detailsBtn = new DetailsButton(getResources(), R.mipmap.ic_details, null, getWidth(), getHeight());
                detailsBtn.draw(canvas);
            }
            if (currentMode == Mode.OUTPUT_MODE && (edges.size() > 0)) {
                for (Edge e : edges) {
                    if (oriented)
                        e.drawOriented(canvas, vertRadius, showTextLabels);
                    else e.draw(canvas, vertRadius, showTextLabels);
                }
            }
        } else {

            for (Edge e : mstEdges) {
                if (oriented)
                    e.drawOriented(canvas, vertRadius, showTextLabels);
                else e.draw(canvas, vertRadius, showTextLabels);
            }
        }
        if (vertices.size() > 0)
            for (Vertex v : vertices) {
                v.draw(canvas, vertRadius);
            }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        p = new Paint();
        p.setStrokeWidth(2);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLACK);
        drawingZone = new RectF(0, 0, getWidth(), getHeight() - getHeight() / 5);
        okBtn = new OKButton(getResources(), R.mipmap.ic_ok, null, getWidth(), getHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (currentMode.equals(Mode.INPUT_MODE) && drawingZone.contains(x, y) && vertexCounter < 10) {
                    vertices.add(new Vertex(x, y, vertexCounter, getWidth()));
                    if (vertices.size() > 0) {
                        for (int i = 0; i < vertices.size() - 1; i++) {
                            if (vertices.get(i).verV(x, y)) {
                                vertices.remove(vertexCounter);
                                vertexCounter--;
                            }
                        }
                    }
                    vertexCounter++;
                }
                if (okBtn.clicked(x, y)) {
                    if (currentMode.equals(Mode.INPUT_MODE)) {
                        Intent i = new Intent(getContext(), AdjMatrix.class);
                        i.putExtra("size", vertexCounter);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (vertices.size() > 2)
                            getContext().startActivity(i);
                    } else {
                        currentMode = Mode.INPUT_MODE;
                        edges.clear();
                        vertices.clear();
                    }
                }
                if (currentMode.equals(Mode.OUTPUT_MODE)) {
                    if (detailsBtn.clicked(x, y)) {
                        Intent i = new Intent(getContext(), DetailsActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("oriented", oriented);
                        getContext().startActivity(i);
                    }
                }
                invalidate();
                break;
            }
        }
        return true;
    }
}
