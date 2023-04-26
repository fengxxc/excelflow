package com.github.fengxxc;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author fengxxc
 */
public interface IExcelHandler<T> {
    void accept(T... justUs) throws ParserConfigurationException, InvalidFormatException, SAXException, IOException;
    void proccessEnd() throws IOException, OpenXML4JException, ParserConfigurationException, SAXException;
}
