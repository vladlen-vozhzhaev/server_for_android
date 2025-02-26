
public class Person {
    public  String name;
    private static int hp = 100;

    public Person(String name) {
        this.name = name;
    }


    public static int getHp() {
        return hp;
    }

    public static void setHp(int hp) {
        Person.hp = hp;
    }
}
