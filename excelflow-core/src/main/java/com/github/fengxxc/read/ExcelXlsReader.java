package com.github.fengxxc.read;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class ExcelXlsReader extends ExcelReader {

    @Override
    public void proccessEnd() throws IOException, InvalidFormatException, ParserConfigurationException, SAXException {
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(super.getInputStream());
        ExcelFlowXlsHandler handler = new ExcelFlowXlsHandler(poifsFileSystem, this.sheet2CellTreeMap, this.pickerIdMap, this.beforePickCallback, this.pickCallback, this.sheetEndCallback);
        this.sheetEndCallback.accept(handler.getSheetName());
    }

}
