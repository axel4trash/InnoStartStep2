package edu.innotech.Task3;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//состояние объекта
public class State {
    //мапа с ключом по методу, значения - список аргументов
    private final Map<Method, List<Object>> values = new HashMap<>();
    public State(){}

    public State (State oldState, Method method, Object[] args){
        values.putAll(oldState.values);
        values.put(method, Arrays.asList(args));
    }

    @Override
    public String toString() {
        return "State{" +
                "values=" + values +
                '}';
    }
}
