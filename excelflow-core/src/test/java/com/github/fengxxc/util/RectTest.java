package com.github.fengxxc.util;

import com.github.fengxxc.model.Point;
import com.github.fengxxc.model.RTreeNode;
import com.github.fengxxc.model.Rect;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author fengxxc
 * @date 2023-04-11
 */
public class RectTest {

    @Test
    public void topology() {
        final RTreeNode<Integer> root = new RTreeNode<Integer>(RTreeNode.DEFAULT_CAPACITY, Rect.of(Point.of(25, 11), Point.of(27, 10)));
        final Rect.Topology topology = root.getRect().topology(Rect.of(Point.of(26, 10), Point.of(26, 10)));
        // System.out.println("topology = " + topology);
        Assert.assertEquals(Rect.Topology.Cover, topology);
    }
}