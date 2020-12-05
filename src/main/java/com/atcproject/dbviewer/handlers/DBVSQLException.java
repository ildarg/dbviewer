package com.atcproject.dbviewer.handlers;

import com.atcproject.dbviewer.Constants;

import java.sql.SQLException;

public class DBVSQLException extends DBViewerException {
    public DBVSQLException(SQLException ex) {
        super(Constants.ERR_SQL_EXCEPTION, Constants.SQL_EXECUTION_ERROR, ex.getLocalizedMessage());
    }
}
