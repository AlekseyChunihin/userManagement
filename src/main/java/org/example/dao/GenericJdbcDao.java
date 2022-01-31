package org.example.dao;

import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class GenericJdbcDao {

    private static final Logger log = LoggerFactory.getLogger(GenericJdbcDao.class);

    private static final JdbcConnectionPool jdbcConnectionPool;

    static {
        Properties props = loadProperties();
        String url = props.getProperty("url");
        log.info("Connecting to {}", url);
        jdbcConnectionPool = JdbcConnectionPool.create(url,
                props.getProperty("user"), props.getProperty("password"));
    }

    public static JdbcConnectionPool getConnectionPool() {
        return jdbcConnectionPool;
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = GenericJdbcDao.class.getResourceAsStream("/h2.properties")) {
            props.load(input);
        } catch (IOException | NullPointerException e) {
            log.error("Failed to get properties {}", e.getMessage());
        }
        return props;
    }
}
