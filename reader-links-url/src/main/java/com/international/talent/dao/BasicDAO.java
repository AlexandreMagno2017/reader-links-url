package com.international.talent.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;

public abstract class BasicDAO
{
    private String url;

    private static JdbcConnectionPool cp;

    public BasicDAO(String url)
    {
        this.url = url;
        if (cp == null)
        {
            createConnectionPool(url);
        }
    }

    private void createConnectionPool(String url)
    {
        cp = JdbcConnectionPool.create(url, "", "");
        cp.setMaxConnections(50);
    }

    public Connection getConnection() throws SQLException
    {
        return cp.getConnection();
    }

}
