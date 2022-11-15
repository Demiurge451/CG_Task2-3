package util;

import Math.*;

public final class PositionPointOnLine {
    private static final int eps = 10;

    private PositionPointOnLine() {
        throw new UnsupportedOperationException();
    }

    public static boolean isPointOnLine(ScreenPoint p1, ScreenPoint p2, ScreenPoint cur) {
        int left = Math.min(p1.getX(), p2.getX());
        int right = Math.max(p1.getX(), p2.getX());
        int top = Math.max(p1.getY(), p2.getY());
        int bottom = Math.min(p1.getY(), p2.getY());
        if (!(cur.getX() >= left && cur.getX() <= right && cur.getY() <= top && cur.getY() >= bottom)) {
            return false;
        }

        double a = p2.getY() - p1.getY();
        double b = p1.getX() - p2.getX();
        double c = p1.getY() * (p2.getX() - p1.getX()) - p1.getX() * (p2.getY() - p1.getY());
        double distance = Math.abs(a * cur.getX() + b * cur.getY() + c);
        distance /= Math.sqrt(a * a + b * b);
        return (int)distance < eps;
    }
}
