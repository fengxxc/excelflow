package com.github.fengxxc.model;

import java.util.Date;
import java.util.Objects;

/**
 * @author fengxxc
 */
public class MaskCard {
    private String name;
    private int number;
    private String company;
    private String job;
    private String tel;
    private Date time;

    public MaskCard() {
    }

    public String getName() {
        return name;
    }

    public MaskCard setName(String name) {
        this.name = name;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public MaskCard setNumber(int number) {
        this.number = number;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public MaskCard setCompany(String company) {
        this.company = company;
        return this;
    }

    public String getJob() {
        return job;
    }

    public MaskCard setJob(String job) {
        this.job = job;
        return this;
    }

    public String getTel() {
        return tel;
    }

    public MaskCard setTel(String tel) {
        this.tel = tel;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public MaskCard setTime(Date time) {
        this.time = time;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaskCard maskCard = (MaskCard) o;
        return number == maskCard.number && Objects.equals(name, maskCard.name) && Objects.equals(company, maskCard.company) && Objects.equals(job, maskCard.job) && Objects.equals(tel, maskCard.tel) && Objects.equals(time, maskCard.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number, company, job, tel, time);
    }

    @Override
    public String toString() {
        return "MaskCard{" +
                "name='" + name + '\'' +
                ", number=" + number +
                ", company='" + company + '\'' +
                ", job='" + job + '\'' +
                ", tel='" + tel + '\'' +
                ", time=" + time +
                '}';
    }
}
