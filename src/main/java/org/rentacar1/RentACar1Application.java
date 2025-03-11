package org.rentacar1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.rentacar1.app")
public class RentACar1Application {

    public static void main(String[] args) {

        SpringApplication.run(RentACar1Application.class, args);
    }

}
