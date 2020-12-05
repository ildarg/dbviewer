package com.atcproject.dbviewer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

@Service
@Slf4j
@SessionScope
public class PoolService {

    final HashMap<Integer, Connection> connectionSet = new HashMap<>();

    public Connection getConnection(String url, String username, String password) throws SQLException {
        final Integer id = url.hashCode();
        if (connectionSet.containsKey(id)) {
            return connectionSet.get(id);
        }

        Connection connection = DriverManager.getConnection(url, username, password);
        connectionSet.put(id, connection);
        return connection;
    }
}
