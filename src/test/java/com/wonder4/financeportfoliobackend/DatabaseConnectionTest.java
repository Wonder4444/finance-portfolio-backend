package com.wonder4.financeportfoliobackend;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired private DataSource dataSource;

    @Test
    void testConnection() throws SQLException {
        System.out.println("start");
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
            assertTrue(connection.isValid(1));
            System.out.println("Connection built: " + connection.getMetaData().getURL());
        }
        System.out.println("end");
    }
}
