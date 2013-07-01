package jp.ddo.chiroru.cellar.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <p>
 * DBコネクションを管理するヘルパーです.
 * </p>
 */
public final class ConnectionHelper {

    private String url;
    private String user;
    private String password;
    private static ConnectionHelper instance;

    private ConnectionHelper() throws Exception {
        String driver = null;
        try {
            ResourceBundle testBundle = ResourceBundle.getBundle("test-jdbc");
            driver =  testBundle.getString("jdbc.driver");
            Class.forName(driver);
            url = testBundle.getString("jdbc.url");
            user = testBundle.getString("jdbc.user");
            password = testBundle.getString("jdbc.password");
        } catch (MissingResourceException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
            driver =  bundle.getString("jdbc.driver");
            Class.forName(driver);
            url = bundle.getString("jdbc.url");
            user = bundle.getString("jdbc.user");
            password = bundle.getString("jdbc.password");
        }
    }

    public static Connection getConnection() {
        try {
            if (instance == null) {
                instance = new ConnectionHelper();
            }
            System.out.println("---------------------------------------------");
            System.out.println(" URL      : " + instance.url);
            System.out.println(" USER     : " + instance.user);
            System.out.println(" PASSWORD : " + instance.password);
            System.out.println("---------------------------------------------");

            return DriverManager.getConnection(instance.url,
                    instance.user,
                    instance.password);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
