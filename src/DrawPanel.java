import javax.swing.*;

import Drawers.BresenhamCircleDrawer;
import Drawers.BresenhamLineDrawer;
import Drawers.BufferedImagePixelDrawer;
import Drawers.Interfaces.CircleDrawer;
import Drawers.Interfaces.LineDrawer;
import Drawers.Interfaces.PixelDrawer;
import Math.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import util.*;

public class DrawPanel extends JPanel {
    private ScreenConverter sc;
    private Point lastPoint;
    private Line ox, oy;
    private RealPoint start;
    private List<SegmentOfCircle> segments = new ArrayList<>();
    private SegmentOfCircle curSegment = new SegmentOfCircle();
    private SegmentOfCircle active;
    private boolean scalable;


    public DrawPanel() {
        sc = new ScreenConverter(-50, 50, 100, 100, 800, 800);
        updateAxes();

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (active != null) {
                    ScreenPoint sp = new ScreenPoint(e.getX(), e.getY());
                    RealPoint rp = sc.screenToReal(sp);
                    RealPoint center = active.getCenter();
                    scalable = scalable == false ? findEdgeNear(rp, active) : true;
                    if (scalable) {
                        active.setRadius(distanceBetweenPoints(center.getX(), center.getY(), rp.getX(), rp.getY()));
                    } else {
                        double deltaX = rp.getX() - start.getX();
                        double deltaY = start.getY() - rp.getY();
                        active.setCenter(new RealPoint(center.getX() + deltaX, center.getY() - deltaY));
                        start.setX(start.getX() + deltaX);
                        start.setY(start.getY() - deltaY);
                    }

                } else if (lastPoint != null) {
                    Point currentPoint = e.getPoint();
                    ScreenPoint delta = new ScreenPoint(-currentPoint.x + lastPoint.x,
                            -currentPoint.y + lastPoint.y);

                    RealPoint rDelta = sc.screenToReal(delta);
                    sc.setX(rDelta.getX());
                    sc.setY(rDelta.getY());

                    lastPoint = currentPoint;
                }
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    lastPoint = e.getPoint();
                }

                if (SwingUtilities.isLeftMouseButton(e)) {
                    ScreenPoint sp = new ScreenPoint(e.getX(), e.getY());
                    RealPoint rp = sc.screenToReal(sp);

                    SegmentOfCircle nearSegment = findSegmentNear(rp);
                    if (nearSegment != null) {
                        active = nearSegment;
                        start = rp;
                    } else {
                        if (active == null) {
                            SegmentOfCircle segment = new SegmentOfCircle(curSegment);
                            segment.setCenter(rp);
                            segments.add(segment);
                        }
                        active = null;
                    }

                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    lastPoint = null;
                }
                scalable = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        addMouseWheelListener(e -> {
            int clicks = e.getWheelRotation();
            double step = 0.01;
            double coef = 1 + (clicks > 0 ? step : -step);

            sc.setW(coef * sc.getW());
            sc.setH(coef * sc.getH());
            sc.setX(coef * sc.getX());
            sc.setY(coef * sc.getY());
            repaint();


        });
    }


    @Override
    protected void paintComponent(Graphics g) {
        sc.setScreenWidth(getWidth());
        sc.setScreenHeight(getHeight());
        Graphics2D g2d = (Graphics2D) g;


        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D biG = bi.createGraphics();

        biG.setColor(Color.WHITE);
        biG.fillRect(0, 0, getWidth(), getHeight());

        PixelDrawer pd = new BufferedImagePixelDrawer(bi);
        LineDrawer ld = new BresenhamLineDrawer(pd);
        CircleDrawer cd = new BresenhamCircleDrawer(pd, ld);

        drawAll(ld, cd);
        g2d.drawImage(bi, 0, 0, null);
        biG.dispose();
    }

    private double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
        double c = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        return c;
    }

    private void drawAll(LineDrawer ld, CircleDrawer cd) {
        updateAxes();
        drawOneLine(ld, ox);
        drawOneLine(ld, oy);
        Color color;
        for (SegmentOfCircle segment : segments) {
            color = Color.BLACK;
            if (active != null && active.equals(segment)) {
                color = Color.red;
            }
            ScreenPoint center = sc.realToScreen(segment.getCenter());
            int radius = sc.convertLength(segment.getRadius());
            List<Double> angles = sc.convertAngles(segment.getStartAngle(), segment.getFinishAngle());
            cd.drawCircleSegment(center.getX(), center.getY(), radius, angles.get(0), angles.get(1), color);

        }
    }

    private SegmentOfCircle findSegmentNear(RealPoint rp) {
        for (SegmentOfCircle segment : segments) {
            ScreenPoint sp = sc.realToScreen(rp);
            ScreenPoint center = sc.realToScreen(segment.getCenter());
            int radius = sc.convertLength(segment.getRadius());
            List<Double> angles = sc.convertAngles(segment.getStartAngle(), segment.getFinishAngle());
            if (PositionPointOnSegment.isPointOnSegment(sp.getX(), sp.getY(), center.getX(), center.getY(), radius,
                    angles.get(0), angles.get(1))) {
                return segment;
            }
        }
        return null;
    }

    private boolean findEdgeNear(RealPoint rp, SegmentOfCircle segment) {
        ScreenPoint cur = sc.realToScreen(rp);
        ScreenPoint center = sc.realToScreen(segment.getCenter());
        int radius = sc.convertLength(segment.getRadius());
        List<Double> angles = sc.convertAngles(segment.getStartAngle(), segment.getFinishAngle());
        return PositionPointOnSegment.isPointNearEdge(cur.getX(), cur.getY(), center.getX(),
                center.getY(), radius, angles.get(0), angles.get(1));
    }

    private void updateAxes() {
        ox = new Line(new RealPoint(sc.getW(), 0), new RealPoint(-sc.getW(), 0));
        oy = new Line(new RealPoint(0, sc.getH()), new RealPoint(0, -sc.getH()));
    }



    private void drawOneLine(LineDrawer ld, Line line) {
        ScreenPoint p1 = sc.realToScreen(line.getP1());
        ScreenPoint p2 = sc.realToScreen(line.getP2());
        Color color = Color.BLACK;
        ld.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY(), color);
    }



    public SegmentOfCircle getCurSegment() {
        return curSegment;
    }

    public void setSegments(List<SegmentOfCircle> segments) {
        this.segments = segments;
    }
}
