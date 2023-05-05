package com.github.fengxxc;

import java.util.function.Function;

/**
 * @author fengxxc
 */
public class Relay<S extends BaseMappers<S, T>, M extends ElementMapper<T, R>, T, R> {
    private S mappers;
    private M mapper;
    private Function<? extends Object, R> valFunc;

    public Relay(S mappers, M mapper) {
        this.mappers = mappers;
        this.mapper = mapper;
    }

    public S cell(String cell) {
        return this.mappers.cell(cell);
    }

    public S val(Function<? extends Object, R> func) {
        this.valFunc = func;
        mapper.val(func);
        return mappers;
    }

    public Function<? extends Object, R> getValFunc() {
        return valFunc;
    }
}
