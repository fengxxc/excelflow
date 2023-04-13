package com.github.fengxxc.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple RTree, No balancing
 * @author fengxxc
 * @date 2023-04-08
 */
public class RTreeNode<T> {
    public static final int DEFAULT_CAPACITY = 3;
    private Rect rect;
    private List<T> entrys;
    private int nodeCapacity;
    private RTreeNode<T> parent;
    protected RTreeNode<T>[] children;
    private int childAddIndex;

    public RTreeNode(int nodeCapacity, Rect rect) {
        if (nodeCapacity < 2) {
            throw new IllegalArgumentException("node capacity must >= 2");
        }
        this.nodeCapacity = nodeCapacity;
        this.children = new RTreeNode[nodeCapacity];
        this.rect = rect;
    }

    public RTreeNode(Rect rect) {
        this(DEFAULT_CAPACITY, rect);
    }

    public Rect getRect() {
        return rect;
    }

    public RTreeNode<T> setRect(Rect rect) {
        this.rect = rect;
        return this;
    }

    public List<T> getEntrys() {
        return entrys;
    }

    public RTreeNode<T> setEntrys(LinkedList<T> entrys) {
        this.entrys = entrys;
        return this;
    }

    public RTreeNode<T> addEntry(T entry) {
        if (this.entrys == null) {
            this.entrys = new LinkedList<>();
        }
        this.entrys.add(entry);
        return this;
    }

    public RTreeNode<T> addEntrys(Collection <? extends T> entrys) {
        if (this.entrys == null) {
            this.entrys = new LinkedList<>();
        }
        this.entrys.addAll(entrys);
        return this;
    }

    public RTreeNode<T> getParent() {
        return parent;
    }

    public RTreeNode<T> setParent(RTreeNode<T> parent) {
        this.parent = parent;
        return this;
    }

    public RTreeNode<T>[] getChildren() {
        return children;
    }

    public RTreeNode<T> setChildren(RTreeNode<T>[] children) {
        this.children = children;
        return this;
    }

    public RTreeNode<T> add(RTreeNode<T> newNode) {
        Rect newRect = newNode.getRect();
        List<T> newEntry = newNode.getEntrys();
        final Rect.Topology topology = rect.topology(newRect);
        switch (topology) {
            case Covered:
                return addAsParent(newNode);
            case Equal:
                entrys.addAll(newEntry);
                return this;
            case Cover:
                return addAsChildren(newNode);
            case Disjoint:
            case Intersect:
                return addAsSibling(newNode);
        }
        return this;
    }

    public RTreeNode<T> addAsParent(RTreeNode<T> parentNode) {
        this.parent.add(parentNode);
        return this;
    }

    private RTreeNode<T> addAsChildren(RTreeNode<T> child) {
        List<RTreeNode<T>> childchildren = new LinkedList<>();
        for (int i = 0; i < this.children.length; i++) {
            if (this.children[i] == null) {
                break; // or continue if children not continuous
            }
            final Rect.Topology topology = this.children[i].getRect().topology(child.getRect());
            switch (topology) {
                case Equal:
                    /* equal with someone child */
                    this.children[i].addEntrys(child.getEntrys());
                    return this;
                case Cover:
                    /* as child of someone child */
                    this.children[i].addAsChildren(child);
                    return this;
                case Covered:
                    childchildren.add(this.children[i]);
                    break;
            }
        }
        if (childchildren.size() > 0) {
            /* as parent of some children */
            for (int i = 0; i < childchildren.size(); i++) {
                final RTreeNode<T> childchild = childchildren.get(i);
                childchild.setParent(child);
                child.children[i] = childchild;
            }
            child.setParent(this);
            return this;
        }
        /* as sibling of children */
        if (this.childAddIndex >= this.children.length - 1) {
            // out of children size, split last child
            final RTreeNode<T> lastChild = this.children[this.children.length - 1];
            final Rect splitRect = computeMinBoundRect(lastChild.getRect(), child.getRect());
            final RTreeNode<T> splitNode = new RTreeNode<T>(this.children.length, splitRect).setParent(this);
            splitNode.setChildren(new RTreeNode[]{
                    child.setParent(splitNode),
                    lastChild.setParent(splitNode)
            });
            this.children[this.children.length - 1] = splitNode;
            return this;
        }
        child.setParent(this);
        this.children[this.childAddIndex++] = child;
        return this;
    }

    private RTreeNode<T> addAsSibling(RTreeNode<T> sibling) {
        if (this.parent == null) {
            final Rect unionRect = computeMinBoundRect(this.getRect(), sibling.getRect());
            final RTreeNode<T> parent = new RTreeNode<>(this.nodeCapacity, unionRect);
            parent.children[0] = this;
            parent.children[1] = sibling;
            this.setParent(parent);
            sibling.setParent(parent);
            return this;
        }
        return this.parent.add(sibling);
    }

    public RTreeNode<T> getRoot() {
        RTreeNode<T> node = this;
        while (node.getParent() != null) {
            node = node.getParent();
        }
        return node;
    }

    public List<T> search(Point point) {
        return search(Rect.of(point, point));
    }

    public List<T> search(Rect rect) {
        RTreeNode<T> root = this.getRoot();
        final RTreeNode<T> search = search(root, rect);
        if (search == null) {
            return null;
        }
        return search.getEntrys();
    }

    public static <T> RTreeNode<T> search(RTreeNode<T> root, Rect rect) {
        if (root.getRect().topology(rect) == Rect.Topology.Cover) {
            for (int i = 0; i < root.getChildren().length; i++) {
                final RTreeNode<T> child = root.getChildren()[i];
                if (child == null) {
                    break; // or continue if children not continuous
                }
                final RTreeNode<T> find = search(child, rect);
                if (find != null) {
                    return find;
                }
            }
            return root;
        }
        return null;
    }

    private static Rect computeMinBoundRect(Rect... rects) {
        int top = Integer.MAX_VALUE, right = 0, bottom = 0, left = Integer.MAX_VALUE;
        for (int i = 0; i < rects.length; i++) {
            final Rect rect = rects[i];
            top = Math.min(top, rect.getTopRight().Y);
            right = Math.max(right, rect.getTopRight().X);
            bottom = Math.max(bottom, rect.getBottomLeft().Y);
            left = Math.min(left, rect.getBottomLeft().X);
        }
        return Rect.of(Point.of(top, right), Point.of(bottom, left));
    }

    public String rectToJsonString(String prefix, int level) {
        final StringBuffer sb = new StringBuffer();
        final Point topRight = this.getRect().getTopRight();
        final Point bottomLeft = this.getRect().getBottomLeft();
        sb.append(prefix.repeat(level)).append("{\n")
                .append(prefix.repeat(level+1)).append("topRight: '(").append(topRight.X).append(",").append(topRight.Y).append(")'")
                .append(", bottomLeft: '(").append(bottomLeft.X).append(",").append(bottomLeft.Y).append(")'");
        for (int i = 0; i < this.children.length; i++) {
            final RTreeNode<T> child = this.children[i];
            if (child == null) {
                if (i == 0) {
                    sb.append("\n");
                    break;
                }
                sb.append(prefix.repeat(level+1)).append("]\n");
                break;
            }
            if (i == 0) {
                sb.append(", \n").append(prefix.repeat(level+1)).append("childen: [\n");
            }
            sb.append(child.rectToJsonString(prefix, level+2));
            if (i == this.children.length - 1) {
                sb.append(prefix.repeat(level+1)).append("]\n");
            }

        }
        sb.append(prefix.repeat(level)).append("}, \n");
        return sb.toString();
    }
}
