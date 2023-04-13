package com.github.fengxxc.util;

import com.github.fengxxc.exception.ExcelFlowReflectionException;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fengxxc
 * @date 2023-04-13
 */
public class ReflectUtils {

    /**
     * lambda method to property name
     * @param func
     * @return
     */
    public static <T> String methodToPropertyName(AsFunction<T, ?> func) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        SerializedLambda lambda = getSerializedLambda(func);
        String methodName = lambda.getImplMethodName();
        String prefix = null;
        if (methodName.startsWith("get")) {
            prefix = "get";
        } else if (methodName.startsWith("is")) {
            prefix = "is";
        } else {
            throw new ExcelFlowReflectionException("Cannot parsing method name '" + methodName + "'.  expected start with 'is' or 'get'.");
        }
        final String property = methodName.substring(prefix.length());
        return Character.isLowerCase(property.charAt(0)) ? property : Character.toLowerCase(property.charAt(0)) + property.substring(1);
    }

    /**
     * Serializable Function reflect to SerializedLambda
     * @param func
     * @return
     */
    static SerializedLambda getSerializedLambda(Serializable func) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = func.getClass().getDeclaredMethod("writeReplace");
        // final boolean accessible = method.isAccessible();
        method.setAccessible(true);
        final SerializedLambda lambda = (SerializedLambda) method.invoke(func);
        // method.setAccessible(accessible);
        return lambda;
    }
}
