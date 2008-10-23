package my.example.annotation.test;

import java.util.ArrayList;
import java.util.List;

import my.example.annotation.AnnotationResolver;
import my.example.annotation.Greeting;
import my.example.annotation.Message;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the reflection and annotation examples
 * 
 * @author mheiss
 * 
 */
public class TestGreeting {

    @Before 
    public void setup() {
        System.out.println("--------------");
    }
    
    @Test(expected = NullPointerException.class)
    public void testGreeting() {
        Greeting greeting = new Greeting();
        greeting.sayHello();
    }

    @Test
    public void testWithGreeting() throws Exception {
        // create a new instance of greeting
        Greeting greeting = new Greeting();
        
        //the list of all messages
        List<Object> resolved = new ArrayList<Object>();

        // check for annotations
        AnnotationResolver resolver = new AnnotationResolver(Message.class);
        resolver.resolveAnnotation(resolved,greeting);
        
        greeting.sayHello();
        
        for(Object o:resolved) {
            System.out.println(o.getClass().getName());
        }
    }

}
