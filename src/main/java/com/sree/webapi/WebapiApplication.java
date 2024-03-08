package com.sree.webapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebapiApplication {

	public static void main(String[] args) {
              /** System.getenv().forEach((key,value)->{
                     System.out.printf("KEY: %s - Value %s %n", key, value);
                });**/
               
		SpringApplication.run(WebapiApplication.class, args);
	}

}
