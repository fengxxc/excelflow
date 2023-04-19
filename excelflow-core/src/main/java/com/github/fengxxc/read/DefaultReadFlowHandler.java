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
 * @date 2023-04-17
 */
public abstract class DefaultReadFlowHandler {
    protected String sheetName;
    private SharedStringsTable sst;
    private Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap;
    private Consumer<EFCell> beforePickCallback;
    private final BiConsumer<Integer, Object> pickCallback;
    private Map<Picker, BeanWrapperImpl> pickObjCache = new HashMap<>();

    // when this cell value is null, set last value as this value? TODO get from config
    private boolean setLastValIfNull = false;

    public DefaultReadFlowHandler(String sheetName, SharedStringsTable sst, Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap, Consumer<EFCell> beforePickCallback, BiConsumer<Integer, Object> pickCallback) {
        this.sheetName = sheetName;
        this.sst = sst;
        this.sheet2CellTreeMap = sheet2CellTreeMap;
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
            Picker picker = cellMapper.getPicker();
            if (!picker.getSheet().equals(this.sheetName)) {
                continue;
            }
            BeanWrapperImpl beanWrapper = pickObjCache.get(picker);
            if (beanWrapper == null) {
                try {
                    beanWrapper = new BeanWrapperImpl(createInstance(picker.getObject()));
                } catch (InstantiationException| IllegalAccessException| InvocationTargetException | NoSuchMethodException e) {
                    throw new ExcelFlowReflectionException("can not create class '" + picker.getObject().getName() + "' instance.");
                    // e.printStackTrace();
                }
                pickObjCache.put(picker, beanWrapper);
            }

            /* beanWrapper not complete, make property */
            Object val = formattedValue;
            // convert property type
            Class propertyType = cellMapper.getObjectPropertyType();
            val = ReflectUtils.convertValueByClassType(formattedValue, val, propertyType);

            if (cellMapper.val() != null) {
                // apply customer val function
                val = cellMapper.val().apply(val);
            }

            try {
                setFieldValue(beanWrapper, cellMapper.getObjectProperty(), val);
            } catch (TypeMismatchException e) {
                throw new ExcelFlowReflectionException("can not set property '" + cellMapper.getObjectProperty() + "' value '" + formattedValue + "', mismatch type.");
                // e.printStackTrace();
            }

            if (isArriveEndPoint(cellReference, picker)) {
                /* beanWrapper completed */
                Consumer pickerPickCallback = cellMapper.getPicker().getOnPickCallback();
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
