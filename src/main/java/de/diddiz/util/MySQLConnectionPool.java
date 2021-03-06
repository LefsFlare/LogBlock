package de.diddiz.util;

import com.zaxxer.hikari.HikariDataSource;
import de.diddiz.LogBlock.config.Config;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnectionPool implements Closeable {

    private final HikariDataSource ds;

    public MySQLConnectionPool(String url, String user, String password) throws ClassNotFoundException {
        this.ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);

        ds.setMinimumIdle(25);
        ds.setMaximumPoolSize(25);
        ds.setPoolName("LogBlock-Connection-Pool");

        ds.addDataSourceProperty("useUnicode", "true");
        ds.addDataSourceProperty("characterEncoding", "utf-8");
        ds.addDataSourceProperty("rewriteBatchedStatements", "true");

        ds.addDataSourceProperty("cachePrepStmts", "true");
        ds.addDataSourceProperty("prepStmtCacheSize", "250");
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

    @Override
    public void close() {
        ds.close();
    }

    public Connection getConnection() throws SQLException {
        Connection connection = ds.getConnection();
        if (Config.mb4) {
            connection.createStatement().executeQuery("SET NAMES utf8mb4");
        }
        return connection;
    }

}
