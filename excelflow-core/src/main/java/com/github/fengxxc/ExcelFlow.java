package com.github.fengxxc;

import com.github.fengxxc.read.ExcelReader;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author fengxxc
 * @date 2023-04-01
 */
public class ExcelFlow {

    public static ExcelReader read(InputStream is) throws IOException, OpenXML4JException {
        OPCPackage pkg = OPCPackage.open(is);
        XSSFReader reader = new XSSFReader(pkg);
        final ExcelReader excelReader = new ExcelReader(reader);
        return excelReader;
    }

    public static ExcelReader write(OutputStream os) {
        // TODO
        return null;
    }

}
