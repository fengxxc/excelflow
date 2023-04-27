package com.github.fengxxc;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

import java.util.HashMap;
import java.util.Map;

/**
 * same as BeanWrapperImpl, include the Map
 * @author fengxxc
 */
public class DataWrapper {
    private boolean isMap = false;
    private Map<CharSequence, Object> map;
    private BeanWrapperImpl beanWrapper;

    public DataWrapper(Object obj) {
        if (Map.class.isAssignableFrom(obj.getClass())) {
            this.isMap = true;
            map = (Map) obj;
            return;
        }
        beanWrapper = new BeanWrapperImpl(obj);
    }

    public DataWrapper(Class clazz) throws IllegalAccessException, InstantiationException {
        if (Map.class.isAssignableFrom(clazz)) {
            this.isMap = true;
            if (clazz.isInterface()) {
                map = new HashMap<>();
            } else {
                map = (Map) clazz.newInstance();
            }
            return;
        }
        beanWrapper = new BeanWrapperImpl(clazz);
    }

    public void setPropertyValue(String property, Object value) throws BeansException {
        if (isMap) {
            map.put(property, value);
            return;
        }
        beanWrapper.setPropertyValue(property, value);
    }

    public Object getPropertyValue(String property) {
        if (isMap) {
            return map.get(property);
        }
        return beanWrapper.getPropertyValue(property);
    }

    public Object getRootInstance() {
        if (isMap) {
            return map;
        }
        return beanWrapper.getRootInstance();
    }
}
