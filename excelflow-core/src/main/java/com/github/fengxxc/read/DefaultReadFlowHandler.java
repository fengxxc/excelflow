package com.github.fengxxc.read;

import com.github.fengxxc.DataWrapper;
import com.github.fengxxc.exception.ExcelFlowReflectionException;
import com.github.fengxxc.model.*;
import com.github.fengxxc.util.ExcelFlowUtils;
import com.github.fengxxc.util.QueueHashMap;
import com.github.fengxxc.util.ReflectUtils;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.springframework.beans.TypeMismatchException;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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
    private PickObjCache pickObjCache = new PickObjCache();
    private QueueHashMap<String, List<CellMapper>> nextCellMappersQueue = new QueueHashMap<String, List<CellMapper>>();

    public DefaultReadFlowHandler(
            String sheetName
            , SharedStringsTable sst
            , Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap
            , Map<Integer, Picker> pickerIdMap
            , Consumer<EFCell> beforePickCallback
            , BiConsumer<Integer, Object> pickCallback
    ) {
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
        List<CellMapper> matchCellMappers = new ArrayList<>();
        List<CellMapper> sourceCellMappers = cellRTreeNode.search(Point.of(cellReference));
        if (sourceCellMappers != null) {
            for (CellMapper sourceCellMapper : sourceCellMappers) {
                sourceCellMapper.setObjectCacheId(0);
            }
            matchCellMappers.addAll(sourceCellMappers);
        }
        Map.Entry<String, List<CellMapper>> nextPeek = nextCellMappersQueue.peek();
        if (nextPeek != null) {
            String nextPeekCellRef = nextPeek.getKey();
            if (Point.of(cellReference).compareTo(Point.of(nextPeekCellRef)) > 0) {
                // proccess cell that jumped but pick
                for (CellMapper nextPeekMapper : nextPeek.getValue()) {
                    nextPeekMapper.setObjectCacheId(nextPeekMapper.getObjectCacheId() + 1);
                    makeNext(nextPeekCellRef, nextPeekMapper, null, this.pickerIdMap.get(nextPeekMapper.getParentId()).getNextFunc());
                }
                this.nextCellMappersQueue.remove(nextPeekCellRef);
            }
        }
        List<CellMapper> curCellMappers = nextCellMappersQueue.get(cellReference);
        if (curCellMappers != null) {
            for (CellMapper curCellMapper : curCellMappers) {
                curCellMapper.setObjectCacheId(curCellMapper.getObjectCacheId() + 1);
            }
            matchCellMappers.addAll(curCellMappers);
        }
        if (matchCellMappers == null || matchCellMappers.size() == 0) {
            return;
        }

        final EFCell efCell = new EFCell(cellReference, formattedValue, matchCellMappers);
        if (beforePickCallback != null) {
            beforePickCallback.accept(efCell);
        }

        for (CellMapper cellMapper : matchCellMappers) {
            int pickerId = cellMapper.getParentId();
            Picker picker = (Picker) this.pickerIdMap.get(pickerId);
            if (!picker.getSheet().equals(this.sheetName)) {
                continue;
            }

            Object value = formattedValue;
            // convert property type
            Class propertyType = cellMapper.getObjectPropertyReturnType();
            value = ReflectUtils.convertValueByClassType(formattedValue, value, propertyType);

            if (cellMapper.val() != null) {
                // apply customer value function
                value = cellMapper.val().apply(value);
            }

            DataWrapper beanWrapper = pickObjCache.get(cellMapper.getParentId(), cellMapper.getObjectCacheId());
            if (beanWrapper == null) {
                try {
                    beanWrapper = new DataWrapper(picker.getObject());
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new ExcelFlowReflectionException("can not create class '" + picker.getObject().getName() + "' instance.");
                }
                pickObjCache.put(cellMapper.getParentId(), cellMapper.getObjectCacheId(), beanWrapper);
            }

            try {
                beanWrapper.setPropertyValue(cellMapper.getObjectProperty(), value);
            } catch (TypeMismatchException e) {
                throw new ExcelFlowReflectionException("can not set property '" + cellMapper.getObjectProperty() + "' value '" + formattedValue + "', mismatch type, expected type is '" + cellMapper.getObjectPropertyReturnType() + "'.");
                // e.printStackTrace();
            }

            if (cellMapper.isEndOfParent()) {
                /* beanWrapper completed */
                Consumer pickerPickCallback = picker.getOnPickCallback();
                Object obj = beanWrapper.getRootInstance();
                if (pickerPickCallback != null) {
                    pickerPickCallback.accept(obj);
                }
                if (pickCallback != null) {
                    pickCallback.accept(picker.getId(), obj);
                }
                pickObjCache.remove(cellMapper.getParentId(), cellMapper.getObjectCacheId());
            }

            /* next... */
            BiFunction<String, PropVal, Offset> nextFunc = picker.getNextFunc();
            makeNext(cellReference, cellMapper, value, nextFunc);
        }

        this.nextCellMappersQueue.remove(cellReference);
    }

    private void makeNext(String cellReference, CellMapper cellMapper, Object value, BiFunction<String, PropVal, Offset> nextFunc) {
        if (nextFunc != null) {
            PropVal propVal = PropVal.of(cellMapper.getObjectProperty(), value, cellMapper.getObjectPropertyReturnType());
            Offset offset = nextFunc.apply(cellReference, propVal);
            String nextCellRef = ExcelFlowUtils.computNextCellRef(cellReference, offset);

            List<CellMapper> nextCellMappers = this.nextCellMappersQueue.get(nextCellRef);
            if (nextCellMappers == null) {
                nextCellMappers = new ArrayList<CellMapper>();
            }
            nextCellMappers.add(cellMapper);
            this.nextCellMappersQueue.offer(nextCellRef, nextCellMappers);
        }
    }

    public String getSheetName() {
        return sheetName;
    }
}
