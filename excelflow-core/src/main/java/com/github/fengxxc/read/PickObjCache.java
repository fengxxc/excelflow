package com.github.fengxxc.read;

import com.github.fengxxc.DataWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fengxxc
 */
public class PickObjCache {
    private Map<Integer, Map<Integer, DataWrapper>> map = new HashMap<>();

    public DataWrapper get(Integer pickId, Integer objCacheId) {
        Map<Integer, DataWrapper> objMap = this.map.get(pickId);
        if (objMap == null) {
            return null;
        }
        return objMap.get(objCacheId);
    }

    public void put(Integer pickId, Integer objCacheId, DataWrapper obj) {
        Map<Integer, DataWrapper> objMap = this.map.get(pickId);
        if (objMap == null) {
            objMap = new HashMap<Integer, DataWrapper>();
        }
        objMap.put(objCacheId, obj);
        this.map.put(pickId, objMap);
    }

    public void remove(Integer pickId, Integer objCacheId) {
        Map<Integer, DataWrapper> objMap = this.map.get(pickId);
        if (objMap == null) {
            return;
        }
        objMap.remove(objCacheId);
    }
}
