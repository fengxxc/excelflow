package com.github.fengxxc;

import com.github.fengxxc.model.Point;
import com.github.fengxxc.util.AsFunction;
import com.github.fengxxc.util.ReflectUtils;
import org.apache.commons.math3.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

/**
 * @author fengxxc
 */
public abstract class ElementMapper<T, R> implements Comparable<ElementMapper<T, R>> {
    private int parentId;
    private Point point;
    private String objectProperty;
    private Class<?> objectPropertyReturnType;
    private Function<R, R> valFunc;


    public int getParentId() {
        return this.parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getObjectProperty() {
        return objectProperty;
    }

    public void prop(String objectProperty) {
        this.objectProperty = objectProperty;
        this.objectPropertyReturnType = Object.class;
    }

    public void prop(AsFunction<T, R> func) {
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
        this.objectPropertyReturnType = pair.getSecond();
    }

    public Function<R, R> val() {
        return valFunc;
    }

    public void val(Function<R, R> valFunc) {
        this.valFunc = valFunc;
    }

    public Class<?> getObjectPropertyReturnType() {
        return objectPropertyReturnType;
    }

    @Override
    public int compareTo(ElementMapper o) {
        /*final int sheetDiff = this.getParent().getSheetAt() - o.getParentBunch().getSheetAt();
        if (sheetDiff != 0) {
            return sheetDiff;
        }*/
        final int yDiff = this.getPoint().Y - o.getPoint().Y;
        if (yDiff != 0) {
            return ((int) yDiff);
        }
        return ((int) (this.getPoint().X - o.getPoint().X));
    }
}
