package com.github.fengxxc.write;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author fengxxc
 */
public class ObjIterator<T extends Object> implements Iterator {
    private BlockingQueue<T> objQueue = new ArrayBlockingQueue<T>(7);
    private Class<T> kind;
    private volatile boolean finish = false;
    private volatile T endSignal;

    private ObjIterator(Class<T> kind, T endSignal) {
        this.kind = kind;
        this.endSignal = endSignal;
    }

    public static <T> ObjIterator<T> of(Class<T> kind, T endSignal) {
        return new ObjIterator<T>(kind, endSignal);
    }

    public Class<T> getKind() {
        return kind;
    }

    public void put(T obj) throws InterruptedException {
        this.objQueue.put(obj);
    }

    public void putEndSignal() throws InterruptedException {
        put(this.endSignal);
    }

    @Override
    public boolean hasNext() {
        return !(finish || (objQueue.peek() != null && objQueue.peek() == this.endSignal));
    }

    /**
     * may be next is null at last, you need check it.
     * @return
     */
    @Override
    public T next() {
        T obj = null;
        if (finish) {
            return obj;
        }
        try {
            // System.out.println("will take");
            obj = objQueue.take();
            // System.out.println("taked: " + obj.toString());
            if (obj == this.endSignal) {
                finish = true;
                // System.out.println("obj == this.endSignal");
                return null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
