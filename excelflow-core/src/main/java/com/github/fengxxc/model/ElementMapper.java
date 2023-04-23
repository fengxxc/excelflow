package com.github.fengxxc.model;

import com.github.fengxxc.EventType;
import com.github.fengxxc.util.AsFunction;
import com.github.fengxxc.util.ReflectUtils;
import org.apache.commons.math3.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author fengxxc
 * @date 2023-04-22
 */
public abstract class ElementMapper<T, R> {
    private int parentId;
    private Point point;
    private String objectProperty;
    private Class<?> objectPropertyType;
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

    public void as(String objectProperty) {
        this.objectProperty = objectProperty;
    }

    public void as(AsFunction<T, R> func) {
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
    }

    public Function<R, R> val() {
        return valFunc;
    }

    public void val(Function<R, R> valFunc) {
        this.valFunc = valFunc;
    }

    public Class<?> getObjectPropertyType() {
        return objectPropertyType;
    }
}
