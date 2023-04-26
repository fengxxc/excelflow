package com.github.fengxxc.read;

import com.github.fengxxc.exception.ExcelFlowReflectionException;
import com.github.fengxxc.model.*;
import com.github.fengxxc.util.ReflectUtils;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.TypeMismatchException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author fengxxc
 */
public abstract class DefaultReadFlowHandler {
    protected String sheetName;
    private SharedStringsTable sst;
    private Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap;
    private Map<Integer, Picker> pickerIdMap = new HashMap<>();
    private Consumer<EFCell> beforePickCallback;
    private final BiConsumer<Integer, Object> pickCallback;
    private Map<Picker, BeanWrapperImpl> pickObjCache = new HashMap<>();

    // when this cell value is null, set last value as this value? TODO get from config
    private boolean setLastValIfNull = false;

    public DefaultReadFlowHandler(String sheetName, SharedStringsTable sst, Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap, Map<Integer, Picker> pickerIdMap, Consumer<EFCell> beforePickCallback, BiConsumer<Integer, Object> pickCallback) {
        this.sheetName = sheetName;
        this.sst = sst;
        this.sheet2CellTreeMap = sheet2CellTreeMap;
        this.pickerIdMap = pickerIdMap;
        this.beforePickCallback = beforePickCallback;
        this.pickCallback = pickCallback;
    }

    public void cellFlow(String cellReference, String formattedValue) {
        final RTreeNode<CellMapper> cellRTreeNode = sheet2CellTreeMap.get(sheetName);
        if (cellRTreeNode == null) {
            return;
        }
        final List<CellMapper> searchCellMappers = cellRTreeNode.search(Point.of(cellReference));
        if (searchCellMappers == null || searchCellMappers.size() == 0) {
            return;
        }

        final EFCell efCell = new EFCell(cellReference, formattedValue, searchCellMappers);
        beforePickCallback.accept(efCell);

        for (CellMapper cellMapper : searchCellMappers) {
            int pickerId = cellMapper.getParentId();
            Picker picker = (Picker) this.pickerIdMap.get(pickerId);
            if (!picker.getSheet().equals(this.sheetName)) {
                continue;
            }
            BeanWrapperImpl beanWrapper = pickObjCache.get(picker);
            if (beanWrapper == null) {
                try {
                    beanWrapper = new BeanWrapperImpl(ReflectUtils.createInstance(picker.getObject()));
                } catch (InstantiationException| IllegalAccessException| InvocationTargetException | NoSuchMethodException e) {
                    throw new ExcelFlowReflectionException("can not create class '" + picker.getObject().getName() + "' instance.");
                    // e.printStackTrace();
                }
                pickObjCache.put(picker, beanWrapper);
            }

            /* beanWrapper not complete, make property */
            Object value = formattedValue;
            // convert property type
            Class propertyType = cellMapper.getObjectPropertyReturnType();
            value = ReflectUtils.convertValueByClassType(formattedValue, value, propertyType);

            if (cellMapper.val() != null) {
                // apply customer value function
                value = cellMapper.val().apply(value);
            }

            try {
                ReflectUtils.setFieldValue(beanWrapper, cellMapper.getObjectProperty(), value);
            } catch (TypeMismatchException e) {
                throw new ExcelFlowReflectionException("can not set property '" + cellMapper.getObjectProperty() + "' value '" + formattedValue + "', mismatch type.");
                // e.printStackTrace();
            }

            if (isArriveEndPoint(cellReference, picker)) {
                /* beanWrapper completed */
                Consumer pickerPickCallback = picker.getOnPickCallback();
                Object obj = beanWrapper.getRootInstance();
                if (pickerPickCallback != null) {
                    pickerPickCallback.accept(obj);
                }
                if (pickCallback != null) {
                    pickCallback.accept(picker.getId(), obj);
                }
                if (!setLastValIfNull) {
                    pickObjCache.replace(picker, null);
                }
            }

        }
    }

    private static boolean isArriveEndPoint(String cellReference, Picker picker) {
        boolean isFowardY = picker.getFoward() == Foward.Up || picker.getFoward() == Foward.Down;
        if (picker.getEndPoint().getAxis(isFowardY ? 'x' : 'y') != Point.of(cellReference).getAxis(isFowardY ? 'x' : 'y')) {
            return false;
        }
        int axisVal = Point.of(cellReference).getAxis(isFowardY ? 'y' : 'x');
        boolean arriveEndPoint = Math.abs(axisVal - picker.getEndPoint().getAxis(isFowardY ? 'y' : 'x')) % picker.getStepLength() == 0;
        return arriveEndPoint;
    }



}
