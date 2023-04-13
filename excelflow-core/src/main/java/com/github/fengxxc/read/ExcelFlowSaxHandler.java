package com.github.fengxxc.read;

import com.github.fengxxc.model.CellMapper;
import com.github.fengxxc.model.EFCell;
import com.github.fengxxc.model.Point;
import com.github.fengxxc.model.RTreeNode;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author fengxxc
 * @date 2023-04-02
 */
public class ExcelFlowSaxHandler<R> implements XSSFSheetXMLHandler.SheetContentsHandler {
    private String sheetName;
    private SharedStringsTable sst;
    private Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap;
    private BiConsumer<Integer, Object> cellCallback;

    public ExcelFlowSaxHandler(String sheetName, SharedStringsTable sst, Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap, BiConsumer<Integer, Object> cellCallback) {
        this.sheetName = sheetName;
        this.sst = sst;
        this.sheet2CellTreeMap = sheet2CellTreeMap;
        this.cellCallback = cellCallback;
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
        if (searchCellMappers == null) {
            return;
        }

        // System.out.println("searchGrids = " + Arrays.toString(searchCellMappers.toArray()));
        for (CellMapper cellMapper : searchCellMappers) {
            if (!cellMapper.getPipeline().getSheet().equals(this.sheetName)) {
                continue;
            }
            final EFCell efCell = new EFCell(cellReference, formattedValue, cellMapper);
            this.cellCallback.accept(cellMapper.getPipeline().getId(), efCell);
        }
    }
}
