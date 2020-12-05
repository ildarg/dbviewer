package com.atcproject.dbviewer.service;

import com.atcproject.dbviewer.handlers.DBVSQLException;
import com.atcproject.dbviewer.model.ColumnMetaView;
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

    private DatabaseMetaData getDatabaseMetaData(Long connectionID) throws SQLException {
        ConnectionDetail conn = connectionService.getConnectionDetailsById(connectionID);
        Connection connection = pool.getConnection(conn.getUrl(), conn.getUsername(), conn.getPassword());
        return connection.getMetaData();
    }

    public List<String> getSchemas(Long connectionID) {
        try {
            DatabaseMetaData metaData = getDatabaseMetaData(connectionID);
            List<String> schemas = new ArrayList<>();
            try (ResultSet rs = metaData.getSchemas()) {
                while (rs.next()) {
                    schemas.add(rs.getString(META_SCHEMA_NAME));
                }
            }
            return schemas;
        } catch (SQLException e) {
            throw new DBVSQLException(e);
        }
    }

    public List<String> getTables(Long connectionID, String schema) {
        try {
            DatabaseMetaData metaData = getDatabaseMetaData(connectionID);
            List<String> tables = new ArrayList<>();
            final String schemaParam = null == schema ? PATTERN_ALL : schema;
            try (ResultSet rs = metaData.getTables(null, schemaParam, PATTERN_ALL, new String[] {"TABLE", "VIEW"})) {
                while (rs.next()) {
                    tables.add(rs.getString(META_TABLE_NAME));
                }
            }
            return tables;
        } catch (SQLException e) {
            throw new DBVSQLException(e);
        }
    }

    public List<String> getColumnNames(Long connectionID, String schema, String table) {
        try {
            DatabaseMetaData metaData = getDatabaseMetaData(connectionID);
            List<String> columns = new ArrayList<>();
            final String schemaParam = null == schema ? PATTERN_ALL : schema;
            final String tableParam = null == table ? PATTERN_ALL : table;
            try (ResultSet rs = metaData.getColumns(null, schemaParam, tableParam, PATTERN_ALL)) {
                while (rs.next()) {
                    columns.add(rs.getString(META_COLUMN_NAME));
                }
            }
            return columns;
        } catch (SQLException e) {
            throw new DBVSQLException(e);
        }
    }

    public TableView getData(Long id, String schema, String table) {
        ConnectionDetail conn = connectionService.getConnectionDetailsById(id);

        try {
            Connection connection = pool.getConnection(conn.getUrl(), conn.getUsername(), conn.getPassword());

            try (Statement stmt = connection.createStatement()) {

                ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM %s.%s", schema, table));

                TableView tableView = new TableView();

                ResultSetMetaData rsMetaHeader = rs.getMetaData();
                int columnHeaderCount = rsMetaHeader.getColumnCount();
                for (int i = 1; i <= columnHeaderCount; i++) {
                    ColumnMetaView columnMetaView = new ColumnMetaView(
                            rsMetaHeader.getColumnName(i),
                            rsMetaHeader.getColumnTypeName(i),
                            rsMetaHeader.getColumnType(i),
                            rsMetaHeader.getPrecision(i),
                            rsMetaHeader.getColumnDisplaySize(i)
                    );
                    tableView.getColumns().add(columnMetaView);
                }

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
            throw new DBVSQLException(e);
        }
    }
}
