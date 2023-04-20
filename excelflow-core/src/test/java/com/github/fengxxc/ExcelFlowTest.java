package com.github.fengxxc;

import com.github.fengxxc.model.Foward;
import com.github.fengxxc.model.Picker;
import com.github.fengxxc.model.NobelPrize;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

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
                                    .cell("A2").as(NobelPrize::getRanking).val(v -> ((int) v))
                                    .cell("B2").as(NobelPrize::getUniversity).val(v -> "㊗" + v)
                                    .cell("C2").as(NobelPrize::getCountry)
                                    .cell("D2").as(NobelPrize::getTotal)
                                    .cell("E2").as(NobelPrize::getNaturalScienceAwardTotal)
                                    .cell("F2").as(NobelPrize::getPhysics)
                                    .cell("G2").as(NobelPrize::getChemistry)
                                    .cell("H2").as(NobelPrize::getPhysiologyOrMedicine)
                                    .cell("I2").as(NobelPrize::getEconomy)
                                    .cell("J2").as(NobelPrize::getLiterature)
                                    .cell("K2").as(NobelPrize::getPeace)
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
                                    .cell("A2").as(NobelPrize::getRanking)
                                    .cell("B2").as(NobelPrize::getUniversity)
                                    .cell("C2").as(NobelPrize::getCountry)
                                    .cell("D2").as(NobelPrize::getTotal)
                                    .cell("E2").as(NobelPrize::getNaturalScienceAwardTotal)
                                    .cell("F2").as(NobelPrize::getPhysics)
                                    .cell("G2").as(NobelPrize::getChemistry)
                                    .cell("H2").as(NobelPrize::getPhysiologyOrMedicine)
                                    .cell("I2").as(NobelPrize::getEconomy)
                                    .cell("J2").as(NobelPrize::getLiterature)
                                    .cell("K2").as(NobelPrize::getPeace)
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
}