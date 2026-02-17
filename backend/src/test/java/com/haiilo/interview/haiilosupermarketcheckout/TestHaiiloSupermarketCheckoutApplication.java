package com.haiilo.interview.haiilosupermarketcheckout;

import org.springframework.boot.SpringApplication;

public class TestHaiiloSupermarketCheckoutApplication {

    public static void main(String[] args) {
        SpringApplication.from(HaiiloSupermarketCheckoutApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
