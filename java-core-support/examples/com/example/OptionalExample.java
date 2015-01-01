package com.example;

import java.util.Optional;
import java.util.Properties;

public class OptionalExample {

    Properties DEFAULT_PROPERTIES = new Properties();
    
    public void test(){
        test(Optional.of(1));
    }
    
    public void test(Optional<Integer> port){
        
        if (port.isPresent()) {
            System.out.println(port.get());
        }
        port.ifPresent(l -> System.out.println(l));
        port.ifPresent(System.out::println);
        
        System.out.println(port.orElse(8080));
        System.out.println(port.orElseGet(() -> Integer.parseInt(DEFAULT_PROPERTIES.getProperty("port"))));
        System.out.println(port.orElseThrow(() -> new IllegalArgumentException("port is required")));
        
        
        Optional<Integer> systemPort = port.filter(i -> i<1024);
        Optional<String> asString = port.map(i -> String.valueOf(i));
        Optional<Integer> orDefault = port.flatMap(i -> getOptionalDefault("port"));
        Optional<Integer> orOtherDefault = port.flatMap(i -> Optional.ofNullable(System.getProperty("default.port")).map(s -> Integer.parseInt(s)));
        
    }
    
    private Optional<Integer> getOptionalDefault(String property){
        String str = DEFAULT_PROPERTIES.getProperty(property);
        if (str == null){
            return Optional.empty();
        } else {
            return Optional.of(Integer.parseInt(str));
        }
    }
    
}
