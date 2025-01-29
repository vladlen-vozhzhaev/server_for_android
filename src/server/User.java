package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class User {
    private Socket socket;
    private String name;
    private DataInputStream is;
    private DataOutputStream out;

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
}
