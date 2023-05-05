package com.github.fengxxc.model;

import com.github.fengxxc.exception.ExcelFlowConfigException;

/**
 * @author fengxxc
 */
public class Offset {
    int x = 0;
    int y = 0;

    private Offset() {
    }

    private Offset(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public static Offset of() {
        return new Offset();
    }

    public static Offset of(int x, int y) {
        return new Offset(x, y);
    }

    public int getX() {
        return x;
    }

    public Offset setX(int x) {
        validate(x, this.y);
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public Offset setY(int y) {
        validate(this.x, y);
        this.y = y;
        return this;
    }

    public static void validate(int x, int y) {
        if (x < 0 && y < 0) {
            throw new ExcelFlowConfigException("offset x and y cannot both be less than 0, because reading Excel cells is forward from left-top to right-bottom.");
        }
    }
}
