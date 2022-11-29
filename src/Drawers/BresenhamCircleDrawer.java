package Drawers;


import Drawers.Interfaces.CircleDrawer;
import Drawers.Interfaces.LineDrawer;
import Drawers.Interfaces.PixelDrawer;

import java.awt.*;

import util.*;

public class BresenhamCircleDrawer implements CircleDrawer {
    private final PixelDrawer pd;
    private final LineDrawer ld;


    public BresenhamCircleDrawer(PixelDrawer pd, LineDrawer ld) {
        this.pd = pd;
        this.ld = ld;
    }

    @Override
    public void drawCircleSegment(int x0, int y0, int radius, double startAngle, double finishAngle, Color color) {
        int x = 0;
        int y = radius;
        int delta = 1 - 2 * radius;
        int error;

        if (Math.abs(startAngle - finishAngle) < 2 * Math.PI) {
            drawChord(x0, y0, radius, startAngle, finishAngle, color);
        }

        while (y >= 0) {
            setPixelIfInArc(x0 + x, y0 + y, x0, y0, radius, startAngle, finishAngle, color);
            setPixelIfInArc(x0 + x, y0 - y, x0, y0, radius, startAngle, finishAngle, color);
            setPixelIfInArc(x0 - x, y0 + y, x0, y0, radius, startAngle, finishAngle, color);
            setPixelIfInArc(x0 - x, y0 - y, x0, y0, radius, startAngle, finishAngle, color);
            error = 2 * (delta + y) - 1;
            if (delta < 0 && error <= 0) {
                x++;
                delta += 2 * x + 1;
                continue;
            }
            error = (2 * delta - x) - 1;
            if (delta > 0 && error > 0) {
                y--;
                delta += 1 - 2 * y;
                continue;
            }
            x++;
            y--;
            delta += 2 * (x - y);
        }
    }

    private void drawChord(int x0, int y0, int radius, double startAngle, double finishAngle, Color color) {
        int x1 = (int) (Math.cos(startAngle) * radius) + x0;
        int y1 = (int) (Math.sin(startAngle) * radius) + y0;
        int x2 = (int) (Math.cos(finishAngle) * radius) + x0;
        int y2 = (int) (Math.sin(finishAngle) * radius) + y0;
        ld.drawLine(x1, y1, x2, y2, color);
    }



    private void setPixelIfInArc(int x, int y, int x0, int y0, int radius, double startAngle, double finishAngle, Color color) {
        if (PositionPointOnSegment.isPointOnArc(x, y, x0, y0, radius, startAngle, finishAngle)) {
            pd.setPixel(x, y, color);
        }
    }
}
