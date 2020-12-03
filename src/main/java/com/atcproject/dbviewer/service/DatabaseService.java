package com.atcproject.dbviewer.service;

import com.atcproject.dbviewer.handlers.DBViewerException;
import com.atcproject.dbviewer.model.ConnectionDetail;
import com.atcproject.dbviewer.model.TableRowView;
import com.atcproject.dbviewer.model.TableView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.atcproject.dbviewer.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseService {

    private final ConnectionService connectionService;

    private final PoolService pool;

    public List<String> getSchemas(Long connectionID) {

        try {
            DatabaseMetaData metaData = getDatabaseMetaData(connectionID);
            List<String> tables = new ArrayList<>();
            try (ResultSet rs = metaData.getSchemas()) {
                while (rs.next()) {
                    tables.add(rs.getString(SCHEMA_NAME_COLUMN_INDEX));
                }
            }
            return tables;
        } catch (SQLException e) {
            log.error("Can not get meta data for schemas, {} ", e);
            throw new DBViewerException(e.getMessage());
        }
    }

    private DatabaseMetaData getDatabaseMetaData(Long connectionID) throws SQLException {
        ConnectionDetail conn = connectionService.getConnectionById(connectionID);

        if (null == conn) {
            throw new DBViewerException(CONNECTION_NOT_FOUND);
        }

        Connection connection = pool.getConnection(conn.getUrl(), conn.getUsername(), conn.getPassword());
        return connection.getMetaData();
    }

    public List<String> getTables(Long connectionID, String schema) {

        try {
            DatabaseMetaData metaData = getDatabaseMetaData(connectionID);
            List<String> tables = new ArrayList<>();
            final String schemaParam = null == schema ? PATTERN_ALL : schema;
            try (ResultSet rs = metaData.getTables(null, schemaParam, PATTERN_ALL, new String[] {"TABLE", "VIEW"})) {
                while (rs.next()) {
                    tables.add(rs.getString(TABLE_NAME_COLUMN_INDEX));
                }
            }
            return tables;
        } catch (SQLException e) {
            log.error("Can not get meta data for tables, {} ", e);
            throw new DBViewerException(e.getMessage());
        }
    }

    public List<String> getColumns(Long connectionID, String schema, String table) {

        try {
            DatabaseMetaData metaData = getDatabaseMetaData(connectionID);

            List<String> tables = new ArrayList<>();
            final String schemaParam = null == schema ? PATTERN_ALL : schema;
            final String tableParam = null == table ? PATTERN_ALL : table;
            try (ResultSet rs = metaData.getColumns(null, schemaParam, tableParam, PATTERN_ALL)) {
                while (rs.next()) {
                    tables.add(rs.getString(COLUMN_NAME_COLUMN_INDEX));
                }
            }
            return tables;
        } catch (SQLException e) {
            log.error("Can not get meta data for columns, {} ", e);
            throw new DBViewerException(e.getMessage());
        }
    }

    public TableView getData(Long id, String schema, String table) {
        ConnectionDetail conn = connectionService.getConnectionById(id);

        if (null == conn) {
            throw new DBViewerException(CONNECTION_NOT_FOUND);
        }

        try {
            Connection connection = pool.getConnection(conn.getUrl(), conn.getUsername(), conn.getPassword());

            try (Statement stmt = connection.createStatement()) {

                ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM %s.%s", schema, table));

                TableView tableView = new TableView();

                List<String> headers = getColumns(id, schema, table);
                tableView.getColumns().addAll(headers);
                int rowNumber = 0;
                while (rs.next() && rowNumber < PREVIEW_ROW_NUMBER) {
                    ResultSetMetaData rsMeta = rs.getMetaData();
                    int columnCount = rsMeta.getColumnCount();
                    TableRowView viewRow = new TableRowView();
                    for (int i = 1; i <= columnCount; i++) {
                        viewRow.getValues().add(rs.getString(i));
                    }
                    tableView.getRows().add(viewRow);
                    rowNumber++;
                }
                return tableView;
            }

        } catch (SQLException e) {
            log.error("Can not fetch data for preview, {} ", e);
            throw new DBViewerException(e.getMessage());
        }
    }
}
