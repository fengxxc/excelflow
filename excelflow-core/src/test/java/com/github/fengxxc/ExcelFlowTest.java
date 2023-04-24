package com.github.fengxxc;

import com.github.fengxxc.model.Foward;
import com.github.fengxxc.model.Picker;
import com.github.fengxxc.model.NobelPrize;
import com.github.fengxxc.write.Recorder;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Arrays;

/**
 * @author fengxxc
 * @date 2023-04-02
 */
public class ExcelFlowTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void readXlsx() throws IOException, ParserConfigurationException, OpenXML4JException, SAXException {
        try(InputStream is = ExcelFlow.class.getResourceAsStream("/excel/test1.xlsx")) {
            ExcelFlow.read(is).picks(
                    Picker.of(NobelPrize.class)
                            .sheet("Sheet1")
                            .cellMap(cellMappers -> cellMappers
                                    .cell("A2").prop(NobelPrize::getRanking).val(v -> ((int) v))
                                    .cell("B2").prop(NobelPrize::getUniversity).val(v -> "㊗" + v)
                                    .cell("C2").prop(NobelPrize::getCountry)
                                    .cell("D2").prop(NobelPrize::getTotal)
                                    .cell("E2").prop(NobelPrize::getNaturalScienceAwardTotal)
                                    .cell("F2").prop(NobelPrize::getPhysics)
                                    .cell("G2").prop(NobelPrize::getChemistry)
                                    .cell("H2").prop(NobelPrize::getPhysiologyOrMedicine)
                                    .cell("I2").prop(NobelPrize::getEconomy)
                                    .cell("J2").prop(NobelPrize::getLiterature)
                                    .cell("K2").prop(NobelPrize::getPeace)
                            )
                            .iterative(true)
                            .foward(Foward.Down)
                            .setStepLength(1)
                            .onPick(obj -> {
                                System.out.println(obj);
                            })
            ).onBeforePick(efCell -> {
                // TODO
            }).onPick((pickerId, object) -> {
                // TODO
            }).proccess();

        }
    }

    @Test
    public void readXls() throws IOException, ParserConfigurationException, OpenXML4JException, SAXException {
        // new ArrayList<Integer>().stream().map()
        try(InputStream is = ExcelFlow.class.getResourceAsStream("/excel/test1.xls")) {
            ExcelFlow.read(is).picks(
                    Picker.of(NobelPrize.class)
                            .sheet("Sheet1")
                            .cellMap(cellMappers -> cellMappers
                                    .cell("A2").prop(NobelPrize::getRanking)
                                    .cell("B2").prop(NobelPrize::getUniversity)
                                    .cell("C2").prop(NobelPrize::getCountry)
                                    .cell("D2").prop(NobelPrize::getTotal)
                                    .cell("E2").prop(NobelPrize::getNaturalScienceAwardTotal)
                                    .cell("F2").prop(NobelPrize::getPhysics)
                                    .cell("G2").prop(NobelPrize::getChemistry)
                                    .cell("H2").prop(NobelPrize::getPhysiologyOrMedicine)
                                    .cell("I2").prop(NobelPrize::getEconomy)
                                    .cell("J2").prop(NobelPrize::getLiterature)
                                    .cell("K2").prop(NobelPrize::getPeace)
                            )
                            .iterative(true)
                            .foward(Foward.Down)
                            .setStepLength(1)
            ).onBeforePick(efCell -> {
                System.out.println("onBeforePick");
                System.out.println(efCell.toString());
            }).onPick((pickerId, object) -> {
                System.out.println("onPick");
                // System.out.println(pickerId);
                System.out.println(object);
            }).proccess();

        }
    }

    @Test
    public void writeXlsx() throws IOException, InvalidFormatException, SAXException, ParserConfigurationException {
        NobelPrize[] nobelPrizes = {
                new NobelPrize().setCountry("美国").setUniversity("哈佛大学").setChemistry("2"),
                new NobelPrize().setCountry("英国").setUniversity("剑桥大学").setEconomy(3)
        };
        try (OutputStream os = new FileOutputStream("F:\\temp\\excelflow\\export\\test3.xlsx")) {
            ExcelFlow.write(os).record(
                    Recorder.of(null)
                            .propMap(propMaps -> propMaps
                                    .cell("A1").val("国家")
                                    .cell("B1").val("大学")
                                    .cell("C1").val("化学奖")
                                    .cell("D1").val("经济学奖")
                            ),
                    Recorder.of(NobelPrize.class)
                            .source(Arrays.stream(nobelPrizes).iterator())
                            .propMap(propMaps -> propMaps
                                    .cell("A2").prop(NobelPrize::getCountry)
                                    .cell("B2").prop(NobelPrize::getUniversity)
                                    .cell("C2").prop(NobelPrize::getChemistry)
                                    .cell("D2").prop(NobelPrize::getEconomy)
                            )
                            // .setStepLength(2)
            ).proccess();
        }
    }
}