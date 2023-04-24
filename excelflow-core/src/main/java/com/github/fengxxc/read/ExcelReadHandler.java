package com.github.fengxxc.read;

import com.github.fengxxc.IExcelHandler;
import com.github.fengxxc.JustWe;
import com.github.fengxxc.exception.ExcelFlowConfigException;
import com.github.fengxxc.model.*;
import com.github.fengxxc.util.ExcelFlowUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fengxxc
 * @date 2023-04-21
 */
public abstract class ExcelReadHandler<T extends JustWe> implements IExcelHandler<T> {
    protected Map<Integer, T> pickerIdMap = new HashMap<>();
    protected Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap = new HashMap<>();

    @Override
    public void accept(T... justUs) throws ParserConfigurationException, InvalidFormatException, SAXException, IOException {
        for (int i = 0; i < justUs.length; i++) {
            T justWe = justUs[i];
            if (justWe.getId() == -1) {
                justWe.setId(i);
            }
            if (pickerIdMap.containsKey(justWe.getId())) {
                throw new ExcelFlowConfigException("the pick` id '" + justWe.getId() + "' must be unique.");
            }
            pickerIdMap.put(justWe.getId(), justWe);
            final String sheetName = justWe.getSheet();
            /*if (sheetName == null || "".equals(sheetName)) {
                throw new ExcelPortConfigException("can not get sheetIndex or sheetName, bunch id: " + bunch.getId());
            }*/
            int length = 0;
            final Foward foward = justWe.getFoward();
            Point endPoint = null;
            for (Object item : justWe.getMappers()) {
                CellMapper mapper = (CellMapper) item;
                mapper.setParentId(justWe.getId());
                endPoint = ExcelFlowUtils.maxIn2D(mapper.getPoint(), endPoint);
                final Rect rect = ExcelFlowUtils.getMaxRect(foward, mapper.getPoint());
                final RTreeNode<CellMapper> rTreeNode = new RTreeNode<CellMapper>(rect).addEntry(mapper);
                final RTreeNode<CellMapper> cellRTreeNode = this.sheet2CellTreeMap.get(sheetName);
                if (cellRTreeNode == null) {
                    this.sheet2CellTreeMap.put(sheetName, rTreeNode);
                } else {
                    cellRTreeNode.add(rTreeNode);
                }
            }
            justWe.setEndPoint(endPoint);
        }
    }

    @Override
    public abstract void proccess() throws IOException, InvalidFormatException, ParserConfigurationException, SAXException;
}
