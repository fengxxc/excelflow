package com.github.fengxxc.read;

import com.github.fengxxc.Relay;
import com.github.fengxxc.BaseMappers;
import com.github.fengxxc.model.Point;
import com.github.fengxxc.util.PropFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author fengxxc
 */
public class CellMappers<T> implements BaseMappers<CellMappers<T>, T> {
    private List<CellMapper<T, ?>> mappers = new ArrayList<>();
    private CellMapper<T, ?> current = null;

    public CellMappers() {
    }

    private void start(CellMapper<T, ?> cellMapper) {
        if (current != null) {
            mappers.add(current);
        }
        current = cellMapper;
    }

    @Override
    public CellMappers<T> cell(String cellRef) {
        final CellMapper<T, ?> mapper = CellMapper.<T, Object>of(cellRef);
        start(mapper);
        return this;
    }

    @Override
    public CellMappers<T> cell(Point point) {
        final CellMapper<T, ?> mapper = CellMapper.<T, Object>of(point);
        start(mapper);
        return this;
    }

    private void assertCurrentNull() {
        if (current == null) {
            throw new NullPointerException("Cannot assemble a null CellMapper, you should call 'cell' method first.");
        }
    }

    @Override
    public <R> Relay<CellMappers<T>, CellMapper<T, R>, T, R> prop(PropFunction<T, R> func) {
        assertCurrentNull();

        // Java泛型擦除可真是操蛋
        // renew
        CellMapper<T, R> reCurrent = CellMapper.<T, R>reOf(current);
        reCurrent.prop(func);
        Relay<CellMappers<T>, CellMapper<T, R>, T, R> wrap = new Relay<CellMappers<T>, CellMapper<T, R>, T, R>(this, reCurrent);

        current = reCurrent;
        return wrap;
    }

    @Override
    public CellMappers<T> prop(String property) {
        assertCurrentNull();
        current.prop(property);
        return this;
    }

    @Override
    public CellMappers<T> val(Function func) {
        assertCurrentNull();
        current.val(func);
        return this;
    }

    @Override
    public List<CellMapper<T, ?>> getMappers() {
        this.end();
        return mappers;
    }

    public CellMappers<T> end() {
        if (current != null) {
            this.mappers.add(current);
        }
        return this;
    }

}
