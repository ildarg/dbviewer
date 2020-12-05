package com.atcproject.dbviewer.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnMetaView {
    private String columnName;
    private int columnType;
    private String columnTypeName;
    private int precision;
    private int columnDisplaySize;

    public ColumnMetaView(String columnName, String columnTypeName, int columnType, int precision, int columnDisplaySize) {
        this.columnName = columnName;
        this.columnTypeName = columnTypeName;
        this.columnType = columnType;
        this.precision = precision;
        this.columnDisplaySize = columnDisplaySize;
    }
}
