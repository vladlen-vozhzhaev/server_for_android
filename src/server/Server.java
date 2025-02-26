package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
/*
* serverJson = {
*   "command": "Команда от сервера",
*   "error": "сообщение об ошибке",
*   "msg": "сообщение"
* }
* */
public class Server {
    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>(); // Создаём список пользователей
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            ServerSocket serverSocket = new ServerSocket(9123); // Открываем порт 9123
            System.out.println("Сервер запущен");
            while (true){
                Socket socket = serverSocket.accept(); // Ожидаем подключение клиента
                User currentUser = new User(socket);
                users.add(currentUser);
                System.out.println("Клиент подключился");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run(){
                        try {
                            String command;
                            JSONObject jsonObject = new JSONObject();
                            JSONParser jsonParser = new JSONParser();
                            while (true){
                                command = currentUser.getIs().readUTF();
                                jsonObject = (JSONObject) jsonParser.parse(command);
                                System.out.println(jsonObject.toJSONString());
                                if(jsonObject.get("command").toString().equals("reg")){
                                    currentUser.reg();
                                    break;
                                } else if (jsonObject.get("command").toString().equals("login")) {
                                    if(currentUser.login()) break;
                                }
                                jsonObject.put("command", "wrong_data");
                                currentUser.getOut().writeUTF(jsonObject.toJSONString());
                            }
                            System.out.println(currentUser.getName() + " авторизовался");
                            jsonObject.put("command", "success");
                            currentUser.getOut().writeUTF(jsonObject.toJSONString());
                            while (true){
                                String message = currentUser.getIs().readUTF();
                                jsonObject = (JSONObject) jsonParser.parse(message); // {to: <int:id>, message: <String:msg>}
                                System.out.println(message);
                                if (jsonObject.get("command").equals("find_user")){
                                    String login = jsonObject.get("login").toString();
                                    ResultSet resultSet = Database.query("SELECT * FROM users WHERE login = ?", new String[]{login});
                                    if(resultSet.next()){
                                        String name = resultSet.getString("name");
                                        String lastname = resultSet.getString("lastname");
                                        int userId = resultSet.getInt("id");
                                        jsonObject.put("name", name);
                                        jsonObject.put("lastname", lastname);
                                        jsonObject.put("id", userId);
                                        currentUser.getOut().writeUTF(jsonObject.toJSONString());
                                    }else{
                                        jsonObject.put("error", "not_exist");
                                        currentUser.getOut().writeUTF(jsonObject.toJSONString());
                                    }
                                } else if (jsonObject.get("command").equals("get_message")) {
                                    int toId = Integer.parseInt(jsonObject.get("to_id").toString());
                                    int userId = currentUser.getUserId();
                                    String[] params = {String.valueOf(toId), String.valueOf(userId), String.valueOf(toId), String.valueOf(userId)};
                                    ResultSet resultSet = Database.query("SELECT * FROM messages WHERE to_id = ? AND from_id = ? OR from_id = ? AND to_id = ?", params);
                                    JSONArray jsonMessages = new JSONArray();
                                    while (resultSet.next()){
                                        JSONObject jsonMessage = new JSONObject();
                                        int from_id = resultSet.getInt("from_id");
                                        int to_id = resultSet.getInt("to_id");
                                        String msg = resultSet.getString("msg");
                                        jsonMessage.put("from_id", from_id);
                                        jsonMessage.put("to_id", to_id);
                                        jsonMessage.put("msg", msg);
                                        jsonMessages.add(jsonMessage);
                                    }
                                    System.out.println(jsonMessages.toJSONString());
                                    currentUser.getOut().writeUTF(jsonMessages.toJSONString());
                                } else{
                                    for (User user : users) {
                                        int to = Integer.parseInt(jsonObject.get("to").toString()); // Кому отправляем
                                        String msg = jsonObject.get("message").toString(); // Сообщение
                                        int from = currentUser.getUserId(); // Кто отправляет
                                        Database.update("INSERT INTO messages (from_id, to_id, msg) VALUES (?,?,?)", new String[]{String.valueOf(from), String.valueOf(to), msg});// тут запрос к БД для сохранения сообщения!
                                        if (user.getUserId() == to){
                                            user.getOut().writeUTF(currentUser.getName()+": "+msg);
                                        }
                                    }
                                    System.out.println(currentUser.getName()+": "+message);
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            System.out.println("Потеряно соединение с клиентом");
                            users.remove(currentUser);
                        }
                    }
                });
                thread.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}