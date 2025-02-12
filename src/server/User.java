package server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private Socket socket;
    private String name;
    private String lastname;
    private String login;
    private int userId;
    private DataInputStream is;
    private DataOutputStream out;
    private JSONObject jsonObject = new JSONObject();
    private JSONParser jsonParser = new JSONParser();
    public User(Socket socket) {
        try {
            this.socket = socket;
            this.is = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
            System.out.println("Ошибка создания потоков ввода-вывода");
        }
    }

    public String getName() {
        return name;
    }
    public DataInputStream getIs() {
        return is;
    }
    public DataOutputStream getOut() {
        return out;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getUserId() {return userId;}

    public void reg() throws Exception{
        jsonObject.put("command", "allow_reg");
        this.getOut().writeUTF(jsonObject.toJSONString());
        String stringUserData = this.is.readUTF();
        jsonObject = (JSONObject) jsonParser.parse(stringUserData);
        JSONObject userData = (JSONObject) jsonParser.parse(jsonObject.get("user_data").toString());
        String name = userData.get("name").toString();
        String lastname = userData.get("lastname").toString();
        String login = userData.get("login").toString();
        String pass = userData.get("pass").toString();
        String[] params = {name, lastname, login, pass};
        this.userId = Database.update("INSERT INTO users (name, lastname, login, pass) VALUES (?,?,?,?)", params);
        this.lastname = lastname;
        this.login = login;
        this.name = name;
    }
    public boolean login() throws Exception{
        jsonObject.put("command", "allow_login");
        this.getOut().writeUTF(jsonObject.toJSONString());
        String stringUserData = this.is.readUTF();
        jsonObject = (JSONObject) jsonParser.parse(stringUserData);
        JSONObject userData = (JSONObject) jsonParser.parse(jsonObject.get("user_data").toString());
        String login = userData.get("login").toString();
        String pass = userData.get("pass").toString();
        String[] params = {login, pass};
        ResultSet resultSet = Database.query("SELECT * FROM users WHERE login = ? AND pass = ?", params);
        try {
            if(resultSet.next()){
                this.setName(resultSet.getString("name"));
                this.lastname = resultSet.getString("lastname");
                this.login = login;
                this.userId = resultSet.getInt("id");
                return true;
            }else {
                jsonObject.put("command", "error");
                this.getOut().writeUTF(jsonObject.toJSONString());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
