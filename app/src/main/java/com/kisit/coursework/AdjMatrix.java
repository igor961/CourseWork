package com.kisit.coursework;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.*;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;
import com.kisit.coursework.graph.Graph;

public class AdjMatrix extends AppCompatActivity {
    private double[][] matrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView parentLayout = new ScrollView(this);
        parentLayout.setOverScrollMode(View.SCROLL_AXIS_VERTICAL);
        FlexboxLayout mainLayout = new FlexboxLayout(this);
        mainLayout.setFlexDirection(FlexDirection.COLUMN);
        mainLayout.setJustifyContent(JustifyContent.SPACE_AROUND);
        mainLayout.setAlignItems(AlignItems.CENTER);
        GridLayout layout = new GridLayout(getApplicationContext());
        Intent i = getIntent();
        final int size = i.getIntExtra("size", 0);
        layout.setRowCount(size);
        layout.setColumnCount(size);
        final EditText[][] val = new EditText[size][size];
        final int length = size * size;
        for (int k = 0; k < size; k++) {
            for (int j = 0; j < size; j++) {
                val[k][j] = new EditText(this);
                val[k][j].setMaxLines(1);
                val[k][j].setInputType(InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL);
                val[k][j].setTextSize(18);
                DisplayMetrics metrics = new DisplayMetrics();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                    getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
                else
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                val[k][j].setWidth(metrics.widthPixels / (size * 2));
                layout.addView(val[k][j]);
            }
        }
        LinearLayout checkBoxGroup = new LinearLayout(this);
        checkBoxGroup.setOrientation(LinearLayout.VERTICAL);
        final CheckBox orientationBox = new CheckBox(this);
        final CheckBox showTextLabelsBox = new CheckBox(this);
        orientationBox.setText("Oriented?");
        showTextLabelsBox.setText("Show text labels?");
        checkBoxGroup.addView(orientationBox);
        checkBoxGroup.addView(showTextLabelsBox);
        Button ok = new Button(this);
        ok.setText("OK");
        matrix = new double[size][size];
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int k = 0; k < size; k++) {
                    for (int j = 0; j < size; j++) {
                        String s = val[k][j].getText().toString();
                        if (!s.equals("")) {
                            matrix[k][j] = Double.parseDouble(s);
                        } else {
                            matrix[k][j] = 0;
                        }
                    }
                }
                Graph G = Graph.getInstance().init(matrix, size);
                if (!orientationBox.isChecked())
                    G.normalizeM();
                boolean oriented = orientationBox.isChecked();
                G.setWeight(oriented);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("finished", true);
                i.putExtra("oriented", oriented);
                i.putExtra("textLabels", showTextLabelsBox.isChecked());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        mainLayout.addView(layout);
        mainLayout.addView(checkBoxGroup);
        mainLayout.addView(ok);
        parentLayout.addView(mainLayout);
        setContentView(parentLayout);
    }
}
