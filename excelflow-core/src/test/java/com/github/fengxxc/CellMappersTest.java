package com.github.fengxxc;

import com.github.fengxxc.model.Point;
import com.github.fengxxc.read.CellMapper;
import com.github.fengxxc.read.CellMappers;
import com.github.fengxxc.model.NobelPrize;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author fengxxc
 */
public class CellMappersTest implements Serializable {
    private static final long serialVersionUID = 1L;

    @Test
    public void cell() {
    }

    @Test
    public void cellMappers() {
        final CellMappers<NobelPrize> mappers = new CellMappers<>();
        mappers
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
                ;
        final List<CellMapper<NobelPrize, ?>> mapperList = mappers.getMappers();
        System.out.println(Arrays.toString(mapperList.toArray()));
        Assert.assertEquals(11, mapperList.size());
        for (int i = 0; i < mapperList.size(); i++) {
            CellMapper<NobelPrize, ?> mapper = mapperList.get(i);
            switch (i) {
                case 0:
                    Assert.assertEquals(Point.of(1, 0), mapper.getPoint());
                    Assert.assertEquals("ranking", mapper.getObjectProperty());
                    Assert.assertEquals(Integer.class, mapper.getObjectPropertyReturnType());
                    break;
                case 1:
                    Assert.assertEquals(Point.of(1, 1), mapper.getPoint());
                    Assert.assertEquals("university", mapper.getObjectProperty());
                    Assert.assertEquals(String.class, mapper.getObjectPropertyReturnType());
                    break;
                case 2:
                    Assert.assertEquals(Point.of(1, 2), mapper.getPoint());
                    Assert.assertEquals("country", mapper.getObjectProperty());
                    Assert.assertEquals(String.class, mapper.getObjectPropertyReturnType());
                    break;
            }
        }
    }
}