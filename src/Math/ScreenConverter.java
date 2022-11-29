package Math;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ScreenConverter {
    private double x, y, w, h;
    private double screenHeight, screenWidth;

    public ScreenConverter(double x, double y, double w, double h, double screenWidth, double screenHeight) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public ScreenPoint realToScreen(RealPoint rp) {
        double xx = ((rp.getX() - x) / w) * screenWidth;
        double yy = ((y - rp.getY()) / h) * screenHeight;
        return new ScreenPoint((int) xx, (int) yy);
    }

    public RealPoint screenToReal(ScreenPoint sp) {
        double xx = x + sp.getX() * w / screenWidth;
        double yy = y - sp.getY() * h / screenHeight;
        return new RealPoint(xx, yy);
    }

    public List<Double> convertAngles(double startAngle, double finishAngle) {
        List<Double> angles = new ArrayList<>();
        double turn = 2 * Math.PI;
        if (Math.abs(startAngle - finishAngle) > turn) {
            angles.add(0d);
            angles.add(turn);
            return angles;
        }
        int countTurn = Math.abs((int) Math.min(startAngle / turn, finishAngle / turn));
        startAngle = startAngle > turn ? startAngle - turn * countTurn : startAngle + turn * countTurn;
        finishAngle = finishAngle > turn ? finishAngle - turn * countTurn : finishAngle + turn * countTurn;
        angles.add(-startAngle);
        angles.add(-finishAngle);
        return angles;
    }

    public int convertLength(double len) {
        return (int) Math.round(len * Math.min(screenWidth, screenHeight) / Math.min(w, h));
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(double screenHeight) {
        this.screenHeight = screenHeight;
    }

    public double getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(double screenWidth) {
        this.screenWidth = screenWidth;
    }
}
