package com.github.fengxxc;

import com.github.fengxxc.model.CellMapper;
import com.github.fengxxc.model.NobelPrize;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

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

    }
}