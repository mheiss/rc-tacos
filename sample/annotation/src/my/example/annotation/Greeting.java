package my.example.annotation;

public class Greeting extends BaseGreeting implements IGreeting {

    @Message(clazz=Greeting1.class)
    private IGreeting nextGreeting;
    
    @Override
    public void sayHello() {
        System.out.println("Hello from greeting");
        nextGreeting.sayHello();
    }
}
