package com.github.fengxxc.read;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.Comments;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.function.BiConsumer;

/**
 * @author fengxxc
 * @date 2023-04-20
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
            mergeCellCallback.accept(split[0], split[1]);
        }
    }
}
