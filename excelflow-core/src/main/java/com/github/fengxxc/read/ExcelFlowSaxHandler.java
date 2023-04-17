package com.github.fengxxc.read;

import com.github.fengxxc.exception.ExcelFlowReflectionException;
import com.github.fengxxc.model.*;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
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
 * @date 2023-04-02
 */
public class ExcelFlowSaxHandler<R> extends DefaultReadFlowHandler implements XSSFSheetXMLHandler.SheetContentsHandler {


    public ExcelFlowSaxHandler(String sheetName, SharedStringsTable sst, Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap, Consumer<EFCell> beforePickCallback, BiConsumer<Integer, Object> pickCallback) {
        super(sheetName, sst, sheet2CellTreeMap, beforePickCallback, pickCallback);
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
        cellFlow(cellReference, formattedValue);
    }

}
