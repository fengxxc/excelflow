package com.github.fengxxc.util;

import com.github.fengxxc.ExcelFlow;
import com.github.fengxxc.model.Foward;
import com.github.fengxxc.model.NobelPrize;
import com.github.fengxxc.read.Picker;
import com.github.fengxxc.write.Recorder;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author fengxxc
 */
public class ExcelReadToWriteTest {
    @Test
    public void readToWrite() throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {

        // ArrayList<NobelPrize> readResult = new ArrayList<>();
        try(
                InputStream is = ExcelFlow.class.getResourceAsStream("/excel/test1.xlsx");
                // InputStream is = ExcelFlow.class.getResourceAsStream("/excel/test1.xls");
                OutputStream os = new FileOutputStream("F:\\temp\\excelflow\\export\\test1write.xlsx");
        ) {
            ExcelFlow.read(is).picks(
                    Picker.of(NobelPrize.class)
                            .sheet("Sheet1")
                            .cellMap(cellMappers -> cellMappers
                                    .cell("A2").prop(NobelPrize::getRanking).val(v -> ((int) v))
                                    .cell("B2").prop(NobelPrize::getUniversity).val(v -> "㊗" + v)
                                    .cell("C2").prop(NobelPrize::getCountry).val(country -> ((String) country).replaceAll("\u00a0", ""))
                                    .cell("D2").prop(NobelPrize::getTotal)
                                    .cell("E2").prop(NobelPrize::getNaturalScienceAwardTotal)
                                    .cell("F2").prop(NobelPrize::getPhysics)
                                    .cell("G2").prop(NobelPrize::getChemistry)
                                    .cell("H2").prop(NobelPrize::getPhysiologyOrMedicine)
                                    .cell("I2").prop(NobelPrize::getEconomy)
                                    .cell("J2").prop(NobelPrize::getLiterature)
                                    .cell("K2").prop(NobelPrize::getPeace)
                            )
                            .foward(Foward.Down)
                            .onPick(obj -> {
                                // readResult.add(obj);
                                // System.out.println(obj);
                            })
            ).onPick((pickerId, object) -> {
                // TODO
            })
            .proccessThenWrite(os).record(
                    Recorder.of(1)
                            .propMap(propMaps -> propMaps
                                    .cell("A2").val("国家")
                                    .cell("A3").val("大学")
                                    .cell("A4").val("排名")
                                    .cell("A5").val("自然科学奖总人数")
                                    .cell("A6").val("物理")
                                    .cell("A7").val("诺贝尔奖总人数")
                            ),
                    Recorder.of(0, NobelPrize.class)
                            .sheet("Sheet1")
                            .propMap(propMaps -> propMaps
                                    .cell("B2").prop(NobelPrize::getCountry).val(country -> country + "🏆")
                                    .cell("B3").prop(NobelPrize::getUniversity)
                                    .cell("B4").prop(NobelPrize::getRanking)
                                    .cell("B5").prop(NobelPrize::getNaturalScienceAwardTotal)
                                    .cell("B6").prop(NobelPrize::getPhysics)
                                    .cell("B7").prop(NobelPrize::getTotal)
                            )
                            .foward(Foward.Right)
            ).proccessEnd();

        }
    }
}
