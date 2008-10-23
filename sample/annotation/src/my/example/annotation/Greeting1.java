package my.example.annotation;

public class Greeting1 implements IGreeting {
    
    @Message(clazz=Greeting2.class)
    private IGreeting nextGreeting;
    
    public void sayHello() {
        System.out.println("Hello from greeting 1");
        nextGreeting.sayHello();
    }

}
