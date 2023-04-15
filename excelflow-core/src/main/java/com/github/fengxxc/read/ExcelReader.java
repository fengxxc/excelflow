package com.github.fengxxc.read;

import com.github.fengxxc.model.*;
import com.github.fengxxc.model.Point;
import com.github.fengxxc.model.RTreeNode;
import com.github.fengxxc.model.Rect;
import com.github.fengxxc.util.ExcelFlowUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author fengxxc
 * @date 2023-04-01
 */
public class ExcelReader {
    private XSSFReader reader;
    private Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap = new HashMap<>();
    private Consumer<EFCell> cellCallback;
    private BiConsumer<Integer, Object> partCallback;

    public ExcelReader(XSSFReader reader) {
        this.reader = reader;
    }

    public ExcelReader focuses(Part... parts) throws ParserConfigurationException, InvalidFormatException, SAXException, IOException {
        for (int i = 0; i < parts.length; i++) {
            final Part part = parts[i];
            if (part.getId() == -1) {
                part.setId(i);
            }
            final String sheetName = part.getSheet();
            /*if (sheetName == null || "".equals(sheetName)) {
                throw new ExcelPortConfigException("can not get sheetIndex or sheetName, bunch id: " + bunch.getId());
            }*/
            // int left = Integer.MAX_VALUE, right = 0, top = Integer.MAX_VALUE, bottom = 0;
            int length = 0;
            final Foward foward = part.getFoward();
            Point endPoint = null;
            for (int j = 0; j < part.getCellMappers().size(); j++) {
                CellMapper cellMapper = (CellMapper) part.getCellMappers().get(j);
                cellMapper.setPart(part);
                endPoint = ExcelFlowUtils.maxIn2D(cellMapper.getPoint(), endPoint);
                int top = foward == Foward.Up ? 0 : cellMapper.getPoint().Y;
                int right = foward == Foward.Right ? Integer.MAX_VALUE : cellMapper.getPoint().X;
                int bottom = foward == Foward.Down ? Integer.MAX_VALUE : cellMapper.getPoint().Y;
                int left = foward == Foward.Left ? 0 : cellMapper.getPoint().X;
                final Rect rect = Rect.of(Point.of(top, right), Point.of(bottom, left));
                final RTreeNode<CellMapper> rTreeNode = new RTreeNode<CellMapper>(rect).addEntry(cellMapper);
                final RTreeNode<CellMapper> gridRTreeNode = this.sheet2CellTreeMap.get(sheetName);
                if (gridRTreeNode == null) {
                    this.sheet2CellTreeMap.put(sheetName, rTreeNode);
                } else {
                    gridRTreeNode.add(rTreeNode);
                }
            }
            part.setEndPoint(endPoint);
        }
        return this;
    }

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
        ExcelFlowSaxHandler handler = new ExcelFlowSaxHandler(sheetName, sst, sheet2CellTreeMap, cellCallback, partCallback);
        final DataFormatter dataFormatter = new DataFormatter();
        // ComXSSFReader reader = new ComXSSFReader(sheet, handler);
        XMLReader parser = XMLHelper.newXMLReader();
        parser.setContentHandler(new XSSFSheetXMLHandler(stylesTable, null, sst, handler, dataFormatter, false));

        parser.parse(new InputSource(sheet));
    }

    public ExcelReader onCell(Consumer<EFCell> cellCallback) {
        this.cellCallback = cellCallback;
        return this;
    }

    public ExcelReader onPart(BiConsumer<Integer, Object> partCallback) {
        this.partCallback = partCallback;
        return this;
    }
}
