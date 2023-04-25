package com.github.fengxxc.util;

import com.github.fengxxc.model.Foward;
import com.github.fengxxc.model.Point;
import com.github.fengxxc.model.Rect;

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

    public static Rect getMaxRect(Foward foward, Point point) {
        int top = foward == Foward.Up ? 0 : point.Y;
        int right = foward == Foward.Right ? Integer.MAX_VALUE : point.X;
        int bottom = foward == Foward.Down ? Integer.MAX_VALUE : point.Y;
        int left = foward == Foward.Left ? 0 : point.X;
        final Rect rect = Rect.of(Point.of(top, right), Point.of(bottom, left));
        return rect;
    }

    public static String repeatString(String string, int repeat) {
        StringBuffer sb = new StringBuffer(string);
        for (int i = 0; i < repeat; i++) {
            sb.append(string);
        }
        return sb.toString();
    }
}
