package client.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public interface H2DBConnector {

        default Connection connectToDB() {
            try {
                Class.forName("org.h2.Driver");
                return DriverManager.getConnection("jdbc:h2:mem:h2db;DB_CLOSE_DELAY=-1", "", "");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }