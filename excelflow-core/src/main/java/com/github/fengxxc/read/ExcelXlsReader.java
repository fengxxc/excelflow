package com.github.fengxxc.read;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class ExcelXlsReader extends ExcelReader {
    private POIFSFileSystem poifsFileSystem;

    @Override
    public ExcelReader read(InputStream is) throws IOException, OpenXML4JException {
        this.poifsFileSystem = new POIFSFileSystem(is);

        return this;
    }

    @Override
    public void proccessEnd() throws IOException, InvalidFormatException, ParserConfigurationException, SAXException {
        new ExcelFlowXlsHandler(this.poifsFileSystem, this.sheet2CellTreeMap, this.pickerIdMap, this.beforePickCallback, this.pickCallback);
    }



}
