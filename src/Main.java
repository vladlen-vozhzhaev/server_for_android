import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        /*MultiThread task1 = new MultiThread("Первая задача");
        MultiThread task2 = new MultiThread("Вторая задача");
        MultiThread task3 = new MultiThread("Третья задача");
        task1.start();
        task2.start();
        task3.start();*/
        /*Thread thread1 = new Thread(new MyRunnableCass("Первая задача"));
        Thread thread2 = new Thread(new MyRunnableCass("Вторая задача"));
        Thread thread3 = new Thread(new MyRunnableCass("Третья задача"));
        thread1.start();
        thread2.start();
        thread3.start();*/
        /*Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    System.out.println("Первая задача"+" "+i);
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    System.out.println("Вторая задача"+" "+i);
                }
            }
        });
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    System.out.println("Третья задача"+" "+i);
                }
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();*/
        Thread thread1 = new Thread(()->{
            for (int i = 0; i < 100; i++) {
                System.out.println("Первая задача"+" "+i);
            }
        });
        Thread thread2 = new Thread(()->{
            for (int i = 0; i < 100; i++) {
                System.out.println("Вторая задача"+" "+i);
            }
        });
        Thread thread3 = new Thread(()->{
            for (int i = 0; i < 100; i++) {
                System.out.println("Третья задача"+" "+i);
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();
    }
}

class MyRunnableCass implements Runnable{
    public String task = "";

    public MyRunnableCass(String task) {
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(task+" "+i);
        }
    }
}

class MultiThread extends Thread{
    public String task = "";
    public MultiThread(String task){
        this.task = task;
    }
    @Override
    public void run(){
        for (int i = 0; i < 100; i++) {
            System.out.println(task+" "+i);
        }
    }
}


/*public class Main {
    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream("test.txt");
            int i = 0;
            while ((i = fis.read())!=-1){
                System.out.print((char) i);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        *//*try {
            FileOutputStream fos = new FileOutputStream("test.txt");
            String str = "Hello world!";
            byte[] buff = str.getBytes();
            fos.write(buff);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*//*

    }
}*/
