package com.github.fengxxc.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author fengxxc
 */
public class QueueHashMap<K, V> {
    private LinkedHashMap<K, V> lm;

    public QueueHashMap() {
        this.lm = new LinkedHashMap<K, V>();
    }

    public Map.Entry<K, V> peek() {
        Set<Map.Entry<K, V>> entries = lm.entrySet();
        Iterator<Map.Entry<K, V>> iterator = entries.iterator();
        if (iterator.hasNext()) {
            Map.Entry<K, V> next = iterator.next();
            return next;
        }
        return null;
    }

    public Map.Entry<K, V> poll() {
        Map.Entry<K, V> entry = peek();
        lm.remove(entry.getKey());
        return entry;
    }

    public void offer(K key, V value) {
        lm.put(key, value);
    }

    public V get(K key) {
        return lm.get(key);
    }

    public V remove(K key) {
        return lm.remove(key);
    }

    public static void main(String[] args) {
        QueueHashMap<String, Integer> qm = new QueueHashMap<>();
        System.out.println("qm.peek().toString() = " + qm.peek().toString());

        qm.offer("A1", 1);
        qm.offer("B1", 2);
        qm.offer("C1", 3);
        qm.offer("D1", 4);
        qm.offer("A2", 5);
        qm.offer("B2", 6);

        System.out.println("qm.peek().toString() = " + qm.peek().toString());
        System.out.println("qm.peek().toString() = " + qm.peek().toString());
        qm.poll();
        System.out.println("qm.peek().toString() = " + qm.peek().toString());
        System.out.println("qm.peek().toString() = " + qm.peek().toString());
        qm.remove("B1");
        System.out.println("qm.peek().toString() = " + qm.peek().toString());

    }
}
