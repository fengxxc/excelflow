package com.github.fengxxc.util;

import com.github.fengxxc.exception.ExcelFlowReflectionException;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author fengxxc
 */
public class ReflectUtils {

    /**
     * parse lambda method to property name and return type
     * @param func
     * @return first: property name, second: return type
     */
    public static <T> Pair<String, Class> parseLambdaMethod(AsFunction<T, ?> func) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        SerializedLambda lambda = getSerializedLambda(func);

        // get prop name
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
        String propName = Character.isLowerCase(property.charAt(0)) ? property : Character.toLowerCase(property.charAt(0)) + property.substring(1);

        // get return type
        String desc = lambda.getInstantiatedMethodType();
        String[] typeDescs = desc.substring(1, desc.length() - 1).split(";\\)");
        /*Class<?>[] paramTypes = new Class<?>[typeDescs.length - 1];
        for (int i = 0; i < paramTypes.length; i++) {
            paramTypes[i] = getClassFromTypeDesc(typeDescs[i]);
        }*/
        Class<?> returnType = getClassFromTypeDesc(typeDescs[typeDescs.length - 1]);
        return new Pair<>(propName, returnType);
    }

    /**
     * Serializable Function reflect to SerializedLambda
     * @param func
     * @return
     */
    public static SerializedLambda getSerializedLambda(Serializable func) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = func.getClass().getDeclaredMethod("writeReplace");
        // final boolean accessible = method.isAccessible();
        method.setAccessible(true);
        final SerializedLambda lambda = (SerializedLambda) method.invoke(func);
        // method.setAccessible(accessible);
        return lambda;
    }

    public static Class<?> getClassFromTypeDesc(String typeDesc) throws ClassNotFoundException {
        if (typeDesc.startsWith("L")) {
            return getObjectClass(typeDesc);
        }
        switch (typeDesc) {
            case "V":
                return void.class;
            case "Z":
                return boolean.class;
            case "B":
                return byte.class;
            case "C":
                return char.class;
            case "D":
                return double.class;
            case "F":
                return float.class;
            case "I":
                return int.class;
            case "J":
                return long.class;
            case "S":
                return short.class;
        }

        return null;
    }

    public static Class<?> getObjectClass(String typeDesc) throws ClassNotFoundException {
        String className = typeDesc.substring(1, typeDesc.length());
        return Class.forName(className.replace('/', '.'));
    }

    public static Object convertValueByClassType(String originValue, Object val, Class propertyType) {
        switch (propertyType.getSimpleName()) {
            case "String":
                val = String.valueOf(originValue);
                break;
            case "Integer":
                val = Integer.valueOf(originValue);
                break;
            case "Boolean":
                val = Boolean.valueOf(originValue);
                break;
            case "Character":
                val = originValue.charAt(0);
                break;
            case "Byte":
                val = Byte.valueOf(originValue);
                break;
            case "Short":
                val = Short.valueOf(originValue);
                break;
            case "Long":
                val = Long.valueOf(originValue);
                break;
            case "Float":
                val = Float.valueOf(originValue);
                break;
            case "Double":
                val = Double.valueOf(originValue);
                break;
            default:
                val = originValue;
        }

        return val;
    }

    public static void setPropertyValue(Object obj, String propertyName, Object propertyValue) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        if (obj == null || propertyName == null || "".equals(propertyName)) {
            return;
        }
        Class<?> clazz = obj.getClass();
        String UpperCamelCasePropName = propertyName.length() > 1 ? propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1) : propertyName.toUpperCase();
        Method method = null;
        try {
            method = clazz.getMethod("set" + UpperCamelCasePropName, propertyValue.getClass());
        } catch (NoSuchMethodException e) {
            Field field = clazz.getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(obj, propertyValue);
            return;
        }
        method.invoke(obj, propertyValue);
    }

    public static Object getPropertyValue(Object object, String propertyName) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        if (object == null || propertyName == null || "".equals(propertyName)) {
            return null;
        }
        Class<?> clazz = object.getClass();
        String UpperCamelCasePropName = propertyName.length() > 1 ? propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1) : propertyName.toUpperCase();
        Method method = null;
        try {
            method = clazz.getMethod("get" + UpperCamelCasePropName);
        } catch (NoSuchMethodException e) {
            Field field = clazz.getDeclaredField(propertyName);
            field.setAccessible(true);
            return field.get(object);
        }
        return method.invoke(object);
    }

    public static <T> T createInstance(Class<T> object, Object... args) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class<?>[] parameterTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        T newObj = null;
        newObj = object.getDeclaredConstructor(parameterTypes).newInstance();
        return newObj;
    }

    public static Object getFieldValueByCglib(Object obj, String fieldName) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(obj.getClass());
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                if (method.getName().startsWith("get") && method.getName().toLowerCase().contains(fieldName.toLowerCase())) {
                    return methodProxy.invokeSuper(o, objects);
                } else {
                    return null;
                }
            }
        });
        Object result = enhancer.create();
        return result;
    }
}
