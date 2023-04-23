package com.github.fengxxc.read;

import com.github.fengxxc.ExcelHandler;
import com.github.fengxxc.exception.ExcelFlowConfigException;
import com.github.fengxxc.model.*;
import com.github.fengxxc.util.ExcelFlowUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.xml.sax.SAXException;

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
public abstract class ExcelReader extends ExcelHandler<ExcelReader> {
    protected Map<Integer, Picker> pickerIdMap = new HashMap<>();
    protected Consumer<EFCell> beforePickCallback;
    protected BiConsumer<Integer, Object> pickCallback;
    protected BiConsumer<String, String> mergeCellCallback;

    public abstract ExcelReader read(InputStream is) throws IOException, OpenXML4JException;

    public ExcelReader picks(Picker... pickers) throws ParserConfigurationException, InvalidFormatException, SAXException, IOException {
        for (int i = 0; i < pickers.length; i++) {
            final Picker picker = pickers[i];
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
            final Foward foward = picker.getFoward();
            Point endPoint = null;
            for (int j = 0; j < picker.getCellMappers().size(); j++) {
                CellMapper cellMapper = (CellMapper) picker.getCellMappers().get(j);
                cellMapper.setParentId(picker.getId());
                endPoint = ExcelFlowUtils.maxIn2D(cellMapper.getPoint(), endPoint);
                final Rect rect = ExcelFlowUtils.getMaxRect(foward, cellMapper.getPoint());
                final RTreeNode<CellMapper> rTreeNode = new RTreeNode<CellMapper>(rect).addEntry(cellMapper);
                final RTreeNode<CellMapper> gridRTreeNode = this.sheet2CellTreeMap.get(sheetName);
                if (gridRTreeNode == null) {
                    this.sheet2CellTreeMap.put(sheetName, rTreeNode);
                } else {
                    gridRTreeNode.add(rTreeNode);
                }
            }
            picker.setEndPoint(endPoint);
        }
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
}
