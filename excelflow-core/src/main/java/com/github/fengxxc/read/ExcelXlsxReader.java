package com.github.fengxxc.read;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class ExcelXlsxReader extends ExcelReader {
    private XSSFReader reader;

    @Override
    public ExcelReader read(InputStream is) throws IOException, OpenXML4JException {
        OPCPackage pkg = OPCPackage.open(is);
        this.reader = new XSSFReader(pkg);
        return this;
    }

    @Override
    public void proccess() throws IOException, InvalidFormatException, ParserConfigurationException, SAXException {
        SharedStringsTable sst = reader.getSharedStringsTable();
        final StylesTable stylesTable = reader.getStylesTable();
        XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) reader.getSheetsData();
        while (sheets.hasNext()) {
            InputStream sheet = sheets.next();
            // sheets.getSheetComments()
            // System.out.println("Sheet name: " + sheets.getSheetName());
            processSheet(sheets.getSheetName(), sheet, sst, stylesTable);
            sheet.close();
        }
    }

    private void processSheet(String sheetName, InputStream sheet, SharedStringsTable sst, StylesTable stylesTable) throws ParserConfigurationException, SAXException, IOException {
        ExcelFlowSaxHandler handler = new ExcelFlowSaxHandler(sheetName, sst, this.sheet2CellTreeMap, beforePickCallback, pickCallback);
        final DataFormatter dataFormatter = new DataFormatter();
        // ComXSSFReader reader = new ComXSSFReader(sheet, handler);
        XMLReader parser = XMLHelper.newXMLReader();
        parser.setContentHandler(new ExcelFlowXlsxHandler(stylesTable, null, sst, handler, dataFormatter, false));

        parser.parse(new InputSource(sheet));
    }
}
