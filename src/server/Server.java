package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
                            while (true){
                                currentUser.getOut().writeUTF("Для регистрации /reg, \n Для авторизации /login");
                                command = currentUser.getIs().readUTF().toLowerCase();
                                if(command.equals("/reg")){
                                    currentUser.getOut().writeUTF("Введите имя: ");
                                    String name = currentUser.getIs().readUTF();
                                    currentUser.getOut().writeUTF("Введите фамилию: ");
                                    String lastname = currentUser.getIs().readUTF();
                                    currentUser.getOut().writeUTF("Введите логин: ");
                                    String login = currentUser.getIs().readUTF();
                                    currentUser.getOut().writeUTF("Введите пароль: ");
                                    String pass = currentUser.getIs().readUTF();
                                    String[] params = {name, lastname, login, pass};
                                    Database.update("INSERT INTO users (name, lastname, login, pass) VALUES (?,?,?,?)", params);
                                    currentUser.setName(name);
                                    break;
                                } else if (command.equals("/login")) {
                                    currentUser.getOut().writeUTF("Введите логин: ");
                                    String login = currentUser.getIs().readUTF();
                                    currentUser.getOut().writeUTF("Введите пароль: ");
                                    String pass = currentUser.getIs().readUTF();
                                    String[] params = {login, pass};
                                    ResultSet resultSet = Database.query("SELECT * FROM users WHERE login = ? AND pass = ?", params);
                                    try {
                                        if(resultSet.next()){
                                            currentUser.setName(resultSet.getString("name"));
                                            break;
                                        }else {
                                            currentUser.getOut().writeUTF("error");
                                        }
                                    }catch (SQLException e){
                                        e.printStackTrace();
                                    }
                                } else {
                                   currentUser.getOut().writeUTF("неверная команда");
                                }
                            }
                            System.out.println(currentUser.getName() + " авторизовался");
                            currentUser.getOut().writeUTF("success");
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
