package com.github.fengxxc.model;

import java.util.Objects;

/**
 * @author fengxxc
 * @date 2023-04-08
 */
public class Rect {
    private Point topRight;
    private Point bottomLeft;

    private Rect() {
    }

    private Rect(Point topRight, Point bottomLeft) {
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
    }

    public Point getTopRight() {
        return topRight;
    }

    public Rect setTopRight(Point topRight) {
        this.topRight = topRight;
        return this;
    }

    public Point getBottomLeft() {
        return bottomLeft;
    }

    public Rect setBottomLeft(Point bottomLeft) {
        this.bottomLeft = bottomLeft;
        return this;
    }

    public static Rect of(Point topRight, Point bottomLeft) {
        return new Rect(topRight, bottomLeft);
    }

    public Topology topology(Rect rect) {
        if (rect.getBottomLeft().X < bottomLeft.X && topRight.X < rect.topRight.X
                && rect.getTopRight().Y < topRight.Y && bottomLeft.Y < rect.getBottomLeft().Y) {
            return Topology.Covered;
        }
        if (bottomLeft.X == rect.getBottomLeft().X && topRight.X == rect.getTopRight().X
                && bottomLeft.Y == rect.getBottomLeft().Y && topRight.Y == rect.getTopRight().Y) {
            return Topology.Equal;
        }
        if (bottomLeft.X <= rect.getBottomLeft().X && rect.getTopRight().X <= topRight.X
                && topRight.Y <= rect.getTopRight().Y && rect.getBottomLeft().Y <= bottomLeft.Y) {
            return Topology.Cover;
        }
        if (topRight.X < rect.getBottomLeft().X || rect.getTopRight().X < bottomLeft.X
                || bottomLeft.Y < rect.getTopRight().Y || rect.getBottomLeft().Y < topRight.Y) {
            return Topology.Disjoint;
        }
        return Topology.Intersect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rect rect = (Rect) o;
        return Objects.equals(topRight, rect.topRight) &&
                Objects.equals(bottomLeft, rect.bottomLeft);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topRight, bottomLeft);
    }

    @Override
    public String toString() {
        return "Rect{" +
                "topRight=" + topRight.toString() +
                ", bottomLeft=" + bottomLeft.toString() +
                '}';
    }

    public enum Topology {
        Covered,
        Equal,
        Cover,
        Disjoint,
        Intersect,
    }
}
