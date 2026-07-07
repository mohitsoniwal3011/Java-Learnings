package com.aspect_programming.aspect_programming_demo;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;



//@Aspect
@Aspect
@Component
public class AspectDemo {

    @Before("execution(* com.aspect_programming.aspect_programming_demo.Greet.show(..))")
    public void log(){
        System.out.println("Call before method");
    }

    @After("within(com.aspect_programming.aspect_programming_demo.*)")
    public void logAgain(){
        System.out.println("Call After method");
    }

    @Around("execution(* com.aspect_programming.aspect_programming_demo.Greet.show(String))")
    public void logNumber(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("called before number");
        joinPoint.proceed();
        System.out.println("called after number");
    }
}


//        What is an Entity?
//
//        What is Persistence Context?
//
//        What are entity lifecycle states?
//
//        What is Dirty Checking?
//
//        What is First Level Cache?
//
//        Difference between persist() and merge().
//
//        save() vs persist().
//
//        LAZY vs EAGER fetching.
//
//        Cascade types.
//
//        orphanRemoval vs CascadeType.REMOVE.
//
//        OneToMany vs ManyToOne.
//
//        Why is @Transactional usually placed on the service layer?
//
//        Explain all propagation types with examples.
//
//        REQUIRED vs REQUIRES_NEW.
//
//        NESTED vs REQUIRES_NEW.





