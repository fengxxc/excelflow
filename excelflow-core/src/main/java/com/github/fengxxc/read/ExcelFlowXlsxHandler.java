package com.github.fengxxc.read;

import com.github.fengxxc.model.EFCell;
import com.github.fengxxc.model.RTreeNode;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.Comments;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author fengxxc
 */
public class ExcelFlowXlsxHandler extends XSSFSheetXMLHandler {
    private BiConsumer<String, String> mergeCellCallback;

    public ExcelFlowXlsxHandler(Styles styles, Comments comments, SharedStrings strings, SheetContentsHandler sheetContentsHandler, DataFormatter dataFormatter, boolean formulasNotResults) {
        super(styles, comments, strings, sheetContentsHandler, dataFormatter, formulasNotResults);
    }

    public ExcelFlowXlsxHandler(Styles styles, SharedStrings strings, SheetContentsHandler sheetContentsHandler, DataFormatter dataFormatter, boolean formulasNotResults) {
        super(styles, strings, sheetContentsHandler, dataFormatter, formulasNotResults);
    }

    public ExcelFlowXlsxHandler(Styles styles, SharedStrings strings, SheetContentsHandler sheetContentsHandler, boolean formulasNotResults) {
        super(styles, strings, sheetContentsHandler, formulasNotResults);
    }

    public BiConsumer<String, String> getMergeCellCallback() {
        return mergeCellCallback;
    }

    public ExcelFlowXlsxHandler setMergeCellCallback(BiConsumer<String, String> mergeCellCallback) {
        this.mergeCellCallback = mergeCellCallback;
        return this;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if ("mergeCell".equals(localName)) {
            String ref = attributes.getValue("ref");
            // System.out.println(ref);
            String[] split = ref.split(":");
            if (mergeCellCallback != null) {
                mergeCellCallback.accept(split[0], split[1]);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
    }

    /**
     * @author fengxxc
     */
    public static class SheetSaxHandler<R> extends DefaultReadFlowHandler implements SheetContentsHandler {
        private Consumer<String> sheetEndCallback;

        public SheetSaxHandler(
                String sheetName
                , SharedStringsTable sst, Map<String
                , RTreeNode<CellMapper>> sheet2CellTreeMap
                , Map<Integer, Picker> pickerIdMap
                , Consumer<EFCell> beforePickCallback
                , BiConsumer<Integer, Object> pickCallback
                , Consumer<String> sheetEndCallback
        ) {
            super(sheetName, sst, sheet2CellTreeMap, pickerIdMap, beforePickCallback, pickCallback);
            this.sheetEndCallback = sheetEndCallback;
        }

        @Override
        public void headerFooter(String text, boolean isHeader, String tagName) {}

        @Override
        public void endSheet() {
            if (this.sheetEndCallback != null) {
                this.sheetEndCallback.accept(super.sheetName);
            }
        }

        @Override
        public void startRow(int rowNum) {}

        @Override
        public void endRow(int rowNum) {}

        @Override
        public void cell(String cellReference, String formattedValue, XSSFComment comment) {
            cellFlow(cellReference, formattedValue);
        }

    }
}
