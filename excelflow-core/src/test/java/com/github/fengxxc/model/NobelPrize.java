package com.github.fengxxc.model;

import java.io.Serializable;

/**
 * @author fengxxc
 * @date 2023-04-12
 */
public class NobelPrize implements Serializable {
    private static final long serialVersionUID = 1L;

    private int ranking;
    private String university;
    private String country;
    private int total;
    private int naturalScienceAwardTotal;
    private int physics;
    private int chemistry;
    private int physiologyOrMedicine;
    private int economy;
    private int literature;
    private int peace;


    public int getRanking() {
        return ranking;
    }

    public NobelPrize setRanking(int ranking) {
        this.ranking = ranking;
        return this;
    }

    public String getUniversity() {
        return university;
    }

    public NobelPrize setUniversity(String university) {
        this.university = university;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public NobelPrize setCountry(String country) {
        this.country = country;
        return this;
    }

    public int getTotal() {
        return total;
    }

    public NobelPrize setTotal(int total) {
        this.total = total;
        return this;
    }

    public int getNaturalScienceAwardTotal() {
        return naturalScienceAwardTotal;
    }

    public NobelPrize setNaturalScienceAwardTotal(int naturalScienceAwardTotal) {
        this.naturalScienceAwardTotal = naturalScienceAwardTotal;
        return this;
    }

    public int getPhysics() {
        return physics;
    }

    public NobelPrize setPhysics(int physics) {
        this.physics = physics;
        return this;
    }

    public int getChemistry() {
        return chemistry;
    }

    public NobelPrize setChemistry(int chemistry) {
        this.chemistry = chemistry;
        return this;
    }

    public int getPhysiologyOrMedicine() {
        return physiologyOrMedicine;
    }

    public NobelPrize setPhysiologyOrMedicine(int physiologyOrMedicine) {
        this.physiologyOrMedicine = physiologyOrMedicine;
        return this;
    }

    public int getEconomy() {
        return economy;
    }

    public NobelPrize setEconomy(int economy) {
        this.economy = economy;
        return this;
    }

    public int getLiterature() {
        return literature;
    }

    public NobelPrize setLiterature(int literature) {
        this.literature = literature;
        return this;
    }

    public int getPeace() {
        return peace;
    }

    public NobelPrize setPeace(int peace) {
        this.peace = peace;
        return this;
    }

    @Override
    public String toString() {
        return "NobelPrize{" +
                "ranking=" + ranking +
                ", university='" + university + '\'' +
                ", country='" + country + '\'' +
                ", total=" + total +
                ", naturalScienceAwardTotal=" + naturalScienceAwardTotal +
                ", physics=" + physics +
                ", chemistry=" + chemistry +
                ", physiologyOrMedicine=" + physiologyOrMedicine +
                ", economy=" + economy +
                ", literature=" + literature +
                ", peace=" + peace +
                '}';
    }
}
