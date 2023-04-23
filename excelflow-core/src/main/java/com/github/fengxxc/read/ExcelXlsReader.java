package com.github.fengxxc.read;

import com.github.fengxxc.model.CellMapper;
import com.github.fengxxc.model.EFCell;
import com.github.fengxxc.model.Point;
import com.github.fengxxc.model.RTreeNode;
import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelXlsReader extends ExcelReader {
    private POIFSFileSystem poifsFileSystem;

    @Override
    public ExcelReader read(InputStream is) throws IOException, OpenXML4JException {
        this.poifsFileSystem = new POIFSFileSystem(is);

        return this;
    }

    @Override
    public void proccess() throws IOException, InvalidFormatException, ParserConfigurationException, SAXException {
        new ExcelFlowXlsHandler(this.poifsFileSystem, this.sheet2CellTreeMap, this.pickerIdMap, this.beforePickCallback, this.pickCallback);
    }



}
