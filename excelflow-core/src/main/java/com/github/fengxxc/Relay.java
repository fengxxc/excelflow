package com.github.fengxxc;

import java.util.function.Function;

/**
 * @author fengxxc
 */
public class Relay<S extends BaseMappers<S, T>, M extends ElementMapper<T, R>, T, R> {
    private S mappers;
    private M mapper;
    private Function<R, R> valFunc;

    public Relay(S mappers, M mapper) {
        this.mappers = mappers;
        this.mapper = mapper;
    }

    public S cell(String cell) {
        return this.mappers.cell(cell);
    }

    public S val(Function<R, R> func) {
        this.valFunc = func;
        mapper.val(func);
        return mappers;
    }

    public Function<R, R> getValFunc() {
        return valFunc;
    }
}
