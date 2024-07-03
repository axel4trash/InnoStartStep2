package edu.innotech.Task3;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

//Данная аннотация вешается на методы, которые изменяют состояние объекта значимым образом
public @interface Mutator {
    String [] methods4refresh() default "null";
}
