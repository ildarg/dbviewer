package com.atcproject.dbviewer;

public class Constants {

    public static final String PATTERN_ALL = "%";
    public static final String META_SCHEMA_NAME = "TABLE_SCHEM";
    public static final String META_TABLE_NAME = "TABLE_NAME";
    public static final String META_COLUMN_NAME = "COLUMN_NAME";
    public static final int PREVIEW_ROW_NUMBER = 100;

    public static final String ERR_DEFAULT = "APP-0000";
    public static final String ERR_DATA_INTEGRITY = "APP-0001";
    public static final String ERR_WRONG_STATE = "APP-0002";
    public static final String ERR_WRONG_INPUT_PARAMS = "APP-0003";
    public static final String ERR_ENTITY_NOT_FOUND = "APP-0004";
    public static final String ERR_SQL_EXCEPTION = "APP-0005";

    public static final String ENTITY_NOT_FOUND = "Entity not found";
    public static final String SQL_EXECUTION_ERROR = "SQL Execution error";


    private Constants() {
        throw new IllegalStateException("Constant class");
    }
}
