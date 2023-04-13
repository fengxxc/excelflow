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
}
