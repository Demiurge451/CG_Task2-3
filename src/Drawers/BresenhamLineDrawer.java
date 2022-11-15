package Drawers;

import Drawers.Interfaces.LineDrawer;
import Drawers.Interfaces.PixelDrawer;

import java.awt.*;

public class BresenhamLineDrawer implements LineDrawer {
    private PixelDrawer pd;

    public BresenhamLineDrawer(PixelDrawer pd) {
        this.pd = pd;
    }


    @Override
    public void drawLine(int x1, int y1, int x2, int y2, Color c) {
        int dx = x2 - x1;
        int dy = y2 - y1;

        int incX = sign(dx);
        int incY = sign(dy);
        dx = Math.abs(dx);
        dy = Math.abs(dy);

        pd.setPixel(x1, y1, c);
        pd.setPixel(x2, y2, c);
        int pdx, pdy, es, el;
        if (dx > dy) {
            pdx = incX;
            pdy = 0;
            es = dy;
            el = dx;
        } else {
            pdx = 0;
            pdy = incY;
            es = dx;
            el = dy;
        }

        int x = x1;
        int y = y1;
        int err = el / 2;
        for (int i = 0; i < el; i++) {
            err -= es;
            if (err < 0) {
                err += el;
                x += incX;
                y += incY;
            } else {
                x += pdx;
                y += pdy;
            }
            pd.setPixel(x, y, c);
        }


    }

    private int sign(int x) {
        return (x > 0) ? 1 : (x < 0) ? -1 : 0;
    }


}
