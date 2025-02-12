package client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static DataOutputStream out;
    static DataInputStream is;
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 9123); // Подключаемся к серверу
            Scanner scanner = new Scanner(System.in);

            out = new DataOutputStream(socket.getOutputStream());
            is = new DataInputStream(socket.getInputStream());
            while (true){
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = new JSONObject();
                System.out.println("Для регистрации /reg\nДля авторизации /login");
                String userMsg = scanner.nextLine();
                if(userMsg.equals("/reg"))
                    jsonObject.put("command", "reg");
                else if (userMsg.equals("/login")) {
                    jsonObject.put("command", "login");
                }else{
                    System.out.println("Неверная команда");
                    continue;
                }
                out.writeUTF(jsonObject.toJSONString());
                String response = is.readUTF();
                jsonObject = (JSONObject) jsonParser.parse(response);
                if (jsonObject.get("command").toString().equals("allow_reg")) {
                    JSONObject userData = new JSONObject();
                    System.out.println("Введите имя: ");
                    userData.put("name", scanner.nextLine());
                    System.out.println("Введите фамилию: ");
                    userData.put("lastname", scanner.nextLine());
                    System.out.println("Введите логин: ");
                    userData.put("login", scanner.nextLine());
                    System.out.println("Введите пароль: ");
                    userData.put("pass", scanner.nextLine());
                    jsonObject.put("user_data", userData);
                    out.writeUTF(jsonObject.toJSONString());
                    response = is.readUTF();
                    openChat((JSONObject) jsonParser.parse(response));
                    break;
                } else if (jsonObject.get("command").toString().equals("allow_login")){
                    JSONObject userData = new JSONObject();
                    System.out.println("Введите логин: ");
                    userData.put("login", scanner.nextLine());
                    System.out.println("Введите пароль: ");
                    userData.put("pass", scanner.nextLine());
                    jsonObject.put("user_data", userData);
                    out.writeUTF(jsonObject.toJSONString());
                    response = is.readUTF();
                    openChat((JSONObject) jsonParser.parse(response));
                    break;
                } else if (jsonObject.get("command").toString().equals("wrong_data")) {
                    System.out.println("Ошибка при вводе данных");
                }

            }
        } catch (Exception e) {
            System.out.println("Соединение потеряно");
            System.out.println("Невозможно поключиться к серверу");
        }
    }
    public static void openChat(JSONObject jsonObject){
        Scanner scanner = new Scanner(System.in);
        if (jsonObject.get("command").toString().equals("success")){
            System.out.println("Успешный вход в чат!");
            Thread inputMessageThread = new Thread(()->{
                try {
                    while (true){
                        String response = is.readUTF();
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            inputMessageThread.start();
            Thread thread = new Thread(()->{ // Тут общаемся в чате
                try {
                    JSONObject messageJSON = new JSONObject();
                    String userMessage;
                    while (true){ // {to: <int:id>, message: <String:msg>}
                        System.out.println("ID получателя: ");
                        int to = Integer.parseInt(scanner.nextLine());
                        System.out.println("Текст сообщения: ");
                        userMessage = scanner.nextLine();
                        messageJSON.put("to", to);
                        messageJSON.put("message", userMessage);
                        out.writeUTF(messageJSON.toJSONString());
                    }
                }catch (IOException e){
                    System.out.println("Соединение потеряно");
                }
            });
            thread.start();
        }
    }
}
