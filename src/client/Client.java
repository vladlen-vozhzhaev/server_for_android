package client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 9123); // Подключаемся к серверу
            Scanner scanner = new Scanner(System.in);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream is = new DataInputStream(socket.getInputStream());
            while (true){
                String response = is.readUTF();
                System.out.println(response);
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
                if(jsonObject.get("command").toString().equals("auth")){
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
                } else if (jsonObject.get("command").toString().equals("allow_reg")) {
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
                } else if (jsonObject.get("command").toString().equals("allow_login")){
                    JSONObject userData = new JSONObject();
                    System.out.println("Введите логин: ");
                    userData.put("login", scanner.nextLine());
                    System.out.println("Введите пароль: ");
                    userData.put("pass", scanner.nextLine());
                    jsonObject.put("user_data", userData);
                    out.writeUTF(jsonObject.toJSONString());
                } else if (jsonObject.get("command").toString().equals("wrong_data")) {
                    System.out.println("Ошибка при вводе данных");
                } else if (jsonObject.get("command").toString().equals("success")){
                    System.out.println("Успешный вход в чат!");
                    Thread thread = new Thread(()->{ // Тут общаемся с в чате
                        try {
                            while (true){
                                String userMsg = scanner.nextLine();
                                out.writeUTF(userMsg);
                            }
                        }catch (IOException e){
                            System.out.println("Соединение потеряно");
                        }
                    });
                    thread.start();
                }

            }
        } catch (Exception e) {
            System.out.println("Соединение потеряно");
            System.out.println("Невозможно поключиться к серверу");
        }
    }
}
