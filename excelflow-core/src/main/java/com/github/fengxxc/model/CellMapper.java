package com.github.fengxxc.model;

import org.apache.poi.ss.util.CellReference;

/**
 * @author fengxxc
 * @date 2023-04-01
 */
public class CellMapper<T, R> extends ElementMapper<T, R> implements Comparable<CellMapper<T, R>> {


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
    public int compareTo(CellMapper o) {
        /*final int sheetDiff = this.getParentBunch().getSheetAt() - o.getParentBunch().getSheetAt();
        if (sheetDiff != 0) {
            return sheetDiff;
        }*/
        final int yDiff = this.getPoint().Y - o.getPoint().Y;
        if (yDiff != 0) {
            return ((int) yDiff);
        }
        return ((int) (this.getPoint().X - o.getPoint().X));
    }

    @Override
    public String toString() {
        return "CellMapper{" +
                ", point=" + super.getPoint().toString() +
                ", objectProperty='" + super.getObjectProperty().toString() + '\'' +
                '}';
    }
}
