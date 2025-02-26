import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Person person1 = new Person("Ivan");
        Person person2 = new Person("Alex");
        System.out.println(person1.getHp());
        System.out.println(person2.getHp());
        person2.setHp(50);
        System.out.println(person1.getHp());


    }
}





/*public class Main {
    public static void main(String[] args) {
        int[] nums = new int[10];
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите 10 чисел:");
        for (int i = 0; i < 10; i++) {
            nums[i] = scanner.nextInt();
        }

        // Находим длину самой длинной последовательности повторяющихся чисел
        int maxSequenceLength = 1;
        int currentSequenceLength = 1;
        for (int i = 1; i < 10; i++) {
            if (nums[i] == nums[i - 1]) {
                currentSequenceLength++;
            } else {
                maxSequenceLength = Math.max(maxSequenceLength, currentSequenceLength);
                currentSequenceLength = 1;
            }
        }
        maxSequenceLength = Math.max(maxSequenceLength, currentSequenceLength);

        System.out.println("Длина самой длинной последовательности повторяющихся чисел: " + maxSequenceLength);
    }
}*/



/*public class Main {
    public static void main(String[] args) {
        MyClass.sayHi();
    }
}

class MyClass{
    public static void sayHi(){
        System.out.println("Hello world");
    }
}*/
/*    public static void main(String[] args) {
        *//*MultiThread task1 = new MultiThread("Первая задача");
        MultiThread task2 = new MultiThread("Вторая задача");
        MultiThread task3 = new MultiThread("Третья задача");
        task1.start();
        task2.start();
        task3.start();*//*
        *//*Thread thread1 = new Thread(new MyRunnableCass("Первая задача"));
        Thread thread2 = new Thread(new MyRunnableCass("Вторая задача"));
        Thread thread3 = new Thread(new MyRunnableCass("Третья задача"));
        thread1.start();
        thread2.start();
        thread3.start();*//*
        *//*Thread thread1 = new Thread(new Runnable() {
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
        thread3.start();*//*
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
}*/

/*
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
*/


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
