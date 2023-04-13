package com.github.fengxxc;

import com.github.fengxxc.model.CellMapper;
import com.github.fengxxc.model.CellType;
import com.github.fengxxc.model.Point;
import com.github.fengxxc.util.AsFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengxxc
 * @date 2023-04-12
 */
public class CellMappers<T> {
    private List<CellMapper<T>> mappers = new ArrayList<>();
    private CellMapper<T> current = null;

    public CellMappers() {
    }

    private void start(CellMapper<T> cellMapper) {
        if (current != null) {
            mappers.add(current);
        }
        current = cellMapper;
    }

    public CellMappers<T> cell(String cellRef) {
        final CellMapper<T> cellMapper = CellMapper.<T>of(cellRef);
        start((CellMapper<T>) cellMapper);
        return this;
    }

    public CellMappers<T> cell(Point point) {
        final CellMapper<T> cellMapper = CellMapper.<T>of(point);
        start(cellMapper);
        return this;
    }

    private void assertCurrentNull() {
        if (current == null) {
            throw new NullPointerException("Cannot assemble a null CellMapper, you should call 'cell' method first.");
        }
    }

    public CellMappers<T> as(AsFunction<T, ?> func) {
        assertCurrentNull();
        current.as(func);
        return this;
    }

    public CellMappers<T> as(String property) {
        assertCurrentNull();
        current.as(property);
        return this;
    }

    public CellMappers<T> type(CellType cellType) {
        assertCurrentNull();
        if (cellType == null) {
            cellType = CellType.Text;
        }
        current.setType(cellType);
        return this;
    }

    public List<CellMapper<T>> getMappers() {
        this.end();
        return mappers;
    }

    public CellMappers<T> setMappers(List<CellMapper<T>> mappers) {
        this.mappers = mappers;
        return this;
    }

    public CellMappers<T> end() {
        if (current != null) {
            this.mappers.add(current);
        }
        return this;
    }
}
