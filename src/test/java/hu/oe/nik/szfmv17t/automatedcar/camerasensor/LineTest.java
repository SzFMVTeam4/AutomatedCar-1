package hu.oe.nik.szfmv17t.automatedcar.camerasensor;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class LineTest {
    @Test
    public void slopeTest () {
        Line l;
        l = new Line(0, 0, 10, 10);
        assertEquals(1, l.slope, 0.01);

        l = new Line(8, 8, 10, 10);
        assertEquals(1, l.slope, 0.01);

        l = new Line(5, 5, 10, 20);
        assertEquals(3, l.slope, 0.01);
    }

    @Test
    public void interseciontAssociativityTest () {
        Line l1 = new Line(0, 0, 10, 10);
        Line l2 = new Line(0, 10, 10, 0);

        Point.Double i1 = l1.getIntersectionPoint(l2);
        Point.Double i2 = l2.getIntersectionPoint(l1);

        assertEquals(i1.x, i2.x, 0.01);
        assertEquals(i1.y, i2.y, 0.01);
    }

    @Test
    public void intersectionTest () {
        Line l1 = new Line (0, 0, 10, 10);
        Line l2 = new Line (0, 10, 10, 0);

        Point.Double i = l1.getIntersectionPoint(l2);

        assertEquals(5, i.x, 0.01);
        assertEquals(5, i.y, 0.01);
    }

    @Test
    public void intersectionTestHorizontal () {
        Line l = new Line (0, 0, 10, 10);
        Line horizontal = new Line (0, 5, 10, 5);

        Point.Double i = horizontal.getIntersectionPoint(l);

        assertEquals(5, i.x, 0.01);
        assertEquals(5, i.y, 0.01);
    }

    @Test
    public void intersectionTestVertical1 () {
        Line l = new Line (0, 0, 10, 10);
        Line vertical = new Line (5, 0, 5, 10);

        Point.Double i = vertical.getIntersectionPoint(l);

        assertEquals(5, i.x, 0.01);
        assertEquals(5, i.y, 0.01);
    }

    @Test
    public void intersectionTestVertical2 () {
        Line l = new Line (0, 0, 10, 10);
        Line vertical = new Line (5, 0, 5, 10);

        Point.Double i = l.getIntersectionPoint(vertical);

        assertEquals(5, i.x, 0.01);
        assertEquals(5, i.y, 0.01);
    }
}
