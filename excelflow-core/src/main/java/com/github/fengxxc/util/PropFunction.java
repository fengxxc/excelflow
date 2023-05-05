package com.github.fengxxc.util;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author fengxxc
 */
@FunctionalInterface
public interface PropFunction<T, R> extends Function<T, R>, Serializable {
}
