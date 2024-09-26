package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
@Slf4j
public class ConnectionTest {

    private static void useDataSource(final DriverManagerDataSource dataSource) throws SQLException {
        final Connection con1 = dataSource.getConnection();
        final Connection con2 = dataSource.getConnection();
        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con2, con2.getClass());
    }

    @Test
    void driveManager() throws SQLException {

        final Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        final Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("connection={}, class={}", con1, con1.getClass());
        log.info("connection={}, class={}", con2, con2.getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException {

        final DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        useDataSource(dataSource);
    }
}
