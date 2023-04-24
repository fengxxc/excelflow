package com.github.fengxxc.write;

import com.github.fengxxc.CellMappers;
import com.github.fengxxc.JustWe;
import com.github.fengxxc.PropMappers;
import com.github.fengxxc.model.CellMapper;
import com.github.fengxxc.model.Picker;
import com.github.fengxxc.model.PropMapper;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author fengxxc
 * @date 2023-04-20
 */
public class Recorder<T> extends JustWe<Recorder<T>, T> implements Comparable<Recorder<T>> {
    private Iterator<T> iterator;

    private Recorder() {
    }

    public static <T> Recorder<T> of() {
        return new Recorder<T>();
    }

    public static <T> Recorder<T> of(int id, Class<T> object) {
        return new Recorder<T>().setId(id);
    }

    public static <T> Recorder<T> of(Class<T> object) {
        return new Recorder<T>().setObject(object);
    }

    public static <T> Recorder<T> of(Class<T> object, boolean iterative) {
        return new Recorder<T>().setObject(object).iterative(iterative);
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
