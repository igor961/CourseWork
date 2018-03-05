package com.kisit.coursework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.kisit.coursework.graph.GView;

/**
 * Created by Игорь on 20.12.2017.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        final boolean finished = i.getBooleanExtra("finished", false);
        final boolean oriented = i.getBooleanExtra("oriented", false);
        final boolean showTextLabels = i.getBooleanExtra("textLabels", false);
        if (finished) {
            setContentView(new GView(getApplicationContext(), GView.Mode.OUTPUT_MODE, oriented, showTextLabels));
        } else setContentView(new GView(getApplicationContext(), GView.Mode.INPUT_MODE, oriented, showTextLabels));
    }
}
