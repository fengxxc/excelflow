package com.github.fengxxc.model;

import com.github.fengxxc.util.AsFunction;
import com.github.fengxxc.util.ReflectUtils;
import org.apache.commons.math3.util.Pair;
import org.apache.poi.ss.util.CellReference;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

/**
 * @author fengxxc
 * @date 2023-04-01
 */
public class CellMapper<T, R> implements Comparable<CellMapper<T, R>> {
    private Picker<T> picker;

    private Point point;
    private String objectProperty;
    private Class<?> objectPropertyType;
    private Function<R, R> valFunc;

    private CellMapper() {
    }

    public static <T, R> CellMapper<T, R> of(Point point) {
        return new CellMapper().setPoint(point);
    }

    public static <T, R> CellMapper<T, R> of(int y, int x) {
        return of(Point.of(y, x));
    }

    public static <T, R> CellMapper<T, R> of(String colRef, int rowRef) {
        final int x = CellReference.convertColStringToIndex(colRef);
        return of(rowRef - 1, x);
    }

    public static <T, R> CellMapper<T, R> of(String cellRef) {
        final Point point = Point.of(cellRef);
        return of(point);
    }

    public static <T, R> CellMapper<T, R> reOf(CellMapper<T, ?> cellMapper) {
        return new CellMapper<T, R>()
                .setPoint(cellMapper.getPoint())
                .setPicker(cellMapper.getPicker());
    }

    public Picker getPicker() {
        return picker;
    }

    public CellMapper setPicker(Picker picker) {
        this.picker = picker;
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

    public CellMapper<T, R> as(AsFunction<T, R> func) {
        String propName = null;
        Pair<String, Class> pair = null;
        try {
            pair = ReflectUtils.parseLambdaMethod(func);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.objectProperty = pair.getFirst();
        this.objectPropertyType = pair.getSecond();
        return this;
    }

    public Function<R, R> val() {
        return valFunc;
    }

    public CellMapper<T, R> val(Function<R, R> valFunc) {
        this.valFunc = valFunc;
        return this;
    }

    public Class<?> getObjectPropertyType() {
        return objectPropertyType;
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
                ", point=" + point +
                ", objectProperty='" + objectProperty + '\'' +
                '}';
    }
}
