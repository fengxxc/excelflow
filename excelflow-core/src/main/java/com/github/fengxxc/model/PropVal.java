package com.github.fengxxc.model;

import com.github.fengxxc.util.PropFunction;
import com.github.fengxxc.util.ReflectUtils;
import org.apache.commons.math3.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * T: Object type
 * V: Object.value type
 * @author fengxxc
 */
public class PropVal<T, V> {
    private String property;
    private V value;

    public PropVal() {
    }

    public PropVal(String property, V value) {
        this.property = property;
        this.value = value;
    }

    public PropVal(String property, Object value, Class<V> valueClass) {
        this.property = property;
        this.value = (V) value;
    }

    public String getProperty() {
        return property;
    }

    public PropVal<T, V> setProperty(String property) {
        this.property = property;
        return this;
    }

    public V getValue() {
        return value;
    }

    public PropVal<T, V> setValue(V value) {
        this.value = value;
        return this;
    }

    public static <T, V> PropVal<T, V> of(String property, V value) {
        return new PropVal<T, V>(property, value);
    }

    public static <T, V> PropVal<T, V> of(String property, Object value, Class<V> valueClass) {
        return new PropVal<T, V>(property, value, valueClass);
    }

    public boolean propEquals(PropFunction<T, ? extends Object> func) {
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
        return this.getProperty().equals(pair.getFirst());
    }

    @Override
    public String toString() {
        return "PropVal{" +
                "property='" + property + '\'' +
                ", value=" + value +
                '}';
    }
}
