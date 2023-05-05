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
import org.springframework.beans.BeanWrapperImpl;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * @author fengxxc
 */
public class ExcelFlowTest {

    static NobelPrize[] READ_EXPECTEDS = {
            new NobelPrize().setRanking(1).setUniversity("㊗哈佛大学").setCountry("美国").setTotal(161).setNaturalScienceAwardTotal(113).setPhysics(32).setChemistry("38").setPhysiologyOrMedicine(43).setEconomy(33).setLiterature(7).setPeace("8"),
            new NobelPrize().setRanking(2).setUniversity("㊗剑桥大学").setCountry("英国").setTotal(121).setNaturalScienceAwardTotal(98).setPhysics(37).setChemistry("30").setPhysiologyOrMedicine(31).setEconomy(15).setLiterature(5).setPeace("3"),
            new NobelPrize().setRanking(3).setUniversity("㊗伯克利加州大学").setCountry("美国").setTotal(110).setNaturalScienceAwardTotal(82).setPhysics(34).setChemistry("31").setPhysiologyOrMedicine(17).setEconomy(25).setLiterature(3).setPeace("1**"),
            new NobelPrize().setRanking(4).setUniversity("㊗芝加哥大学").setCountry("美国").setTotal(100).setNaturalScienceAwardTotal(62).setPhysics(32).setChemistry("19").setPhysiologyOrMedicine(11).setEconomy(33).setLiterature(3).setPeace("2"),
            new NobelPrize().setRanking(5).setUniversity("㊗哥伦比亚大学").setCountry("美国").setTotal(97).setNaturalScienceAwardTotal(70).setPhysics(33).setChemistry("15").setPhysiologyOrMedicine(22).setEconomy(15).setLiterature(6).setPeace("6"),
            new NobelPrize().setRanking(7).setUniversity("㊗麻省理工学院").setCountry("美国").setTotal(97).setNaturalScienceAwardTotal(62).setPhysics(34).setChemistry("16").setPhysiologyOrMedicine(12).setEconomy(34).setLiterature(0).setPeace("1"),
            new NobelPrize().setRanking(0).setUniversity("㊗史丹佛大学").setCountry("美国").setTotal(86).setNaturalScienceAwardTotal(55).setPhysics(26).setChemistry("13").setPhysiologyOrMedicine(16).setEconomy(28).setLiterature(3).setPeace("1**"),
            new NobelPrize().setRanking(8).setUniversity("㊗加利福尼亚理工学院").setCountry("美国").setTotal(76).setNaturalScienceAwardTotal(70).setPhysics(31).setChemistry("17").setPhysiologyOrMedicine(22).setEconomy(6).setLiterature(0).setPeace("1**"),
            new NobelPrize().setRanking(9).setUniversity("㊗牛津大学").setCountry("英国").setTotal(72).setNaturalScienceAwardTotal(53).setPhysics(15).setChemistry("19").setPhysiologyOrMedicine(19).setEconomy(9).setLiterature(5).setPeace("6**"),
            new NobelPrize().setRanking(10).setUniversity("㊗普林斯顿大学").setCountry("美国").setTotal(69).setNaturalScienceAwardTotal(42).setPhysics(29).setChemistry("9").setPhysiologyOrMedicine(4).setEconomy(21).setLiterature(5).setPeace("1"),
            new NobelPrize().setRanking(11).setUniversity("㊗耶鲁大学").setCountry("美国").setTotal(65).setNaturalScienceAwardTotal(34).setPhysics(8).setChemistry("12").setPhysiologyOrMedicine(14).setEconomy(23).setLiterature(5).setPeace("3"),
            new NobelPrize().setRanking(12).setUniversity("㊗康奈尔大学").setCountry("美国").setTotal(61).setNaturalScienceAwardTotal(50).setPhysics(23).setChemistry("12").setPhysiologyOrMedicine(15).setEconomy(5).setLiterature(4).setPeace("2"),
            new NobelPrize().setRanking(13).setUniversity("㊗柏林洪堡大学").setCountry("德国").setTotal(57).setNaturalScienceAwardTotal(49).setPhysics(14).setChemistry("23").setPhysiologyOrMedicine(12).setEconomy(1).setLiterature(4).setPeace("3"),
            new NobelPrize().setRanking(14).setUniversity("㊗巴黎大学").setCountry("法国").setTotal(51).setNaturalScienceAwardTotal(35).setPhysics(15).setChemistry("10***").setPhysiologyOrMedicine(10).setEconomy(4).setLiterature(6).setPeace("7"),
            new NobelPrize().setRanking(15).setUniversity("㊗哥廷根大学").setCountry("德国").setTotal(45).setNaturalScienceAwardTotal(43).setPhysics(19).setChemistry("16").setPhysiologyOrMedicine(8).setEconomy(0).setLiterature(1).setPeace("1"),
            new NobelPrize().setRanking(16).setUniversity("㊗慕尼黑大学").setCountry("德国").setTotal(43).setNaturalScienceAwardTotal(42).setPhysics(14).setChemistry("19").setPhysiologyOrMedicine(9).setEconomy(0).setLiterature(1).setPeace("1**"),
            new NobelPrize().setRanking(17).setUniversity("㊗约翰·霍普金斯大学").setCountry("美国").setTotal(39).setNaturalScienceAwardTotal(30).setPhysics(4).setChemistry("8").setPhysiologyOrMedicine(18).setEconomy(5).setLiterature(1).setPeace("3"),
            new NobelPrize().setRanking(19).setUniversity("㊗哥本哈根大学").setCountry("丹麦").setTotal(39).setNaturalScienceAwardTotal(34).setPhysics(19).setChemistry("7").setPhysiologyOrMedicine(8).setEconomy(3).setLiterature(2).setPeace("1**"),
            new NobelPrize().setRanking(0).setUniversity("㊗纽约大学").setCountry("美国").setTotal(38).setNaturalScienceAwardTotal(20).setPhysics(3).setChemistry("5").setPhysiologyOrMedicine(12).setEconomy(14).setLiterature(2).setPeace("2"),
            new NobelPrize().setRanking(0).setUniversity("㊗洛克菲勒大学").setCountry("美国").setTotal(38).setNaturalScienceAwardTotal(38).setPhysics(1).setChemistry("11").setPhysiologyOrMedicine(26).setEconomy(0).setLiterature(0).setPeace("0"),
            new NobelPrize().setRanking(21).setUniversity("㊗宾夕法尼亚大学").setCountry("美国").setTotal(36).setNaturalScienceAwardTotal(25).setPhysics(4).setChemistry("10").setPhysiologyOrMedicine(11).setEconomy(11).setLiterature(0).setPeace("0"),
            new NobelPrize().setRanking(22).setUniversity("㊗伦敦大学学院").setCountry("英国").setTotal(34).setNaturalScienceAwardTotal(31).setPhysics(5).setChemistry("7").setPhysiologyOrMedicine(19).setEconomy(2).setLiterature(1).setPeace("0"),
            new NobelPrize().setRanking(23).setUniversity("㊗苏黎世联邦理工学院").setCountry("瑞士").setTotal(32).setNaturalScienceAwardTotal(32).setPhysics(11).setChemistry("17").setPhysiologyOrMedicine(4).setEconomy(0).setLiterature(0).setPeace("0"),
            new NobelPrize().setRanking(24).setUniversity("㊗伊利诺伊大学香槟分校").setCountry("美国").setTotal(30).setNaturalScienceAwardTotal(27).setPhysics(11).setChemistry("5").setPhysiologyOrMedicine(11).setEconomy(3).setLiterature(0).setPeace("0"),
            new NobelPrize().setRanking(0).setUniversity("㊗明尼苏达大学").setCountry("美国").setTotal(30).setNaturalScienceAwardTotal(15).setPhysics(7).setChemistry("4").setPhysiologyOrMedicine(4).setEconomy(12).setLiterature(2).setPeace("1"),
            new NobelPrize().setRanking(26).setUniversity("㊗圣地亚哥加利福尼亚大学").setCountry("美国").setTotal(27).setNaturalScienceAwardTotal(24).setPhysics(5).setChemistry("9").setPhysiologyOrMedicine(10).setEconomy(3).setLiterature(0).setPeace("1**"),
            new NobelPrize().setRanking(0).setUniversity("㊗海德堡大学").setCountry("德国").setTotal(27).setNaturalScienceAwardTotal(24).setPhysics(11).setChemistry("8").setPhysiologyOrMedicine(5).setEconomy(0).setLiterature(1).setPeace("2"),
            new NobelPrize().setRanking(28).setUniversity("㊗密歇根大学").setCountry("美国").setTotal(26).setNaturalScienceAwardTotal(18).setPhysics(9).setChemistry("3").setPhysiologyOrMedicine(6).setEconomy(6).setLiterature(2).setPeace("0"),
            new NobelPrize().setRanking(0).setUniversity("㊗威斯康星大学麦迪逊分校").setCountry("美国").setTotal(26).setNaturalScienceAwardTotal(22).setPhysics(6).setChemistry("7").setPhysiologyOrMedicine(10).setEconomy(2).setLiterature(1).setPeace("0"),
            new NobelPrize().setRanking(30).setUniversity("㊗加州大学洛杉矶分校").setCountry("美国").setTotal(25).setNaturalScienceAwardTotal(13).setPhysics(2).setChemistry("8").setPhysiologyOrMedicine(3).setEconomy(9).setLiterature(1).setPeace("2"),
            new NobelPrize().setRanking(0).setUniversity("㊗曼彻斯特大学").setCountry("英国").setTotal(25).setNaturalScienceAwardTotal(22).setPhysics(11).setChemistry("9").setPhysiologyOrMedicine(2).setEconomy(3).setLiterature(0).setPeace("0"),
            new NobelPrize().setRanking(0).setUniversity("㊗圣路易斯华盛顿大学").setCountry("美国").setTotal(25).setNaturalScienceAwardTotal(24).setPhysics(1).setChemistry("5").setPhysiologyOrMedicine(18).setEconomy(1).setLiterature(0).setPeace("0"),
    };

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
            // System.out.println(obj);
            Assert.assertEquals(READ_EXPECTEDS[i], obj);
        }
    }

    @Test
    public void readXlsxToMap() throws IOException, ParserConfigurationException, OpenXML4JException, SAXException {
        ArrayList<Map> readResult = new ArrayList<>();

        try(InputStream is = ExcelFlow.class.getResourceAsStream("/excel/test1.xlsx")) {
            ExcelFlow.read(is).picks(
                    Picker.of(Map.class)
                            .sheet("Sheet1")
                            .cellMap(cellMappers -> cellMappers
                                    .cell("A2").prop("ranking")
                                    .cell("B2").prop("university").val(v -> "㊗" + v)
                                    .cell("C2").prop("country").val(country -> ((String) country).replaceAll("\u00a0", ""))
                                    .cell("D2").prop("total")
                                    .cell("E2").prop("naturalscienceawardtotal")
                                    .cell("F2").prop("physics")
                                    .cell("G2").prop("chemistry")
                                    .cell("H2").prop("physiologyormedicine")
                                    .cell("I2").prop("economy")
                                    .cell("J2").prop("literature")
                                    .cell("K2").prop("peace")
                            )
                            .foward(Foward.Down)
                            .onPick(obj -> {
                                readResult.add(obj);
                            })
            ).proccessEnd();

        }

        for (int i = 0; i < readResult.size(); i++) {
            Map obj = readResult.get(i);
            System.out.println(obj);
            switch (i) {
                case 0:
                    String expected0 = "{chemistry=38, country=美国, literature=7, total=161, university=㊗哈佛大学, physics=32, peace=8, naturalscienceawardtotal=113, physiologyormedicine=43, ranking=1, economy=33}";
                    Assert.assertEquals(expected0, obj.toString());
                    break;
                case 1:
                    String expected1 = "{chemistry=30, country=英国, literature=5, total=121, university=㊗剑桥大学, physics=37, peace=3, naturalscienceawardtotal=98, physiologyormedicine=31, ranking=2, economy=15}";
                    Assert.assertEquals(expected1, obj.toString());
                    break;
            }
        }
    }

    @Test
    public void readXls() throws IOException, ParserConfigurationException, OpenXML4JException, SAXException {
        ArrayList<NobelPrize> readResult = new ArrayList<>();
        try(InputStream is = ExcelFlow.class.getResourceAsStream("/excel/test1.xls")) {
            ExcelFlow.read(is).picks(
                    Picker.of(NobelPrize.class)
                            .sheet("Sheet1")
                            .cellMap(cellMappers -> cellMappers
                                    .cell("A2").prop(NobelPrize::getRanking)
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
                                readResult.add(obj);
                            })
            ).onBeforePick(efCell -> {
                // System.out.println("onBeforePick");
                // System.out.println(efCell.toString());
            }).onPick((pickerId, object) -> {
                // System.out.println("onPick");
                // System.out.println(object);
            }).proccessEnd();

        }
        for (int i = 0; i < readResult.size(); i++) {
            NobelPrize obj = readResult.get(i);
            // System.out.println(obj);
            Assert.assertEquals(READ_EXPECTEDS[i], obj);
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
                            .foward(Foward.Right)
                            // .setStepLength(2)
            ).proccessEnd();
        }
    }

    @Test
    public void writeXlsxFromMap() throws IOException, InvalidFormatException, SAXException, ParserConfigurationException {
        Map[] nobelPrizes = {
                new HashMap() {{ put("country", "美国"); put("university", "哈佛大学"); put("ranking", 1); put("total", 161); }},
                new HashMap() {{ put("country", "英国"); put("university", "剑桥大学"); put("ranking", 2); put("total", 121); }},
                new HashMap() {{ put("country", "美国"); put("university", "伯克利加州大学"); put("ranking", 3); put("total", 110); }},
                new HashMap() {{ put("country", "美国"); put("university", "芝加哥大学"); put("ranking", 4); put("total", 100); }}
        };
        try (OutputStream os = new FileOutputStream("F:\\temp\\excelflow\\export\\test4.xlsx")) {
            ExcelFlow.write(os).record(
                    Recorder.of()
                            .propMap(propMaps -> propMaps
                                    .cell("A2").val("国家")
                                    .cell("A3").val("大学")
                                    .cell("A4").val("排名")
                                    .cell("A5").val("诺贝尔奖总人数")
                            ),
                    Recorder.of(Map.class)
                            .source(Arrays.stream(nobelPrizes).iterator())
                            .propMap(propMaps -> propMaps
                                    .cell("B2").prop("country").val(country -> country + "🏆")
                                    .cell("B3").prop("university")
                                    .cell("B4").prop("ranking")
                                    .cell("B5").prop("total")
                            )
                            .foward(Foward.Right)
                    // .setStepLength(2)
            ).proccessEnd();
        }
    }
}