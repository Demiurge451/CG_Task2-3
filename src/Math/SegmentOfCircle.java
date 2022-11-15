package Math;

import java.util.ArrayList;
import java.util.List;

public class SegmentOfCircle {
    private RealPoint center;
    private double radius;
    private double startAngle, finishAngle;

    public SegmentOfCircle(RealPoint center, double radius, double startAngle, double finishAngle) {
        this.center = center;
        this.radius = radius;
        this.startAngle = startAngle;
        this.finishAngle = finishAngle;
    }

    public SegmentOfCircle() {
        this(new RealPoint(0, 0), 0, 0, 0);
    }

    public SegmentOfCircle(SegmentOfCircle segment) {
        this(segment.getCenter(), segment.getRadius(), segment.getStartAngle(), segment.getFinishAngle());
    }


    public RealPoint getCenter() {
        return center;
    }

    public void setCenter(RealPoint center) {
        this.center = center;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(double startAngle) {
        this.startAngle = startAngle;
    }

    public double getFinishAngle() {
        return finishAngle;
    }

    public void setFinishAngle(double finishAngle) {
        this.finishAngle = finishAngle;
    }
}
