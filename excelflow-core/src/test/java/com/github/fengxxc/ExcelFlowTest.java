package com.github.fengxxc;

import com.github.fengxxc.model.Foward;
import com.github.fengxxc.read.Picker;
import com.github.fengxxc.model.NobelPrize;
import com.github.fengxxc.write.Recorder;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
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
        ArrayList<NobelPrize> readResult = new ArrayList<>();

        try(InputStream is = ExcelFlow.class.getResourceAsStream("/excel/test1.xlsx")) {
            ExcelFlow.read(is).picks(
                    Picker.of(NobelPrize.class)
                            .sheet("Sheet1")
                            .cellMap(cellMappers -> cellMappers
                                    .cell("A2").prop(NobelPrize::getRanking).val(v -> ((int) v))
                                    .cell("B2").prop(NobelPrize::getUniversity).val(v -> "㊗" + v)
                                    .cell("C2").prop(NobelPrize::getCountry).val(country -> country.replaceAll("\u00a0", ""))
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
                                // System.out.println(obj);
                                readResult.add(obj);
                            })
            ).onBeforePick(efCell -> {
                // TODO
            }).onPick((pickerId, object) -> {
                // TODO
            }).proccessEnd();

        }

        for (int i = 0; i < readResult.size(); i++) {
            NobelPrize obj = readResult.get(i);
            switch (i) {
                case 0:
                    NobelPrize expected = new NobelPrize().setRanking(1).setUniversity("㊗哈佛大学").setCountry("美国").setTotal(161)
                            .setNaturalScienceAwardTotal(113).setPhysics(32).setChemistry("38").setPhysiologyOrMedicine(43)
                            .setEconomy(33).setLiterature(7).setPeace("8");
                    Assert.assertEquals(expected, obj);
            }
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
            ).onBeforePick(efCell -> {
                System.out.println("onBeforePick");
                System.out.println(efCell.toString());
            }).onPick((pickerId, object) -> {
                System.out.println("onPick");
                // System.out.println(pickerId);
                System.out.println(object);
            }).proccessEnd();

        }
    }

    @Test
    public void writeXlsx() throws IOException, InvalidFormatException, SAXException, ParserConfigurationException {
        NobelPrize[] nobelPrizes = {
                new NobelPrize().setCountry("美国").setUniversity("哈佛大学").setRanking(1).setTotal(161),
                new NobelPrize().setCountry("英国").setUniversity("剑桥大学").setRanking(2).setTotal(121),
                new NobelPrize().setCountry("美国").setUniversity("伯克利加州大学").setRanking(3).setTotal(110),
                new NobelPrize().setCountry("美国").setUniversity("芝加哥大学").setRanking(4).setTotal(100),
        };
        try (OutputStream os = new FileOutputStream("F:\\temp\\excelflow\\export\\test3.xlsx")) {
            ExcelFlow.write(os).record(
                    Recorder.of()
                            .propMap(propMaps -> propMaps
                                    .cell("A2").val("国家")
                                    .cell("A3").val("大学")
                                    .cell("A4").val("排名")
                                    .cell("A5").val("诺贝尔奖总人数")
                            ),
                    Recorder.of(NobelPrize.class)
                            .source(Arrays.stream(nobelPrizes).iterator())
                            .propMap(propMaps -> propMaps
                                    .cell("B2").prop(NobelPrize::getCountry).val(country -> country + "🏆")
                                    .cell("B3").prop(NobelPrize::getUniversity)
                                    .cell("B4").prop(NobelPrize::getRanking)
                                    .cell("B5").prop(NobelPrize::getTotal)
                            )
                            .iterative(true)
                            .foward(Foward.Right)
                            // .setStepLength(2)
            ).proccessEnd();
        }
    }
}