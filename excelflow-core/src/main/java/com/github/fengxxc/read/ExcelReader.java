package com.github.fengxxc.read;

import com.github.fengxxc.model.*;
import com.github.fengxxc.model.Point;
import com.github.fengxxc.model.RTreeNode;
import com.github.fengxxc.model.Rect;
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
public abstract class ExcelReader {

    protected Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap = new HashMap<>();
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
            final String sheetName = picker.getSheet();
            /*if (sheetName == null || "".equals(sheetName)) {
                throw new ExcelPortConfigException("can not get sheetIndex or sheetName, bunch id: " + bunch.getId());
            }*/
            // int left = Integer.MAX_VALUE, right = 0, top = Integer.MAX_VALUE, bottom = 0;
            int length = 0;
            final Foward foward = picker.getFoward();
            Point endPoint = null;
            for (int j = 0; j < picker.getCellMappers().size(); j++) {
                CellMapper cellMapper = (CellMapper) picker.getCellMappers().get(j);
                cellMapper.setPicker(picker);
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
            picker.setEndPoint(endPoint);
        }
        return this;
    }

    public abstract void proccess() throws IOException, InvalidFormatException, ParserConfigurationException, SAXException;


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
