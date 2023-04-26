package com.github.fengxxc.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author fengxxc
 */
public class NobelPrize implements Serializable {
    private static final long serialVersionUID = 1L;

    private int ranking;
    private String university;
    private String country;
    private int total;
    private int naturalScienceAwardTotal;
    private int physics;
    private String chemistry;
    private int physiologyOrMedicine;
    private int economy;
    private int literature;
    private String peace;


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

    public String getChemistry() {
        return chemistry;
    }

    public NobelPrize setChemistry(String chemistry) {
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

    public String getPeace() {
        return peace;
    }

    public NobelPrize setPeace(String peace) {
        this.peace = peace;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NobelPrize that = (NobelPrize) o;
        return ranking == that.ranking
                && total == that.total
                && naturalScienceAwardTotal == that.naturalScienceAwardTotal
                && physics == that.physics
                && physiologyOrMedicine == that.physiologyOrMedicine
                && economy == that.economy
                && literature == that.literature
                && Objects.equals(university, that.university)
                && Objects.equals(country, that.country)
                && Objects.equals(chemistry, that.chemistry)
                && Objects.equals(peace, that.peace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ranking, university, country, total, naturalScienceAwardTotal, physics, chemistry, physiologyOrMedicine, economy, literature, peace);
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
