package ru.aleksandrorlov.crazyhamster.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;

/**
 * Created by alex on 22.05.17.
 */

public class MyLeadingMarginSpan implements LeadingMarginSpan.LeadingMarginSpan2 {
    private int margin;
    private int lines;

    public MyLeadingMarginSpan(int margin, int lines) {
        this.margin = margin;
        this.lines = lines;
    }

    @Override
    public int getLeadingMarginLineCount() {
        return lines;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        if (first) {
            return margin;
        } else {
            return 0;
        }
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline,
                                  int bottom, CharSequence text, int start, int end, boolean first,
                                  Layout layout) {

    }
}
