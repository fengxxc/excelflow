package com.github.fengxxc.util;

import com.github.fengxxc.model.Point;
import com.github.fengxxc.model.RTreeNode;
import com.github.fengxxc.model.Rect;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author fengxxc
 * @date 2023-04-11
 */
public class RTreeNodeTest {
    private static final int CAPACITY = 2;
    private RTreeNode<Integer> rtNode = null;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void add() {
        final RTreeNode<Integer> node = new RTreeNode<Integer>(CAPACITY, Rect.of(
                Point.of(10, 12),
                Point.of(19, 5)
        )).add(new RTreeNode<Integer>(CAPACITY, Rect.of(
                Point.of(23, 14),
                Point.of(32, 9)
        ))).add(new RTreeNode<Integer>(CAPACITY, Rect.of(
                Point.of(25, 11),
                Point.of(27, 10)
        )));
        final RTreeNode<Integer> root = node.getRoot();

        Assert.assertNull(root.getParent());
        Assert.assertEquals(Rect.of(Point.of(10, 14), Point.of(32, 5)), root.getRect());
        Assert.assertNotNull(root.getChildren()[0]);
        Assert.assertNotNull(root.getChildren()[1]);
        Assert.assertEquals(Rect.of(Point.of(10, 12), Point.of(19, 5)), root.getChildren()[0].getRect());
        Assert.assertEquals(Rect.of(Point.of(23, 14), Point.of(32, 9)), root.getChildren()[1].getRect());
        Assert.assertNotNull(root.getChildren()[1].getChildren()[0]);
        Assert.assertEquals(Rect.of(Point.of(25, 11), Point.of(27, 10)), root.getChildren()[1].getChildren()[0].getRect());
    }

    @Test
    public void rectToJsonString() {
        final RTreeNode<Integer> node = new RTreeNode<Integer>(CAPACITY, Rect.of(
                Point.of(10, 12),
                Point.of(19, 5)
        )).add(new RTreeNode<Integer>(CAPACITY, Rect.of(
                Point.of(23, 14),
                Point.of(32, 9)
        ))).add(new RTreeNode<Integer>(CAPACITY, Rect.of(
                Point.of(25, 11),
                Point.of(27, 10)
        ))).add(new RTreeNode<Integer>(CAPACITY, Rect.of(
                Point.of(15, 17),
                Point.of(27, 14)
        )));
        final RTreeNode<Integer> root = node.getRoot();
        System.out.println("root node is: ");
        System.out.println(root.rectToJsonString("  ", 0));
    }

    @Test
    public void getRoot() {
        final RTreeNode<Integer> node = new RTreeNode<Integer>(CAPACITY, Rect.of(
                Point.of(10, 12),
                Point.of(19, 5)
        )).add(new RTreeNode<Integer>(CAPACITY, Rect.of(
                Point.of(23, 14),
                Point.of(32, 9)
        ))).add(new RTreeNode<Integer>(CAPACITY, Rect.of(
                Point.of(25, 11),
                Point.of(27, 10)
        )));
        final RTreeNode<Integer> root = node.getRoot();
        // System.out.println("root node is: ");
        Assert.assertNull(root.getParent());
        Assert.assertEquals(Rect.of(Point.of(10, 14), Point.of(32, 5)), root.getRect());
    }

    @Test
    public void search() {
        final RTreeNode<Integer> node = new RTreeNode<Integer>(CAPACITY, Rect.of(
                Point.of(10, 12),
                Point.of(19, 5)
        )).add(new RTreeNode<Integer>(CAPACITY, Rect.of(
                Point.of(23, 14),
                Point.of(32, 9)
        ))).add(new RTreeNode<Integer>(CAPACITY, Rect.of(
                Point.of(25, 11),
                Point.of(27, 10)
        ))).add(new RTreeNode<Integer>(CAPACITY, Rect.of(
                Point.of(15, 17),
                Point.of(27, 14)
        )));
        final RTreeNode<Integer> root = node.getRoot();
        final RTreeNode<Integer> search = RTreeNode.<Integer>search(root, Rect.of(Point.of(26, 10), Point.of(26, 10)));
        System.out.println(search.rectToJsonString("  ", 0));
        Assert.assertEquals(Rect.of(Point.of(25, 11), Point.of(27, 10)), search.getRect());
    }
}