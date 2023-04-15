package com.github.fengxxc.util;

import com.github.fengxxc.model.Point;

/**
 * @author fengxxc
 * @date 2023-04-02
 */
public class ExcelFlowUtils {

    public static boolean gridPosEquals(Point point, String cellReference) {
        final Point point1 = Point.of(cellReference);
        return point.equals(point1);
    }

    public static Point maxIn2D(Point point1, Point point2) {
        if (point1 == null && point2 == null) {
            return Point.of(0, 0);
        }
        if (point1 == null) {
            return point2;
        }
        if (point2 == null) {
            return point1;
        }
        if (point1.Y > point2.Y) {
            return point1;
        }
        if (point1.Y < point2.Y) {
            return point2;
        }
        if (point1.X >= point2.X) {
            return point1;
        }
        return point2;
    }
}
