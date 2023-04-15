package com.github.fengxxc.read;

import com.github.fengxxc.exception.ExcelFlowReflectionException;
import com.github.fengxxc.model.*;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.TypeMismatchException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author fengxxc
 * @date 2023-04-02
 */
public class ExcelFlowSaxHandler<R> implements XSSFSheetXMLHandler.SheetContentsHandler {
    private String sheetName;
    private SharedStringsTable sst;
    private Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap;
    private Consumer<EFCell> cellCallback;
    private final BiConsumer<Integer, Object> partCallback;
    private Map<Part, BeanWrapperImpl> partObjCache = new HashMap<>();

    public ExcelFlowSaxHandler(String sheetName, SharedStringsTable sst, Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap, Consumer<EFCell> cellCallback, BiConsumer<Integer, Object> partCallback) {
        this.sheetName = sheetName;
        this.sst = sst;
        this.sheet2CellTreeMap = sheet2CellTreeMap;
        this.cellCallback = cellCallback;
        this.partCallback = partCallback;
    }

    @Override
    public void headerFooter(String text, boolean isHeader, String tagName) {}

    @Override
    public void endSheet() {}

    @Override
    public void startRow(int rowNum) {}

    @Override
    public void endRow(int rowNum) {}

    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment comment) {
        // System.out.println("cellReference = " + cellReference);
        // System.out.println("formattedValue = " + formattedValue);
        // System.out.println("comment = " + comment);
        final RTreeNode<CellMapper> cellRTreeNode = sheet2CellTreeMap.get(sheetName);
        if (cellRTreeNode == null) {
            return;
        }
        final List<CellMapper> searchCellMappers = cellRTreeNode.search(Point.of(cellReference));
        if (searchCellMappers == null || searchCellMappers.size() == 0) {
            return;
        }

        // System.out.println("searchGrids = " + Arrays.toString(searchCellMappers.toArray()));
        final EFCell efCell = new EFCell(cellReference, formattedValue, searchCellMappers);
        this.cellCallback.accept(efCell);

        for (CellMapper cellMapper : searchCellMappers) {
            Part part = cellMapper.getPart();
            if (!part.getSheet().equals(this.sheetName)) {
                continue;
            }

            BeanWrapperImpl beanWrapper = partObjCache.get(part);
            if (beanWrapper == null) {
                try {
                    beanWrapper = new BeanWrapperImpl(createInstance(part.getObject()));
                } catch (InstantiationException| IllegalAccessException| InvocationTargetException| NoSuchMethodException e) {
                    throw new ExcelFlowReflectionException("can not create class '" + part.getObject().getName() + "' instance.");
                    // e.printStackTrace();
                }
                partObjCache.put(part, beanWrapper);
            }

            boolean arriveEndPoint = isArriveEndPoint(cellReference, part);
            if (arriveEndPoint) {
                // beanWrapper completed
                this.partCallback.accept(part.getId(), beanWrapper.getRootInstance());
            } else {
                // beanWrapper not complete, make property
                try {
                    setFieldValue(beanWrapper, cellMapper.getObjectProperty(), formattedValue);
                } catch (TypeMismatchException e) {
                    throw new ExcelFlowReflectionException("can not set property '" + cellMapper.getObjectProperty() + "' value '" + formattedValue + "', mismatch type.");
                    // e.printStackTrace();
                }
            }

        }
    }

    private boolean isArriveEndPoint(String cellReference, Part part) {
        boolean isFowardY = part.getFoward() == Foward.Up || part.getFoward() == Foward.Down;
        if (part.getEndPoint().getAxis(isFowardY ? 'x' : 'y') != Point.of(cellReference).getAxis(isFowardY ? 'x' : 'y')) {
            return false;
        }
        int axisVal = Point.of(cellReference).getAxis(isFowardY ? 'y' : 'x');
        boolean arriveEndPoint = Math.abs(axisVal - part.getEndPoint().getAxis(isFowardY ? 'y' : 'x')) % part.getStepLength() == 0;
        return arriveEndPoint;
    }

    private static <T> T createInstance(Class<T> object, Object... args) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class<?>[] parameterTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        T newObj = null;
        newObj = object.getDeclaredConstructor(parameterTypes).newInstance();
        return newObj;
    }

    public static void setFieldValue(BeanWrapperImpl beanWrapper, String fieldName, Object fieldValue) {
        beanWrapper.setPropertyValue(fieldName, fieldValue);
    }
}
