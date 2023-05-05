package com.github.fengxxc.util;

import com.github.fengxxc.model.Foward;
import com.github.fengxxc.model.Offset;
import com.github.fengxxc.model.Point;
import com.github.fengxxc.model.Rect;

/**
 * @author fengxxc
 */
public class ExcelFlowUtils {

    public static String repeatString(String string, int repeat) {
        StringBuffer sb = new StringBuffer(string);
        for (int i = 0; i < repeat; i++) {
            sb.append(string);
        }
        return sb.toString();
    }

    public static String computNextCellRef(String cellRef, Offset offset) {
        Point point = Point.of(cellRef);
        point.X += offset.getX();
        point.Y += offset.getY();
        return point.toCellReferenceString();
    }
}
