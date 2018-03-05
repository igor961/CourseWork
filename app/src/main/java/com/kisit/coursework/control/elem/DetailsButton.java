package com.kisit.coursework.control.elem;

import android.content.res.Resources;
import android.graphics.Rect;

public class DetailsButton extends AbstractButton {
    public DetailsButton(Resources resources, int res, Resources.Theme theme, float width, float height) {
        super(resources, res, theme, new Rect(
                (int) (width / 10),
                (int) (height - width / 5),
                (int) (width / 5),
                (int) (height - width / 10)
        ));

    }
}
