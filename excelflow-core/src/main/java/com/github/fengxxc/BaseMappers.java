package com.github.fengxxc;

import com.github.fengxxc.model.ElementMapper;
import com.github.fengxxc.model.Point;
import com.github.fengxxc.util.AsFunction;

import java.util.Collection;
import java.util.List;

/**
 * @author fengxxc
 * @date 2023-04-22
 */
public interface BaseMappers<S extends BaseMappers<S, T>, T> {
    S cell(String cellRef);

    S cell(Point point);

    <R> Relay<S, ? extends ElementMapper<T, R>, T, R> prop(AsFunction<T, R> func);

    S prop(String property);

    Collection<? extends ElementMapper<T, ?>> getMappers();

    // S setMappers(List<? extends ElementMapper<?, T, ?>> mappers);
}
