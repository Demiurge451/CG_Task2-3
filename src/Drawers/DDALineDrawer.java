package Drawers;

import Drawers.Interfaces.LineDrawer;
import Drawers.Interfaces.PixelDrawer;

import java.awt.*;

public class DDALineDrawer implements LineDrawer {
    private PixelDrawer pd;

    public DDALineDrawer(PixelDrawer pd) {
        this.pd = pd;
    }


    @Override
    public void drawLine(int x1, int y1, int x2, int y2, Color c) {
        int countSteps = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        double dx = (double) (x2 - x1) / countSteps;
        double dy = (double) (y2 - y1) / countSteps;

        double x = x1;
        double y = y1;
        for (int i = 1; i <= countSteps; i++) {
            x += dx;
            y += dy;
            pd.setPixel((int) x, (int) y, c);
        }
    }
}
