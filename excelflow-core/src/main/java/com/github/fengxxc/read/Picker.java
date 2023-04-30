package com.github.fengxxc.read;

import com.github.fengxxc.EventType;
import com.github.fengxxc.JustWe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author fengxxc
 */
public class Picker<T> extends JustWe<Picker<T>, T> {
    // private List<CellMapper<T, ?>> cellMappers;


    private Picker() {
    }

    public static <T> Picker<T> of(int id, Class<T> object) {
        return new Picker<T>().setId(id);
    }

    public static <T> Picker<T> of(Class<T> object) {
        return new Picker<T>().setObject(object);
    }

    public Picker<T> cellMap(Consumer<CellMappers<T>> func) {
        final CellMappers<T> mapper = new CellMappers<T>();
        func.accept(mapper);
        final List<CellMapper<T, ?>> mappers = mapper.getMappers();
        super.setMappers(mappers);
        return this;
    }

    public Picker<T> cellMap(CellMapper<T, ?>... cellMappers) {
        final ArrayList<CellMapper<T, ?>> mappers = new ArrayList<>(Arrays.asList(cellMappers));
        super.setMappers(mappers);
        return this;
    }

    public Consumer<T> getOnPickCallback() {
        return super.getCallback(EventType.PICK);
    }

    public Picker<T> onPick(Consumer<T> onPickCallback) {
        return super.addCallback(EventType.PICK, onPickCallback);
    }
}
