package com.github.fengxxc.read;

import com.github.fengxxc.model.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author fengxxc
 * @date 2023-04-01
 */
public abstract class ExcelReader extends ExcelReadHandler<Picker> {
    protected Consumer<EFCell> beforePickCallback;
    protected BiConsumer<Integer, Object> pickCallback;
    protected BiConsumer<String, String> mergeCellCallback;

    public abstract ExcelReader read(InputStream is) throws IOException, OpenXML4JException;

    public ExcelReader picks(Picker... pickers) throws ParserConfigurationException, InvalidFormatException, SAXException, IOException {
        super.accept(pickers);
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
