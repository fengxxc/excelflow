package com.github.fengxxc.util;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author fengxxc
 * @date 2023-04-12
 */
@FunctionalInterface
public interface AsFunction<T, R> extends Function<T, R>, Serializable {
}
