package com.wonder4.financeportfoliobackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
