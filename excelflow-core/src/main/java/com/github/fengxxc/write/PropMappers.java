package com.github.fengxxc.write;

import com.github.fengxxc.Relay;
import com.github.fengxxc.BaseMappers;
import com.github.fengxxc.model.Point;
import com.github.fengxxc.util.PropFunction;

import java.util.TreeSet;
import java.util.function.Function;

/**
 * @author fengxxc
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
    public <R> Relay<PropMappers<T>, PropMapper<T, R>, T, R> prop(PropFunction<T, R> func) {
        assertCurrentNull();

        // Java泛型擦除可真是操蛋
        // renew
        PropMapper<T, R> reCurrent = PropMapper.<T, R>reOf(current);
        reCurrent.prop(func);
        Relay<PropMappers<T>, PropMapper<T, R>, T, R> wrap = new Relay<PropMappers<T>, PropMapper<T, R>, T, R>(this, reCurrent);

        current = reCurrent;
        return wrap;
    }

    @Override
    public PropMappers<T> prop(String property) {
        assertCurrentNull();
        current.prop(property);
        return this;
    }

    @Override
    public PropMappers<T> val(Function func) {
        assertCurrentNull();
        current.val(func);
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
