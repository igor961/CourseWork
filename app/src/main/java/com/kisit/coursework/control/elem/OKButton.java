package com.kisit.coursework.control.elem;

import android.content.res.Resources;
import android.graphics.Rect;

/**
 * Created by Игорь on 21.12.2017.
 */
public class OKButton extends AbstractButton {

    public OKButton(Resources resources, int res, Resources.Theme theme, float width, float height) {
        super(resources, res, theme, new Rect(
                (int) (width - width / 5),
                (int) (height - width / 5),
                (int) (width - width / 10),
                (int) (height - width / 10)));
    }
}
