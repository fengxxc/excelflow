package com.github.fengxxc;

import com.github.fengxxc.model.Point;
import com.github.fengxxc.util.PropFunction;

import java.util.Collection;
import java.util.function.Function;

/**
 * @author fengxxc
 */
public interface BaseMappers<S extends BaseMappers<S, T>, T> {
    S cell(String cellRef);

    S cell(Point point);

    <R> Relay<S, ? extends ElementMapper<T, R>, T, R> prop(PropFunction<T, R> func);

    S prop(String property);

    S val(Function func);

    Collection<? extends ElementMapper<T, ?>> getMappers();

    // S setMappers(List<? extends ElementMapper<?, T, ?>> mappers);
}
