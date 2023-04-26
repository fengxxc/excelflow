package com.github.fengxxc.read;

import com.github.fengxxc.ElementMapper;
import com.github.fengxxc.model.Point;
import org.apache.poi.ss.util.CellReference;

/**
 * @author fengxxc
 */
public class CellMapper<T, R> extends ElementMapper<T, R> {


    private CellMapper() {
    }

    public static <T, R> CellMapper<T, R> of(Point point) {
        CellMapper<T, R> cellMapper = new CellMapper<>();
        cellMapper.setPoint(point);
        return cellMapper;
    }

    public static <T, R> CellMapper<T, R> of(int y, int x) {
        return of(Point.of(y, x));
    }

    public static <T, R> CellMapper<T, R> of(String colRef, int rowRef) {
        final int x = CellReference.convertColStringToIndex(colRef);
        return of(rowRef - 1, x);
    }

    public static <T, R> CellMapper<T, R> of(String cellRef) {
        final Point point = Point.of(cellRef);
        return of(point);
    }

    public static <T, R> CellMapper<T, R> reOf(CellMapper<T, ?> cellMapper) {
        CellMapper<T, R> reMapper = new CellMapper<T, R>();
        reMapper.setParentId(cellMapper.getParentId());
        reMapper.setPoint(cellMapper.getPoint());
        return reMapper;
    }




    @Override
    public String toString() {
        return "CellMapper{" +
                ", point=" + super.getPoint().toString() +
                ", objectProperty='" + super.getObjectProperty().toString() + '\'' +
                '}';
    }
}
