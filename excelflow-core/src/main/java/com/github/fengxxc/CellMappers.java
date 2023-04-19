package com.github.fengxxc;

import com.github.fengxxc.model.CellMapper;
import com.github.fengxxc.model.CellType;
import com.github.fengxxc.model.Point;
import com.github.fengxxc.util.AsFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author fengxxc
 * @date 2023-04-12
 */
public class CellMappers<T> {
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

    public CellMappers<T> cell(String cellRef) {
        final CellMapper<T, ?> cellMapper = CellMapper.<T, Object>of(cellRef);
        start((CellMapper<T, ?>) cellMapper);
        return this;
    }

    public CellMappers<T> cell(Point point) {
        final CellMapper<T, ?> cellMapper = CellMapper.<T, Object>of(point);
        start(cellMapper);
        return this;
    }

    private void assertCurrentNull() {
        if (current == null) {
            throw new NullPointerException("Cannot assemble a null CellMapper, you should call 'cell' method first.");
        }
    }

    public <R> Relay<T, R> as(AsFunction<T, R> func) {
        assertCurrentNull();

        // Java泛型擦除可真是操蛋
        // renew
        CellMapper<T, R> reCurrent = CellMapper.<T, R>reOf(current);
        reCurrent.as(func);
        Relay<T, R> wrap = new Relay<T, R>(this, reCurrent);

        current = reCurrent;
        return wrap;
    }

    public CellMappers<T> as(String property) {
        assertCurrentNull();
        current.as(property);
        return this;
    }

    public List<CellMapper<T, ?>> getMappers() {
        this.end();
        return mappers;
    }

    public CellMappers<T> setMappers(List<CellMapper<T, ?>> mappers) {
        this.mappers = mappers;
        return this;
    }

    public CellMappers<T> end() {
        if (current != null) {
            this.mappers.add(current);
        }
        return this;
    }

    public class Relay<T, R> {
        private CellMappers<T> cellMappers;
        private CellMapper<T, R> mapper;
        private Function<R, R> valFunc;

        public Relay(CellMappers<T> cellMappers, CellMapper<T, R> mapper) {
            this.cellMappers = cellMappers;
            this.mapper = mapper;
        }

        public CellMappers<T> cell(String cell) {
            return this.cellMappers.cell(cell);
        }

        public CellMappers<T> val(Function<R, R> func) {
            this.valFunc = func;
            mapper.val(func);
            return cellMappers;
        }

        public Function<R, R> getValFunc() {
            return valFunc;
        }
    }
}
