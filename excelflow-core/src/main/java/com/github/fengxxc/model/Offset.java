package com.github.fengxxc.model;

/**
 * @author fengxxc
 * @date 2023-04-28
 */
public class Offset {
    int x;
    int y;

    private Offset() {
    }

    private Offset(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Offset of(int x, int y) {
        return new Offset(x, y);
    }

    public int getX() {
        return x;
    }

    public Offset setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public Offset setY(int y) {
        this.y = y;
        return this;
    }
}
