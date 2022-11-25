package Drawers;

import Drawers.Interfaces.LineDrawer;
import Drawers.Interfaces.PixelDrawer;

import java.awt.*;

public class WuLineDrawer implements LineDrawer {

    private PixelDrawer pd;

    public WuLineDrawer(PixelDrawer pd) {
        this.pd = pd;
    }

    @Override
    public void drawLine(int x0, int y0, int x1, int y1, Color c) {
        boolean steep = Math.abs(y1 - y0) > Math.abs(x1- x0);
        if (steep){
            int tmp = x0;
            x0 = y0;
            y0 = tmp;
            tmp = x1;
            x1 = y1;
            y1 = tmp;
        }
        if (x0 > x1){
            int tmp = x0;
            x0 = x1;
            x1 = tmp;
            tmp = y0;
            y0 = y1;
            y1 = tmp;
        }
        int dx = x1 - x0;
        int dy = y1 - y0;
        double gradient;
        if (dx == 0){
            gradient = 1.0;
        }
        else {
            gradient = (double) dy/ (double) dx;
        }

        double xend = round(x0);
        double yend = y0 + gradient * (xend - x0);
        double xgap = rfpart(x0 + 0.5);
        double xpxl1 = xend;
        double ypxl1 = ipart(yend);
        if (steep){
            plot(ypxl1, xpxl1, rfpart(yend) * xgap , c);
            plot(ypxl1+1, xpxl1, fpart(yend) * xgap , c);
        }
        else {
            plot(xpxl1, ypxl1, rfpart(yend) * xgap, c);
            plot(xpxl1, ypxl1 + 1, fpart(yend) * xgap, c);
        }
        double intery = yend + gradient;
        xend = round(x1);
        yend = y1 + gradient * (xend - x1);
        xgap = fpart(x1 + 0.5);
        double xpxl2 = xend;
        double ypxl2 = ipart(yend);
        if (steep){
            plot(ypxl2 , xpxl2, rfpart(yend) * xgap, c);
            plot(ypxl2+1, xpxl2, fpart(yend) * xgap, c);
        }
        else {
            plot(xpxl2, ypxl2, rfpart(yend) * xgap,c);
            plot(xpxl2, ypxl2+1, fpart(yend) * xgap,c);
        }
        if (steep){
            for (double x = xpxl1 + 1; x <= xpxl2-1; x++){
                plot(ipart(intery), x, rfpart(intery),c);
                plot(ipart(intery)+1, x, fpart(intery), c);
                intery = intery + gradient;
            }
        }
        else {
            for (double x = xpxl1 + 1; x <= xpxl2-1; x++){
                plot(x, ipart(intery), rfpart(intery),c);
                plot(x, ipart(intery)+1, fpart(intery),c);
                intery = intery + gradient;
            }
        }
    }

    private void plot(double x, double y, double a, Color c){
        Color color = rgbaToRgb(c, a);
        pd.setPixel((int)x,(int)y,color);
    }

    private double ipart(double x){
        return Math.floor(x);
    }

    private double round(double x){
        return ipart(x + 0.5);
    }

    private double fpart(double x){
        return x - Math.floor(x);
    }

    private double rfpart(double x){
        return 1 - fpart(x);
    }

    private Color rgbaToRgb(Color c, double a){
        return new Color( 255 -(int) ((255 - c.getRed()) * a), 255 - (int) ((255 -c.getGreen()) * a), 255 - (int) ((255 -c.getBlue()) * a));
    }
}