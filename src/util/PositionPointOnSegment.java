package util;

import java.util.ArrayList;
import java.util.List;

import Math.*;

public final class PositionPointOnSegment {
    private static final int eps = 10;
    private static List<Restriction> restrictions = new ArrayList<>();

    public PositionPointOnSegment() {
        throw new UnsupportedOperationException();
    }

    private static class Restriction {
        private final int leftX;
        private final int rightX;
        private final int bottomY;
        private final int topY;

        public Restriction(int leftX, int rightX, int bottomY, int topY) {
            this.leftX = leftX;
            this.rightX = rightX;
            this.bottomY = bottomY;
            this.topY = topY;
        }

        private boolean checkRestrictions(int x, int y) {
            return leftX <= x && rightX >= x && bottomY <= y && topY >= y;
        }
    }



    public static boolean isPointOnSegment(int x, int y, int x0, int y0, int radius, double startAngle, double finishAngle) {
        return isPointOnCircle(x, y, x0, y0, radius)
                && isPointOnArc(x, y, x0, y0, radius, startAngle, finishAngle)
                || isPointOnChord(x, y, x0, y0, radius, startAngle, finishAngle);
    }

    public static boolean isPointOnArc(int x, int y, int x0, int y0, int radius, double startAngle, double finishAngle) {
        createRestrictions(x0, y0, radius, startAngle, finishAngle);
        return checkAllRestrictions(x, y);
    }

    public static boolean isPointNearEdge(int x, int y, int x0, int y0, int radius, double startAngle, double finishAngle) {
        ScreenPoint p1 = createEdges(x0, y0, radius, startAngle);
        ScreenPoint p2 = createEdges(x0, y0, radius, finishAngle);
        ScreenPoint cur = new ScreenPoint(x, y);
        int distance1 = distance(p1.getX(), p1.getY(), cur.getX(), cur.getY());
        int distance2 = distance(p2.getX(), p2.getY(), cur.getX(), cur.getY());
        return distance1 < eps || distance2 < eps;
    }

    private static ScreenPoint createEdges(int x0, int y0, int radius, double angle) {
        int x = (int) (Math.cos(angle) * radius) + x0;
        int y = (int) (Math.sin(angle) * radius) + y0;
        return new ScreenPoint(x, y);
    }

    private static int distance(int x1, int y1, int x2, int y2) {
        return (int)Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static boolean isPointOnChord(int x, int y, int x0, int y0, int radius, double startAngle, double finishAngle) {
        ScreenPoint p1 = createEdges(x0, y0, radius, startAngle);
        ScreenPoint p2 = createEdges(x0, y0, radius, finishAngle);
        ScreenPoint cur = new ScreenPoint(x, y);
        return PositionPointOnLine.isPointOnLine(p1, p2, cur);
    }

    public static boolean isPointOnCircle(int x, int y, int x0, int y0, int radius) {
        int distance = distance(x0, y0, x, y);
        return Math.abs(distance - radius) <= eps;
    }
    
    private static void createRestrictions(int x0, int y0, int radius, double startAngle, double finishAngle) {
        double minAngle = Math.min(startAngle, finishAngle);
        double maxAngle = Math.max(startAngle, finishAngle);
        List<Double> extremePoints = new ArrayList<>();
        extremePoints.add(minAngle);
        double turn = 2 * Math.PI;
        for (double i = -turn; i - turn <= 1e-6; i += turn / 4) {
            if (i > minAngle && i < maxAngle) {
                extremePoints.add(i);
            }
        }
        extremePoints.add(maxAngle);

        for (int i = 0; i < extremePoints.size() - 1; i++) {
            double angle2 = extremePoints.get(i + 1);
            double angle1 = extremePoints.get(i);
            int x1 = (int) (Math.cos(angle1) * radius) + x0;
            int x2 = (int) (Math.cos(angle2) * radius) + x0;
            int y1 = (int) (Math.sin(angle1) * radius) + y0;
            int y2 = (int) (Math.sin(angle2) * radius) + y0;
            restrictions.add(new Restriction(Math.min(x1, x2), Math.max(x1, x2), Math.min(y1, y2), Math.max(y1, y2)));
        }
    }

    private static void clear() {
        restrictions = new ArrayList<>();
    }
    private static boolean checkAllRestrictions(int x, int y) {
        for (Restriction restriction : restrictions) {
            if (restriction.checkRestrictions(x, y)) {
                clear();
                return true;
            }
        }
        clear();
        return false;
    }
}
