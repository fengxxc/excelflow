package com.github.fengxxc.model;

import com.github.fengxxc.util.AsFunction;
import com.github.fengxxc.util.ReflectUtils;
import org.apache.poi.ss.util.CellReference;

import java.lang.reflect.InvocationTargetException;

/**
 * @author fengxxc
 * @date 2023-04-01
 */
public class CellMapper<T> implements Comparable<CellMapper<T>> {
    private Part<T> part;

    private Point point;
    private String objectProperty;
    private CellType type;
    private String format;

    private CellMapper() {
    }

    public static <T> CellMapper<T> of(Point point) {
        return new CellMapper().setPoint(point);
    }

    public static <T> CellMapper<T> of(int y, int x) {
        return of(Point.of(y, x));
    }

    public static <T> CellMapper<T> of(String colRef, int rowRef) {
        final int x = CellReference.convertColStringToIndex(colRef);
        return of(rowRef - 1, x);
    }

    public static <T> CellMapper<T> of(String cellRef) {
        final Point point = Point.of(cellRef);
        return of(point);
    }

    public Part getPart() {
        return part;
    }

    public CellMapper setPart(Part parentBunchs) {
        this.part = parentBunchs;
        return this;
    }

    public Point getPoint() {
        return point;
    }

    public CellMapper setPoint(Point point) {
        this.point = point;
        return this;
    }

    public String getObjectProperty() {
        return objectProperty;
    }

    public CellMapper as(String objectProperty) {
        this.objectProperty = objectProperty;
        return this;
    }

    public CellMapper<T> as(AsFunction<T, ?> func) {
        String propName = null;
        try {
            propName = ReflectUtils.methodToPropertyName(func);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        this.objectProperty = propName;
        return this;
    }

    public CellType getType() {
        return type;
    }

    public CellMapper setType(CellType type) {
        this.type = type;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public CellMapper setFormat(String format) {
        this.format = format;
        return this;
    }

    @Override
    public int compareTo(CellMapper o) {
        /*final int sheetDiff = this.getParentBunch().getSheetAt() - o.getParentBunch().getSheetAt();
        if (sheetDiff != 0) {
            return sheetDiff;
        }*/
        final int yDiff = this.getPoint().Y - o.getPoint().Y;
        if (yDiff != 0) {
            return ((int) yDiff);
        }
        return ((int) (this.getPoint().X - o.getPoint().X));
    }

    @Override
    public String toString() {
        return "CellMapper{" +
                // "parentBunch=" + parentBunch +
                ", point=" + point +
                ", objectProperty='" + objectProperty + '\'' +
                ", type=" + type +
                ", format='" + format + '\'' +
                '}';
    }
}
