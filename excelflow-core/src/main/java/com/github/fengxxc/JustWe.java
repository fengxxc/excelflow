package com.github.fengxxc;

import com.github.fengxxc.model.ElementMapper;
import com.github.fengxxc.model.Foward;
import com.github.fengxxc.model.Point;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * just we means just we，既不伟大也不卑微~
 *
 * 当然是能上能下能摘能插的工具人啦~
 *
 * @author fengxxc
 * @date 2023-04-20
 */
public class JustWe<S, T> {
    private int id = -1;
    private Class<T> object;
    // private int sheetAt = -1;
    private String sheet;
    private Boolean iterative = false;
    private Foward foward = Foward.Down;
    private int stepLength = 1;
    private int stepTotal = -1;
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

    public Boolean iterative() {
        return iterative;
    }

    public S iterative(Boolean iterative) {
        this.iterative = iterative;
        return (S) this;
    }

    public Foward getFoward() {
        return foward;
    }

    public S foward(Foward foward) {
        this.foward = foward;
        return (S) this;
    }

    public int getStepLength() {
        return stepLength;
    }

    public S setStepLength(int stepLength) {
        this.stepLength = stepLength;
        return (S) this;
    }

    public int getStepTotal() {
        return stepTotal;
    }

    public S setStepTotal(int stepTotal) {
        this.stepTotal = stepTotal;
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
