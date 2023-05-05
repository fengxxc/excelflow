package com.github.fengxxc;

import com.github.fengxxc.model.Point;
import com.github.fengxxc.util.PropFunction;
import com.github.fengxxc.util.ReflectUtils;
import org.apache.commons.math3.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

/**
 * @author fengxxc
 */
public abstract class ElementMapper<T, R> implements Comparable<ElementMapper<T, R>> {
    private int parentId;
    private int objectCacheId;
    private Point point;
    private String objectProperty;
    private Class<?> objectPropertyReturnType;
    private Function<? extends Object, R> valFunc;
    private boolean isEndOfParent = false;

    public int getParentId() {
        return this.parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getObjectCacheId() {
        return objectCacheId;
    }

    public ElementMapper<T, R> setObjectCacheId(int objectCacheId) {
        this.objectCacheId = objectCacheId;
        return this;
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

    public void prop(PropFunction<T, R> func) {
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

    public Function<? extends Object, R> val() {
        return valFunc;
    }

    public void val(Function<? extends Object, R> valFunc) {
        this.valFunc = valFunc;
    }

    public Class<?> getObjectPropertyReturnType() {
        return objectPropertyReturnType;
    }

    public boolean isEndOfParent() {
        return isEndOfParent;
    }

    public ElementMapper<T, R> setEndOfParent(boolean endOfParent) {
        isEndOfParent = endOfParent;
        return this;
    }

    @Override
    public int compareTo(ElementMapper o) {
        /*final int sheetDiff = this.getParent().getSheetAt() - o.getParentBunch().getSheetAt();
        if (sheetDiff != 0) {
            return sheetDiff;
        }*/
        return this.getPoint().compareTo(o.getPoint());
    }
}
