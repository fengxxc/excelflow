package com.github.fengxxc.model;

import java.util.List;

/**
 * @author fengxxc
 * @date 2023-04-13
 */
public class EFCell {
    private String cellReference;
    private String formattedValue;
    private List<CellMapper> cellMappers;

    public EFCell() {
    }

    public EFCell(String cellReference, String formattedValue) {
        this.cellReference = cellReference;
        this.formattedValue = formattedValue;
    }

    public EFCell(String cellReference, String formattedValue, List<CellMapper> cellMappers) {
        this.cellReference = cellReference;
        this.formattedValue = formattedValue;
        this.cellMappers = cellMappers;
    }

    public String getCellReference() {
        return cellReference;
    }

    public EFCell setCellReference(String cellReference) {
        this.cellReference = cellReference;
        return this;
    }

    public String getFormattedValue() {
        return formattedValue;
    }

    public EFCell setFormattedValue(String formattedValue) {
        this.formattedValue = formattedValue;
        return this;
    }

    public List<CellMapper> getCellMappers() {
        return cellMappers;
    }

    public EFCell setCellMappers(List<CellMapper> cellMappers) {
        this.cellMappers = cellMappers;
        return this;
    }

    @Override
    public String toString() {
        return "EFCell{" +
                "cellReference='" + cellReference + '\'' +
                ", formattedValue='" + formattedValue + '\'' +
                ", cellMapper=" + cellMappers.toString() +
                '}';
    }
}
