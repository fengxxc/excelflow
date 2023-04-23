package com.github.fengxxc;

import com.github.fengxxc.model.*;
import com.github.fengxxc.read.ExcelReader;
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
public abstract class ExcelHandler<T> {
    protected Map<String, RTreeNode<CellMapper>> sheet2CellTreeMap = new HashMap<>();

    public abstract void proccess() throws IOException, InvalidFormatException, ParserConfigurationException, SAXException;
}
