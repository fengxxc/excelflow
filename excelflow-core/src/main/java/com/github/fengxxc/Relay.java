package com.github.fengxxc;

import com.github.fengxxc.model.ElementMapper;

import java.util.function.Function;

/**
 * @author fengxxc
 * @date 2023-04-22
 */
public class Relay<S extends BaseMappers<S, T>, M extends ElementMapper<T, R>, T, R> {
    private S cellMappers;
    private M mapper;
    private Function<R, R> valFunc;

    public Relay(S cellMappers, M mapper) {
        this.cellMappers = cellMappers;
        this.mapper = mapper;
    }

    public S cell(String cell) {
        return this.cellMappers.cell(cell);
    }

    public S val(Function<R, R> func) {
        this.valFunc = func;
        mapper.val(func);
        return cellMappers;
    }

    public Function<R, R> getValFunc() {
        return valFunc;
    }
}
