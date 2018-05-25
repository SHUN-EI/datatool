package com.ys.datatool.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by mo on @date  2018/5/22.
 */
public class FBDBConnUtil {

    private static final String DRIVER = "org.firebirdsql.jdbc.FBDriver";

    //"jdbc:firebirdsql:localhost/3050:D:\\datasource\\firebird_export\\vbms.fdb";
    private static final String URL = "jdbc:firebirdsql:127.0.0.1/3050:D:\\datasource\\firebird_export\\vbms.fdb";

    private static final String USER = "SYSDBA";

    private static final String PASSWORD = "masterkey";

    private static Connection connection;

    private FBDBConnUtil() {
    }

    static {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    //静态工厂方法，返回connection实例
    public static Connection getConnection() {
        return connection;
    }
}
