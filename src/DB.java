import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class DB {
    public static void main(String[] args) {
        final String DB_URL = "jdbc:mysql://127.0.0.1/chat45";
        final String DB_LOGIN = "root";
        final String DB_PASS = "";
        Connection connection;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите логин: ");
        String login = scanner.nextLine();
        System.out.println("Введите пароль:");
        String pass = scanner.nextLine();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection(DB_URL, DB_LOGIN, DB_PASS);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE login=? AND pass=?");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, pass);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("name");
                String lastname = resultSet.getString("lastname");
                System.out.println(name+" "+lastname);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}