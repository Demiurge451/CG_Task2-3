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
    public void drawLine(int x1, int y1, int x2, int y2, Color c) {
        boolean steep = Math.abs(y2 - y1) > Math.abs(x2- x1);
        if (x1 == x2){
            for (int i = Math.min(y2,y1); i <= Math.max(y2, y1) ; i++) {
                plot(x1, i, 1, c);
            }
            return;
        }
        if (y1 == y2){
            for (int i = Math.min(x2,x1); i <= Math.max(x2, x1) ; i++) {
                plot(i, y1, 1, c);
            }
            return;
        }
        if (steep){
            int tmp = x1;
            x1 = y1;
            y1 = tmp;
            tmp = x2;
            x2 = y2;
            y2 = tmp;
        }
        if (x1 > x2){
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
            tmp = y1;
            y1 = y2;
            y2 = tmp;
        }
        int dx = x2 - x1;
        int dy = y2 - y1;
        double gradient;
        if (dx == 0){
            gradient = 1.0;
        }
        else {
            gradient = (double) dy/ (double) dx;
        }

        double xend = round(x1);
        double yend = y1 + gradient * (xend - x1);
        double xgap = rfpart(x1 + 0.5);
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
        xend = round(x2);
        yend = y2 + gradient * (xend - x2);
        xgap = fpart(x2 + 0.5);
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