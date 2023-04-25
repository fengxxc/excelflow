package com.github.fengxxc;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author fengxxc
 * @date 2023-04-23
 */
public interface IExcelHandler<T> {
    void accept(T... justUs) throws ParserConfigurationException, InvalidFormatException, SAXException, IOException;
    void proccess() throws IOException, OpenXML4JException, ParserConfigurationException, SAXException;
}
