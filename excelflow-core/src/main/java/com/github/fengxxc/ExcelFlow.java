package com.github.fengxxc;

import com.github.fengxxc.read.ExcelReader;
import com.github.fengxxc.read.ExcelXlsReader;
import com.github.fengxxc.read.ExcelXlsxReader;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author fengxxc
 * @date 2023-04-01
 */
public class ExcelFlow {

    public static ExcelReader read(InputStream stream) throws IOException, OpenXML4JException {
        InputStream is = FileMagic.prepareToCheckMagic(stream);
        ExcelReader excelReader = null;
        FileMagic fm = FileMagic.valueOf(is);
        switch (fm) {
            case OLE2:
                excelReader = new ExcelXlsReader().read(is);
                return excelReader;
            case OOXML:
                excelReader = new ExcelXlsxReader().read(is);
                return excelReader;
            default:
                throw new IOException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
        }

    }

    /*public static ExcelWrite write(OutputStream os) {
        // TODO
        return null;
    }*/

}
