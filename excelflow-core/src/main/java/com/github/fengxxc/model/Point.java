package com.github.fengxxc.model;

import org.apache.poi.ss.util.CellReference;

import java.util.Objects;

/**
 * @author fengxxc
 * @date 2023-04-08
 */
public class Point {
    public int Y;
    public int X;

    private Point(int y, int x) {
        Y = y;
        X = x;
    }

    public static Point of(int y, int x) {
        return new Point(y, x);
    }

    public static Point of(String cellRef) {
        int digitIdx = -1;
        for (int i = 0; i < cellRef.length(); i++) {
            if ('0' <= cellRef.charAt(i) && cellRef.charAt(i) <= '9') {
                digitIdx = i;
                break;
            }
        }
        if (digitIdx <= 0) {
            throw new IllegalArgumentException("Bad cell ref format '" + cellRef + "'");
        }
        final String colRef = cellRef.substring(0, digitIdx);
        final int x = CellReference.convertColStringToIndex(colRef);
        final Integer rowRef = Integer.valueOf(cellRef.substring(digitIdx));
        return of(rowRef - 1, x);
    }

    public int getAxis(char xOrY) {
        return xOrY == 'x' || xOrY == 'X' ? X : Y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return Y == point.Y &&
                X == point.X;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Y, X);
    }

    @Override
    public String toString() {
        return "Point{" +
                "Y=" + Y +
                ", X=" + X +
                '}';
    }
}
