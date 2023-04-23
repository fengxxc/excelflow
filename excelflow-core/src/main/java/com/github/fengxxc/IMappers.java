package com.github.fengxxc;

import com.github.fengxxc.model.ElementMapper;
import com.github.fengxxc.model.Point;
import com.github.fengxxc.util.AsFunction;

import java.util.List;

/**
 * @author fengxxc
 * @date 2023-04-22
 */
public interface IMappers<S extends IMappers<S, T>, T> {
    S cell(String cellRef);

    S cell(Point point);

    <R> Relay<S, ? extends ElementMapper<T, R>, T, R> as(AsFunction<T, R> func);

    S as(String property);

    List<? extends ElementMapper<T, ?>> getMappers();

    // S setMappers(List<? extends ElementMapper<?, T, ?>> mappers);
}
