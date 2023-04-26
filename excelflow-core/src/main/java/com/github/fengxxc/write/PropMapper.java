package com.github.fengxxc.write;

import com.github.fengxxc.ElementMapper;
import com.github.fengxxc.model.Point;
import org.apache.poi.ss.util.CellReference;

/**
 * @author fengxxc
 */
public class PropMapper<T, R> extends ElementMapper<T, R> {
    private Object defVal;

    public PropMapper() {
    }

    public static <T, R> PropMapper<T, R> of(Point point) {
        PropMapper<T, R> propMapper = new PropMapper<>();
        propMapper.setPoint(point);
        return propMapper;
    }

    public static <T, R> PropMapper<T, R> of(int y, int x) {
        return of(Point.of(y, x));
    }

    public static <T, R> PropMapper<T, R> of(String colRef, int rowRef) {
        final int x = CellReference.convertColStringToIndex(colRef);
        return of(rowRef - 1, x);
    }

    public static <T, R> PropMapper<T, R> of(String cellRef) {
        final Point point = Point.of(cellRef);
        return of(point);
    }

    public static <T, R> PropMapper<T, R> reOf(PropMapper<T, ?> propMapper) {
        PropMapper<T, R> reMapper = new PropMapper<>();
        reMapper.setParentId(propMapper.getParentId());
        reMapper.setPoint(propMapper.getPoint());
        return reMapper;
    }

    public Object getDefVal() {
        return defVal;
    }

    public PropMapper<T, R> setDefVal(Object defVal) {
        this.defVal = defVal;
        return this;
    }
}
