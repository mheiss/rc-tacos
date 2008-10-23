package my.example.annotation;

public class Greeting2 implements IGreeting {

    @Override
    public void sayHello() {
        System.out.println("Hello from the last greeting");
    }

}
