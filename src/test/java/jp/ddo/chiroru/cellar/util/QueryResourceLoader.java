package jp.ddo.chiroru.cellar.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ResourceBundle;

public class QueryResourceLoader {

    private final static String SCHEMA_FILE = "schema.sql";

    private final static String ENCODING = "UTF-8";

    private String resource;
    
    protected String loadResource(String resource) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedInputStream bis = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream(resource));
        byte[] buffer = new byte[1024];
        while (bis.read(buffer) != -1 ) {
            builder.append(new String(buffer, ENCODING));
            buffer = new byte[1024];
        }
        return builder.toString();
    }

    public QueryResourceLoader(String resource) {
        this.resource = resource;
    }
    
    public void load() throws Exception {
        QueryRunner runner = new QueryRunner();
        String schema = loadResource(SCHEMA_FILE);
        String[] ddl = schema.split(";");
        for (String q : ddl) {
            runner.run(q);
        }
        String data = loadResource(resource);
        String[] dml = data.split(";");
        for (String q : dml) {
            runner.run(q);
        }
    }
    
    public static void main(String[] args) throws Exception {
        QueryResourceLoader s = new QueryResourceLoader("seeds.sql");
        s.load();
    }
}

class QueryRunner {
    private static String driver;
    private static String url;
    private static String user;
    private static String password;
    
    static {
        ResourceBundle bundle = ResourceBundle.getBundle("test-jdbc");
        driver = bundle.getString("jdbc.driver");
        url = bundle.getString("jdbc.url");
        user = bundle.getString("jdbc.user");
        password = bundle.getString("jdbc.password");
    }

    private Connection getConnection() throws Exception {
        Class.forName(driver);
        Connection c = DriverManager.getConnection(url, user, password);
        c.setAutoCommit(true);
        return c;
    }

    void run(String query) throws Exception {
        Statement stmt = getConnection().createStatement();
        stmt.execute(query);
    }
}
