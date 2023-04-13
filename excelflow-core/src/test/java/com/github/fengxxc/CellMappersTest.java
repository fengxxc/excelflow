package com.github.fengxxc;

import com.github.fengxxc.model.CellMapper;
import com.github.fengxxc.model.NobelPrize;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author fengxxc
 * @date 2023-04-12
 */
public class CellMappersTest implements Serializable {
    private static final long serialVersionUID = 1L;

    @Test
    public void cell() {
    }

    @Test
    public void as() {
        final CellMappers<NobelPrize> mappers = new CellMappers<>();
        mappers
                .cell("A2").as(NobelPrize::getRanking)
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
                ;
        final List<CellMapper<NobelPrize>> mapperList = mappers.getMappers();
        System.out.println(Arrays.toString(mapperList.toArray()));

    }
}