package com.github.fengxxc.write;

import com.github.fengxxc.Relay;
import com.github.fengxxc.BaseMappers;
import com.github.fengxxc.model.Point;
import com.github.fengxxc.util.AsFunction;

import java.util.TreeSet;

/**
 * @author fengxxc
 * @date 2023-04-22
 */
public class PropMappers<T> implements BaseMappers<PropMappers<T>, T> {
    private TreeSet<PropMapper<T, ?>> mappers = new TreeSet<PropMapper<T, ?>>();
    private PropMapper<T, ? extends Object> current = null;

    public PropMappers() {
    }

    private void start(PropMapper<T, ?> propMapper) {
        if (current != null) {
            mappers.add(current);
        }
        current = propMapper;
    }

    @Override
    public PropMappers<T> cell(String cellRef) {
        final PropMapper<T, Object> mapper = PropMapper.<T, Object>of(cellRef);
        start(mapper);
        return this;
    }

    @Override
    public PropMappers<T> cell(Point point) {
        final PropMapper<T, ?> mapper = PropMapper.<T, Object>of(point);
        start(mapper);
        return this;
    }

    private void assertCurrentNull() {
        if (current == null) {
            throw new NullPointerException("Cannot assemble a null PropMapper, you should call 'cell' method first.");
        }
    }

    @Override
    public <R> Relay<PropMappers<T>, PropMapper<T, R>, T, R> prop(AsFunction<T, R> func) {
        assertCurrentNull();

        // Java泛型擦除可真是操蛋
        // renew
        PropMapper<T, R> reCurrent = PropMapper.<T, R>reOf(current);
        reCurrent.as(func);
        Relay<PropMappers<T>, PropMapper<T, R>, T, R> wrap = new Relay<PropMappers<T>, PropMapper<T, R>, T, R>(this, reCurrent);

        current = reCurrent;
        return wrap;
    }

    @Override
    public PropMappers<T> prop(String property) {
        assertCurrentNull();
        current.as(property);
        return this;
    }

    public PropMappers<T> val(Object value) {
        assertCurrentNull();
        current.setDefVal(value);
        return this;
    }

    @Override
    public TreeSet<PropMapper<T, ?>> getMappers() {
        this.end();
        return mappers;
    }

    public PropMappers<T> setMappers(TreeSet<PropMapper<T, ?>> mappers) {
        this.mappers = mappers;
        return this;
    }

    public PropMappers<T> end() {
        if (current != null) {
            this.mappers.add(current);
        }
        return this;
    }
}
