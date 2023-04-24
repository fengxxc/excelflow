package com.github.fengxxc.read;

import com.github.fengxxc.model.*;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author fengxxc
 * @date 2023-04-02
 */
public class ExcelFlowSaxHandler<R> extends DefaultReadFlowHandler implements XSSFSheetXMLHandler.SheetContentsHandler {


    public ExcelFlowSaxHandler(String sheetName, SharedStringsTable sst, Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap, Map<Integer, Picker> pickerIdMap, Consumer<EFCell> beforePickCallback, BiConsumer<Integer, Object> pickCallback) {
        super(sheetName, sst, sheet2CellTreeMap, pickerIdMap, beforePickCallback, pickCallback);
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
