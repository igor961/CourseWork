package com.kisit.coursework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;
import com.kisit.coursework.graph.Edge;
import com.kisit.coursework.graph.GView;
import com.kisit.coursework.graph.Graph;
import com.kisit.coursework.graph.Vertex;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Want to go back?", Snackbar.LENGTH_LONG)
                        .setAction("Back", new View.OnClickListener() {
                            @Override
                            public void onClick(View e) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    finishAndRemoveTask();
                                } else {
                                    finish();
                                }
                            }
                        }).show();
            }
        });

        final double[][] matrix = Graph.getInstance().getAdjMatrix();
        final int size = matrix.length;
        GridLayout matrixView = new GridLayout(this);
        matrixView.setRowCount(size);
        matrixView.setColumnCount(size);
        matrixView.setAlignmentMode(View.TEXT_ALIGNMENT_CENTER);
        final TextView[][] matrixViewItems = new TextView[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrixViewItems[i][j] = new TextView(this);
                DisplayMetrics metrics = new DisplayMetrics();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
                    matrixViewItems[i][j].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                } else
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                matrixViewItems[i][j].setTextSize(18);
                matrixViewItems[i][j].setWidth(metrics.widthPixels / size);
                if (matrix[i][j] != 0)
                    if (matrix[i][j] == Math.rint(matrix[i][j]))
                        matrixViewItems[i][j].setText(String.valueOf((int) matrix[i][j]));
                    else matrixViewItems[i][j].setText(String.valueOf(matrix[i][j]));
                else
                    matrixViewItems[i][j].setText("-");
                matrixView.addView(matrixViewItems[i][j]);
            }
        }
        final FlexboxLayout detailsContainer = (FlexboxLayout) findViewById(R.id.details_container);
        detailsContainer.setFlexDirection(FlexDirection.COLUMN);
        detailsContainer.setJustifyContent(JustifyContent.SPACE_AROUND);
        detailsContainer.setAlignItems(AlignItems.CENTER);

        TextView textDetails = new TextView(this);
        textDetails.setTextSize(18);
        String sorted = Graph.getInstance().getSortedAsString();
        textDetails.append("Sorted: " + sorted + "\r\n");

        final Button getMSTBtn = new Button(this);
        getMSTBtn.setText("Get most spanning tree");

        detailsContainer.addView(textDetails);
        detailsContainer.addView(matrixView);
        if (!getIntent().getBooleanExtra("oriented", true)) {
            detailsContainer.addView(getMSTBtn);
            getMSTBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strGraph = "";
                    for (Edge e : GView.edges) {
                        double weight = Graph.getInstance().getMST()[e.v1][e.v2];
                        if (weight != 0) {
                            strGraph += e.v1 + "--" + e.v2 + ";";
                            Vertex gV1 = GView.vertices.get(e.v1);
                            Vertex gV2 = GView.vertices.get(e.v2);
                            GView.mstEdges.add(new Edge(gV1, gV2, weight));
                        }
                    }

                    ImageView mstImageView = new ImageView(v.getRootView().getContext());
                    GView tmpGView = new GView(v.getRootView().getContext(),
                            GView.Mode.MST_MODE, false, true);
                    int width = v.getRootView().getWidth();
                    int height = v.getRootView().getHeight();
                    Bitmap bmp = loadBitmapFromView(tmpGView, width, height - height / 5);
                    if (bmp != null) {
                        mstImageView.setImageBitmap(bmp);
                        detailsContainer.addView(mstImageView);
                    }
                    v.setVisibility(View.GONE);
                }
            });

        }
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, width, height);
        v.draw(c);
        return trimBitmap(b);
    }

    public static Bitmap trimBitmap(Bitmap bmp) {
        int imgHeight = bmp.getHeight();
        int imgWidth = bmp.getWidth();


        //TRIM WIDTH - LEFT
        int startWidth = 0;
        for (int x = 0; x < imgWidth; x++) {
            if (startWidth == 0) {
                for (int y = 0; y < imgHeight; y++) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        startWidth = x;
                        break;
                    }
                }
            } else break;
        }


        //TRIM WIDTH - RIGHT
        int endWidth = 0;
        for (int x = imgWidth - 1; x >= 0; x--) {
            if (endWidth == 0) {
                for (int y = 0; y < imgHeight; y++) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        endWidth = x;
                        break;
                    }
                }
            } else break;
        }


        //TRIM HEIGHT - TOP
        int startHeight = 0;
        for (int y = 0; y < imgHeight; y++) {
            if (startHeight == 0) {
                for (int x = 0; x < imgWidth; x++) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        startHeight = y;
                        break;
                    }
                }
            } else break;
        }


        //TRIM HEIGHT - BOTTOM
        int endHeight = 0;
        for (int y = imgHeight - 1; y >= 0; y--) {
            if (endHeight == 0) {
                for (int x = 0; x < imgWidth; x++) {
                    if (bmp.getPixel(x, y) != Color.TRANSPARENT) {
                        endHeight = y;
                        break;
                    }
                }
            } else break;
        }


        return Bitmap.createBitmap(
                bmp,
                startWidth,
                startHeight,
                endWidth - startWidth,
                endHeight - startHeight
        );

    }
}
