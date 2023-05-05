package com.github.fengxxc;

import com.github.fengxxc.model.Foward;
import com.github.fengxxc.model.Offset;
import com.github.fengxxc.model.Point;
import com.github.fengxxc.model.PropVal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * just we means just we，既不伟大也不卑微~
 *
 * 当然是能上能下能摘能插的工具人啦~
 *
 * @author fengxxc
 */
public class JustWe<S, T> {
    private int id = -1;
    private Class<T> object;
    // private int sheetAt = -1;
    private String sheet;
    // T: cellRef, U: value, R: Offset
    private BiFunction<String, PropVal<T, ? extends Object>, Offset> nextFunc;

    private Point endPoint;
    private Collection<? extends ElementMapper<T, ?>> mappers;

    private Map<EventType, Consumer<T>> callbacks = new HashMap<>();

    public int getId() {
        return id;
    }

    public S setId(int id) {
        this.id = id;
        return (S) this;
    }

    public Class<T> getObject() {
        return object;
    }

    public S setObject(Class<T> object) {
        this.object = object;
        return (S) this;
    }

    public String getSheet() {
        return sheet;
    }

    public S sheet(String sheet) {
        this.sheet = sheet;
        return (S) this;
    }

    /*public int getSheetAt() {
        return sheetAt;
    }

    public Bunch setSheetAt(int sheetAt) {
        this.sheetAt = sheetAt;
        return this;
    }*/

    public BiFunction<String, PropVal<T, ? extends Object>, Offset> getNextFunc() {
        return nextFunc;
    }

    /**
     * where next cell offset
     * @param nextFunc
     * @return
     */
    public S next(BiFunction<String, PropVal<T, ? extends Object>, Offset> nextFunc) {
        this.nextFunc = nextFunc;
        return (S) this;
    }

    public S foward(Foward foward) {
        this.foward(foward, 1);
        return (S) this;
    }

    public S foward(Foward foward, int stepLength) {
        this.nextFunc = (cellRefence, propVal) -> {
            switch (foward) {
                case Right:
                    return Offset.of(1, 0);
                case Down:
                    return Offset.of(0, 1);
            }
            // default Down
            return Offset.of(0, 1);
        };
        return (S) this;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public S setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
        return (S) this;
    }

    public Collection<? extends ElementMapper<T, ?>> getMappers() {
        return mappers;
    }

    public void setMappers(Collection<? extends ElementMapper<T, ?>> mappers) {
        this.mappers = mappers;
    }

    public S addCallback(EventType event, Consumer<T> callback) {
        callbacks.put(event, callback);
        return (S) this;
    }

    public Consumer<T> getCallback(EventType event) {
        return callbacks.get(event);
    }
}
