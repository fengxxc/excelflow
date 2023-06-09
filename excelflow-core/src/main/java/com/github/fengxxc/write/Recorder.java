package com.github.fengxxc.write;

import com.github.fengxxc.JustWe;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author fengxxc
 */
public class Recorder<T> extends JustWe<Recorder<T>, T> implements Comparable<Recorder<T>> {
    private Iterator<T> iterator;

    private Recorder() {
    }

    public static <T> Recorder<T> of() {
        return new Recorder<T>();
    }

    public static <T> Recorder<T> of(int id) {
        return new Recorder<T>().setId(id);
    }

    public static <T> Recorder<T> of(int id, Class<T> object) {
        return new Recorder<T>().setId(id);
    }

    public static <T> Recorder<T> of(Class<T> object) {
        return new Recorder<T>().setObject(object);
    }

    public Recorder<T> source(Iterator<T> iterator) {
        this.iterator = iterator;
        return this;
    }

    public Iterator<T> getSourceIterator() {
        return this.iterator;
    }

    public Recorder<T> propMap(Consumer<PropMappers<T>> func) {
        final PropMappers<T> mapper = new PropMappers<T>();
        func.accept(mapper);
        final TreeSet<PropMapper<T, ?>> mappers = mapper.getMappers();
        super.setMappers(mappers);
        return this;
    }

    public Recorder<T> propMap(PropMapper<T, ?>... propMappers) {
        TreeSet<PropMapper<T, ?>> mapperSet = new TreeSet<>();
        for (int i = 0; i < propMappers.length; i++) {
            mapperSet.add(propMappers[i]);
        }
        super.setMappers(mapperSet);
        return this;
    }

    @Override
    public int compareTo(Recorder<T> o) {
        TreeSet<PropMapper<T, ?>> mappers = (TreeSet<PropMapper<T, ?>>) this.getMappers();
        TreeSet<PropMapper<T, ?>> oMappers = (TreeSet<PropMapper<T, ?>>) o.getMappers();
        return mappers.first().compareTo(oMappers.first());
    }
}
