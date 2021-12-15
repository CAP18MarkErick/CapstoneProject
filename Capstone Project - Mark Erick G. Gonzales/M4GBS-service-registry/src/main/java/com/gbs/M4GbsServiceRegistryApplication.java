package com.gbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
@EnableEurekaServer
public class M4GbsServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(M4GbsServiceRegistryApplication.class, args);
	}

}
