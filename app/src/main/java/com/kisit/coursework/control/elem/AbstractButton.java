package com.kisit.coursework.control.elem;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

public abstract class AbstractButton {

    private final Resources.Theme theme;
    private final int res;
    private Resources resources;
    private Rect bounds;

    public AbstractButton(Resources resources, int res, Resources.Theme theme, Rect bounds) {
        this.res = res;
        this.theme = theme;
        this.resources = resources;
        this.bounds = bounds;
    }

    public void draw(Canvas canvas) {
        Drawable d = ResourcesCompat.getDrawable(this.resources, this.res, this.theme);
        d.setBounds(this.bounds);
        d.draw(canvas);
    }

    public Rect getZone() {
        return this.bounds;
    }

    public boolean clicked(float x, float y) {
        return this.getZone().contains((int) x, (int) y);
    }

    public boolean clicked(int x, int y) {
        return this.getZone().contains(x, y);
    }
}
