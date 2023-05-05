package com.github.fengxxc;

import com.github.fengxxc.model.MaskCard;
import com.github.fengxxc.model.Offset;
import com.github.fengxxc.read.Picker;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author fengxxc
 */
public class ExcelFlow2Test {
    @Test
    public void readMaskCardsTest() throws IOException, OpenXML4JException, ParserConfigurationException, SAXException, ParseException {
        ArrayList<MaskCard> readResult = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (InputStream is = ExcelFlow.class.getResourceAsStream("/excel/maskCards.xlsx")) {
            ExcelFlow.read(is).picks(
                    Picker.of(MaskCard.class) .sheet("Sheet1")
                        .cellMap(cellMappers -> cellMappers
                                .cell("B2").prop(MaskCard::getName)
                                .cell("C3").prop(MaskCard::getNumber)
                                .cell("C4").prop(MaskCard::getCompany)
                                .cell("C5").prop(MaskCard::getJob)
                                .cell("C6").prop(MaskCard::getTel)
                                .cell("C7").prop(MaskCard::getTime).val(time -> {
                                    try {
                                        return dateFormat.parse((String) time);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                })
                        )
                        .next((cellReference, propVal) -> {
                            Offset offset = Offset.of();
                            if (    ((propVal.propEquals(MaskCard::getName)) && cellReference.startsWith("H"))
                                    || cellReference.startsWith("I")) {
                                offset.setX(-6).setY(8);
                            } else {
                                offset.setX(3).setY(0);
                            }
                            return offset;
                        })
                        .onPick(obj -> {
                            readResult.add(obj);
                        })
            ).proccessEnd();
        }

        for (int i = 0; i < readResult.size(); i++) {
            MaskCard maskCard = readResult.get(i);
            // System.out.println(maskCard);
            MaskCard expected = null;
            switch (i) {
                case 0:
                    expected = new MaskCard().setName("马一龙").setNumber(i+1).setCompany("Zip2").setJob("CTO").setTel("12100000001").setTime(dateFormat.parse("1995-01-01"));
                    break;
                case 1:
                    expected = new MaskCard().setName("马二龙").setNumber(i+1).setCompany("PayPal").setJob("CEO").setTel("12100000002").setTime(dateFormat.parse("1999-02-02"));
                    break;
                case 2:
                    expected = new MaskCard().setName("马三龙").setNumber(i+1).setCompany("SpaceX").setJob("CEO").setTel("12100000003").setTime(dateFormat.parse("2002-03-03"));
                    break;
                case 3:
                    expected = new MaskCard().setName("马四龙").setNumber(i+1).setCompany("Tesla").setJob("CEO").setTel("12100000004").setTime(dateFormat.parse("2004-04-04"));
                    break;
                case 4:
                    expected = new MaskCard().setName("马五龙").setNumber(i+1).setCompany("SolarCity").setJob("CEO").setTel("12100000005").setTime(dateFormat.parse("2008-10-01"));
                    break;
                case 5:
                    expected = new MaskCard().setName("马六龙").setNumber(i+1).setCompany("Hyperloop").setJob("CEO").setTel("12100000006").setTime(dateFormat.parse("2013-08-12"));
                    break;
                case 6:
                    expected = new MaskCard().setName("马七龙").setNumber(i+1).setCompany("The Boring Company").setJob("CEO").setTel("12100000007").setTime(dateFormat.parse("2018-12-01"));
                    break;
                case 7:
                    expected = new MaskCard().setName("马八龙").setNumber(i+1).setCompany("OpenAI").setJob("CEO").setTel("12100000008").setTime(dateFormat.parse("2015-12-01"));
                    break;
                case 8:
                    expected = new MaskCard().setName("马九龙").setNumber(i+1).setCompany("Neuralink").setJob("CEO").setTel("12100000009").setTime(dateFormat.parse("2017-03-01"));
                    break;
                case 9:
                    expected = new MaskCard().setName("马十龙").setNumber(i+1).setCompany("Twitter").setJob("CEO").setTel("12100000010").setTime(dateFormat.parse("2022-10-27"));
                    break;
            }
            Assert.assertEquals(expected, maskCard);
        }
    }
}
