package com.aspect_programming.aspect_programming_demo;

import org.springframework.stereotype.Component;

@Component
public class Greet {

    public void show(){
        System.out.println("Show off");
    }

    public void show(String name){
        System.out.println("Show off: "+name);
    }
}
