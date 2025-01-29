package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/chat45";
    private static final String DB_LOGIN = "root";
    private static final String DB_PASS = "";
    private static Connection connection;
    public static ResultSet query(String sql, String[] params){
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_LOGIN, DB_PASS);
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setString(i+1, params[i]);
            }
            resultSet = preparedStatement.executeQuery();

        }catch (Exception e){
            e.printStackTrace();
        }
        return resultSet;
    }
    public static void update(String sql, String[] params){
        try {
            connection = DriverManager.getConnection(DB_URL, DB_LOGIN, DB_PASS);
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setString(i+1, params[i]);
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
