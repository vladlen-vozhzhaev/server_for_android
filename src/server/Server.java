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
        ArrayList<User> users = new ArrayList<>();
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
                            jsonObject.put("command", "auth");
                            while (true){
                                currentUser.getOut().writeUTF(jsonObject.toJSONString());
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
                                for (User user : users) {
                                    if (user != currentUser){
                                        user.getOut().writeUTF(currentUser.getName()+": "+message);
                                    }
                                }
                                System.out.println(currentUser.getName()+": "+message);
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