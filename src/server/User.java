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
        Database.update("INSERT INTO users (name, lastname, login, pass) VALUES (?,?,?,?)", params);
        this.setName(name);
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
                return true;
            }else {
                this.getOut().writeUTF("error");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
