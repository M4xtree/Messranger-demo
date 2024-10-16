package com.messranger.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataBaseConfig {

    private HikariDataSource dataSource;
    private Flyway flyway;
    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseConfig.class);

    public DataBaseConfig() {
        Properties properties = loadProperties();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty("jdbc.url"));
        config.setUsername(properties.getProperty("jdbc.username"));
        config.setPassword(properties.getProperty("jdbc.password"));
        config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("jdbc.maxPoolSize")));
        this.dataSource = new HikariDataSource(config);

        this.flyway = Flyway.configure().dataSource(this.dataSource).load();
        this.flyway.migrate();
        LOGGER.info(flyway.info().toString());
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IOException("Sorry, unable to find db.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            LOGGER.error(ex.toString());
        }
        return properties;
    }

    public Logger getLogger(){
        return LOGGER;
    }

    public HikariDataSource getDataSource() {
        return this.dataSource;
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
