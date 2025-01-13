package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        ArrayList<Socket> sockets = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(9123);
            System.out.println("Сервер запущен");
            while (true){
                Socket socket = serverSocket.accept();
                sockets.add(socket);
                System.out.println("Клиент подключился");
                DataInputStream is = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run(){
                        try {
                            while (true){
                                String message = is.readUTF();
                                for (Socket socket: sockets) {
                                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                                    out.writeUTF(message);
                                }
                                System.out.println(message);
                            }
                        }catch (IOException e){
                            System.out.println("Потеряно соединение с клиентом");
                        }
                    }
                });
                thread.start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
