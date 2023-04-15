package com.github.fengxxc.model;

import com.github.fengxxc.CellMappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author fengxxc
 * @date 2023-04-01
 */
public class Part<T> {
    private int id = -1;
    // private int sheetAt = -1;
    private String sheet;
    private List<CellMapper<T>> cellMappers;
    private Boolean iterative = false;
    private Foward foward = Foward.Down;
    private int stepLength = 1;
    private int stepTotal = -1;

    private Point endPoint;

    private Class<T> object;

    private Part() {
    }

    public static <T> Part<T> of(int id, Class<T> object) {
        return new Part<T>().setId(id);
    }

    public static <T> Part<T> of(Class<T> object) {
        return new Part<T>().setObject(object);
    }

    public static <T> Part<T> of(Class<T> object, boolean iterative) {
        return new Part<T>().setObject(object).iterative(iterative);
    }

    public int getId() {
        return id;
    }

    public Part<T> setId(int id) {
        this.id = id;
        return this;
    }

    /*public int getSheetAt() {
        return sheetAt;
    }

    public Bunch setSheetAt(int sheetAt) {
        this.sheetAt = sheetAt;
        return this;
    }*/

    public String getSheet() {
        return sheet;
    }

    public Part<T> sheet(String sheet) {
        this.sheet = sheet;
        return this;
    }

    public List<CellMapper<T>> getCellMappers() {
        return cellMappers;
    }

    public Part<T> setCellMappers(CellMapper<T>... cellMappers) {
        final ArrayList<CellMapper<T>> mappers = new ArrayList<>(Arrays.asList(cellMappers));
        this.cellMappers = mappers;
        return this;
    }

    public Part<T> cellMap(Consumer<CellMappers<T>> func) {
        final CellMappers<T> mapper = new CellMappers<T>();
        func.accept(mapper);
        final List<CellMapper<T>> mappers = mapper.getMappers();
        this.cellMappers = mappers;
        return this;
    }

    public Boolean iterative() {
        return iterative;
    }

    public Part<T> iterative(Boolean iterative) {
        this.iterative = iterative;
        return this;
    }

    public Foward getFoward() {
        return foward;
    }

    public Part<T> foward(Foward foward) {
        this.foward = foward;
        return this;
    }

    public int getStepLength() {
        return stepLength;
    }

    public Part<T> setStepLength(int stepLength) {
        this.stepLength = stepLength;
        return this;
    }

    public int getStepTotal() {
        return stepTotal;
    }

    public Part<T> setStepTotal(int stepTotal) {
        this.stepTotal = stepTotal;
        return this;
    }

    public Class getObject() {
        return object;
    }

    public Part<T> setObject(Class<T> object) {
        this.object = object;
        return this;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public Part<T> setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
        return this;
    }
}
