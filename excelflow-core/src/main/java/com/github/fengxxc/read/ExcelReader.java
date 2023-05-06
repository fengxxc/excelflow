package com.github.fengxxc.read;

import com.github.fengxxc.IExcelHandler;
import com.github.fengxxc.exception.ExcelFlowConfigException;
import com.github.fengxxc.model.*;
import com.github.fengxxc.write.ExcelWriter;
import com.github.fengxxc.write.ObjIterator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author fengxxc
 */
public abstract class ExcelReader implements IExcelHandler<Picker> {
    private InputStream is;
    protected Map<Integer, Picker> pickerIdMap = new HashMap<>();
    protected Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap = new HashMap<>();
    protected Consumer<EFCell> beforePickCallback;
    protected BiConsumer<Integer, Object> pickCallback;
    protected BiConsumer<String, String> mergeCellCallback;
    protected Consumer<String> sheetEndCallback;

    public ExcelReader read(InputStream is) throws IOException, OpenXML4JException {
        this.is = is;
        return this;
    }

    public InputStream getInputStream() {
        return is;
    }

    @Override
    public void accept(Picker... pickers) throws ParserConfigurationException, InvalidFormatException, SAXException, IOException {
        for (int i = 0; i < pickers.length; i++) {
            Picker picker = pickers[i];
            if (picker.getId() == -1) {
                picker.setId(i);
            }
            if (pickerIdMap.containsKey(picker.getId())) {
                throw new ExcelFlowConfigException("the pick` id '" + picker.getId() + "' must be unique.");
            }
            pickerIdMap.put(picker.getId(), picker);
            final String sheetName = picker.getSheet();
            /*if (sheetName == null || "".equals(sheetName)) {
                throw new ExcelPortConfigException("can not get sheetIndex or sheetName, bunch id: " + bunch.getId());
            }*/
            int length = 0;
            CellMapper endCellMapper = null;
            for (Object item : picker.getMappers()) {
                CellMapper mapper = (CellMapper) item;
                mapper.setParentId(picker.getId());
                if (endCellMapper == null || mapper.compareTo(endCellMapper) > 0) {
                    endCellMapper = mapper;
                }
                final Rect rect = Rect.of(mapper.getPoint(), mapper.getPoint());
                final RTreeNode<CellMapper> rTreeNode = new RTreeNode<CellMapper>(rect).addEntry(mapper);
                final RTreeNode<CellMapper> cellRTreeNode = this.sheet2CellTreeMap.get(sheetName);
                if (cellRTreeNode == null) {
                    this.sheet2CellTreeMap.put(sheetName, rTreeNode);
                } else {
                    cellRTreeNode.add(rTreeNode);
                }
            }
            endCellMapper.setEndOfParent(true);
        }
    }

    @Override
    public abstract void proccessEnd() throws IOException, OpenXML4JException, ParserConfigurationException, SAXException;

    public ExcelWriter proccessThenWrite(OutputStream os) throws OpenXML4JException, ParserConfigurationException, SAXException, IOException {
        Map<Integer, ObjIterator> objIteratorMap = new HashMap<>();
        pickerIdMap.forEach((pickId, picker) -> {
            objIteratorMap.put(pickId, ObjIterator.of(picker.getObject(), new EndSignal()));
        });
        BiConsumer<Integer, Object> pickFunc = (pickId, object) -> {
            ObjIterator objIterator = objIteratorMap.get(pickId);
            try {
                objIterator.put(object);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            objIteratorMap.put(pickId, objIterator);
        };
        Consumer<String> sheetEndFunc = (sheetName) -> {
            objIteratorMap.forEach((pickId, objIterator) -> {
                // send end signal
                try {
                    objIterator.putEndSignal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        };
        this.pickCallback = this.pickCallback == null ? pickFunc : this.pickCallback.andThen(pickFunc);
        this.sheetEndCallback = this.sheetEndCallback == null ? sheetEndFunc : this.sheetEndCallback.andThen(sheetEndFunc);
        new Thread(() -> {
            try {
                proccessEnd();
            } catch (IOException | OpenXML4JException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
        }).start();
        ExcelWriter excelWriter = new ExcelWriter(os, objIteratorMap);
        return excelWriter;
    }

    public ExcelReader picks(Picker... pickers) throws ParserConfigurationException, InvalidFormatException, SAXException, IOException {
        this.accept(pickers);
        return this;
    }

    public ExcelReader onBeforePick(Consumer<EFCell> cellCallback) {
        this.beforePickCallback = cellCallback;
        return this;
    }

    public ExcelReader onPick(BiConsumer<Integer, Object> pickCallback) {
        this.pickCallback = pickCallback;
        return this;
    }

    public ExcelReader onMergeCell(BiConsumer<String, String> mergeCellCallback) {
        this.mergeCellCallback = mergeCellCallback;
        return this;
    }

    /**
     * @author fengxxc
     */
    public static class EndSignal {}
}
