package com.github.fengxxc;

import com.github.fengxxc.read.ExcelReader;
import com.github.fengxxc.read.ExcelXlsReader;
import com.github.fengxxc.read.ExcelXlsxReader;
import com.github.fengxxc.write.ExcelWriter;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.FileMagic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author fengxxc
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

    public static ExcelWriter write(OutputStream os) {
        ExcelWriter excelWriter = new ExcelWriter(os);
        return excelWriter;
    }

}
