package com.github.fengxxc.write;

import com.github.fengxxc.DataWrapper;
import com.github.fengxxc.IExcelHandler;
import com.github.fengxxc.exception.ExcelFlowReflectionException;
import com.github.fengxxc.model.Offset;
import com.github.fengxxc.model.Point;
import com.github.fengxxc.model.PropVal;
import com.github.fengxxc.util.ExcelFlowUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.function.BiFunction;

/**
 * @author fengxxc
 */
public class ExcelWriter implements IExcelHandler<Recorder> {
    private OutputStream os;
    private Map<String, TreeSet<Recorder>> sheet2RecordersMap = new HashMap<>();
    private Map<Integer, ObjIterator> objIteratorMap;

    public ExcelWriter(OutputStream os) {
        this.os = os;
    }

    public ExcelWriter(OutputStream os, Map<Integer, ObjIterator> objIteratorMap) {
        this.os = os;
        this.objIteratorMap = objIteratorMap;
    }

    public ExcelWriter record(Recorder... recorders) throws ParserConfigurationException, InvalidFormatException, SAXException, IOException {
        accept(recorders);
        return this;
    }

    @Override
    public void accept(Recorder... recorders) throws ParserConfigurationException, InvalidFormatException, SAXException, IOException {
        for (int i = 0; i < recorders.length; i++) {
            Recorder recorder = recorders[i];
            if (recorder.getId() == -1) {
                recorder.setId(i);
            }

            // maybe it is read to write
            ObjIterator objIterator = this.objIteratorMap.get(recorder.getId());
            if (recorder.getSourceIterator() == null) {
                recorder.source(objIterator);
                // recorder.setObject(objIterator.getKind());
            }

            String sheetName = recorder.getSheet();
            if (sheetName == null) {
                sheetName = "Sheet1";
            }
            TreeSet<Recorder> recorderSet = sheet2RecordersMap.get(sheetName);
            if (recorderSet == null) {
                recorderSet = new TreeSet<Recorder>();
            }
            recorderSet.add(recorder);
            sheet2RecordersMap.put(sheetName, recorderSet);
        }
    }

    @Override
    public void proccessEnd() throws IOException, InvalidFormatException, ParserConfigurationException, SAXException {
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        // workbook.setCompressTempFiles(true);
        this.sheet2RecordersMap.forEach((sheetName, recorderSet) -> {
            proccessSheet(workbook, sheetName, recorderSet);
        });
        workbook.write(os);
        os.close();
        workbook.dispose();
    }

    private void proccessSheet(SXSSFWorkbook workbook, String sheetName, TreeSet<Recorder> recorderSet) {
        SXSSFSheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
        }
        final SXSSFSheet finalSheet = sheet;
        recorderSet.forEach(recorder -> {
            Iterator sourceIterator = recorder.getSourceIterator();
            Class clazz = recorder.getObject();
            TreeSet<PropMapper> mappers = (TreeSet<PropMapper>) recorder.getMappers();
            Map<PropMapper, String> mapper2CellRefMap = new HashMap<>();
            for (PropMapper mapper : mappers) {
            }

            if (sourceIterator == null) {
                proccessObject(finalSheet, mappers, mapper2CellRefMap, 0, null, null);
                return;
            }
            int iterationNumn = 0;
            BiFunction<String, Object, Offset> nextFunc = recorder.getNextFunc();
            while (sourceIterator.hasNext()) {
                Object data = sourceIterator.next();
                if (data == null) {
                    continue;
                }
                proccessObject(finalSheet, mappers, mapper2CellRefMap, iterationNumn, nextFunc, data);
                iterationNumn++;
            }
        });
    }

    private void proccessObject(SXSSFSheet finalSheet, TreeSet<PropMapper> propMappers, Map<PropMapper, String> mapper2CellRefMap, int iterationNumn, BiFunction<String, Object, Offset> nextFunc, Object data) {
        DataWrapper dataWrapper = null;
        if (data != null) {
            dataWrapper = new DataWrapper(data);
        }
        for (PropMapper propMapper : propMappers) {
            Object value = propMapper.getDefVal();
            if (dataWrapper != null) {
                value = dataWrapper.getPropertyValue(propMapper.getObjectProperty());
            }
            if (propMapper.val() != null) {
                value = propMapper.val().apply(value);
            }
            Class propertyType = propMapper.getObjectPropertyReturnType();
            // set in excel cell
            Point realPos;
            if (iterationNumn == 0) {
                realPos = propMapper.getPoint();
                mapper2CellRefMap.put(propMapper, realPos.toCellReferenceString());
            } else {
                String lastCellRef = mapper2CellRefMap.get(propMapper);
                PropVal propVal = PropVal.of(propMapper.getObjectProperty(), value, propMapper.getObjectPropertyReturnType());
                Offset offset = nextFunc.apply(lastCellRef, propVal);
                String cellRef = ExcelFlowUtils.computNextCellRef(lastCellRef, offset);
                realPos = Point.of(cellRef);
                mapper2CellRefMap.put(propMapper, cellRef);
            }
            SXSSFRow row = finalSheet.getRow(realPos.Y);
            if (row == null) {
                row = finalSheet.createRow(realPos.Y);
            }
            SXSSFCell cell = row.getCell(realPos.X);
            if (cell == null) {
                cell = row.createCell(realPos.X);
            }
            setCellValue(cell, value, propertyType);
        }
    }

    private static void setCellValue(SXSSFCell cell, Object value, Class type) {
        if (value == null) {
            return;
        }
        if (type == null) {
            type = value.getClass();
        }
        switch (type.getSimpleName()) {
            case "String":
                cell.setCellValue(value.toString());
                break;
            case "Integer":
                cell.setCellValue((Integer) value);
                break;
            case "Boolean":
                cell.setCellValue((Boolean) value);
                break;
            case "Character":
                cell.setCellValue((Character) value);
                break;
            case "Byte":
                cell.setCellValue((Byte) value);
                break;
            case "Short":
                cell.setCellValue((Short) value);
                break;
            case "Long":
                cell.setCellValue((Long) value);
                break;
            case "Float":
                cell.setCellValue((Float) value);
                break;
            case "Double":
                cell.setCellValue((Double) value);
                break;
            case "Object":
                cell.setCellValue(value.toString());
                break;
            default:
                throw new ExcelFlowReflectionException("unknow cell type: '" + type.getName() + "' , value is " + value.toString() + ", in " + Point.of(cell.getRowIndex(), cell.getColumnIndex()).toCellReferenceString());
        }
    }

}
