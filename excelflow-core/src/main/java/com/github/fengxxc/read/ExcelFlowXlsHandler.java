package com.github.fengxxc.read;

import com.github.fengxxc.model.*;
import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ExcelFlowXlsHandler extends DefaultReadFlowHandler implements HSSFListener {
    private static String NULL_STRING_VAL = "";
    private static String NULL_NUMBER_VAL = "0";

    // private POIFSFileSystem poifsFileSystem;
    private FormatTrackingHSSFListener formatListener;
    // TODO get from config
    private boolean outputFormulaValues = true;
    private EventWorkbookBuilder.SheetRecordCollectingListener workbookBuildingListener;
    private List<BoundSheetRecord> boundSheetRecords = new ArrayList<>();
    private HSSFWorkbook stubWorkbook;
    private int sheetIndex = -1;
    private BoundSheetRecord[] orderedBSRs;
    // private String sheetName;
    private SSTRecord sstRecord;
    private boolean outputNextStringRecord;
    private int formulaRow;
    private short formulaColumn;

    public ExcelFlowXlsHandler(POIFSFileSystem poifsFileSystem, Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap, Map<Integer, Picker> pickerIdMap, Consumer<EFCell> beforePickCallback, BiConsumer<Integer, Object> pickCallback) throws IOException {
        super(null, null, sheet2CellTreeMap, pickerIdMap, beforePickCallback, pickCallback);

        MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
        formatListener = new FormatTrackingHSSFListener(listener);
        HSSFEventFactory factory = new HSSFEventFactory();
        HSSFRequest request = new HSSFRequest();
        if (outputFormulaValues) {
            request.addListenerForAllRecords(formatListener);
        } else {
            workbookBuildingListener = new EventWorkbookBuilder.SheetRecordCollectingListener(formatListener);
            request.addListenerForAllRecords(workbookBuildingListener);
        }
        factory.processWorkbookEvents(request, poifsFileSystem);
    }

    @Override
    public void processRecord(Record record) {
        switch (record.getSid()) {
            case BoundSheetRecord.sid:
                boundSheetRecords.add((BoundSheetRecord) record);
                break;
            case BOFRecord.sid:
                BOFRecord br = (BOFRecord) record;
                if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
                    if (workbookBuildingListener != null && stubWorkbook == null) {
                        stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
                    }
                    sheetIndex++;
                    if (orderedBSRs == null) {
                        orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
                    }
                    sheetName = orderedBSRs[sheetIndex].getSheetname();
                }
                break;
            case SSTRecord.sid:
                sstRecord = (SSTRecord) record;
                break;
            case BlankRecord.sid:
                BlankRecord brec = (BlankRecord) record;
                cellHandler(sheetName, brec.getRow(), brec.getColumn(), NULL_STRING_VAL);
                break;
            case BoolErrRecord.sid:
                BoolErrRecord berec = (BoolErrRecord) record;
                cellHandler(sheetName, berec.getRow(), berec.getColumn(), berec.getBooleanValue() + "");
                break;
            case FormulaRecord.sid:
                FormulaRecord frec = (FormulaRecord) record;
                String value = null;
                if (outputFormulaValues) {
                    if (Double.isNaN(frec.getValue())) {
                        outputNextStringRecord = true;
                        formulaRow = frec.getRow();
                        formulaColumn = frec.getColumn();
                    } else {
                        value = formatListener.formatNumberDateCell(frec);
                    }
                } else {
                    value = '"' + HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression()) + '"';
                }
                cellHandler(sheetName, frec.getRow(), frec.getColumn(), value);
                break;
            case StringRecord.sid:
                if (outputNextStringRecord) {
                    StringRecord srec = (StringRecord) record;
                    outputNextStringRecord = false;
                }
                break;
            case LabelRecord.sid:
                LabelRecord lrec = (LabelRecord) record;
                cellHandler(sheetName, lrec.getRow(), lrec.getColumn(), "".equals(lrec.getValue().trim()) ? NULL_STRING_VAL : lrec.getValue().trim());
                break;
            case LabelSSTRecord.sid:
                LabelSSTRecord lsrec = (LabelSSTRecord) record;
                if (sstRecord == null) {
                    cellHandler(sheetName, lsrec.getRow(), lsrec.getColumn(), NULL_STRING_VAL);
                } else {
                    cellHandler(sheetName, lsrec.getRow(), lsrec.getColumn(), "".equals(sstRecord.getString(lsrec.getSSTIndex()).toString().trim()) ? NULL_STRING_VAL : sstRecord.getString(lsrec.getSSTIndex()).toString().trim());
                }
                break;
            case NumberRecord.sid:
                NumberRecord numrec = (NumberRecord) record;
                cellHandler(sheetName, numrec.getRow(), numrec.getColumn(), "".equals(formatListener.formatNumberDateCell(numrec).trim()) ? NULL_NUMBER_VAL : formatListener.formatNumberDateCell(numrec).trim());
                break;
            default:
                break;
        }

        if (record instanceof MissingCellDummyRecord) {
            // MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
            // thisRow = mc.getRow();
            // thisColumn = mc.getColumn();
            // cellHandler(sheetName, thisRow, thisColumn, NULL_VAL);
        }

        if (record instanceof LastCellOfRowDummyRecord) {
            // is last cell of this row
        }
    }

    private void cellHandler(String sheetName, int row, int col, String formattedValue) {
        Point point = Point.of(row, col);
        cellFlow(point.toCellReference().formatAsString(), formattedValue);
    }

}
