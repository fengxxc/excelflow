package com.github.fengxxc.model;

/**
 * @author fengxxc
 * @date 2023-04-13
 */
public class EFCell {
    private String cellReference;
    private String formattedValue;
    private CellMapper cellMapper;

    public EFCell() {
    }

    public EFCell(String cellReference, String formattedValue) {
        this.cellReference = cellReference;
        this.formattedValue = formattedValue;
    }

    public EFCell(String cellReference, String formattedValue, CellMapper cellMapper) {
        this.cellReference = cellReference;
        this.formattedValue = formattedValue;
        this.cellMapper = cellMapper;
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

    public CellMapper getCellMapper() {
        return cellMapper;
    }

    public EFCell setCellMapper(CellMapper cellMapper) {
        this.cellMapper = cellMapper;
        return this;
    }

    @Override
    public String toString() {
        return "EFCell{" +
                "cellReference='" + cellReference + '\'' +
                ", formattedValue='" + formattedValue + '\'' +
                ", cellMapper=" + cellMapper.toString() +
                '}';
    }
}
