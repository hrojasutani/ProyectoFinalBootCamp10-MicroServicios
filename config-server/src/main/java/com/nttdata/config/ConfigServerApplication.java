package com.nttdata.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

	public static void main(String[] args) {
		// SpringApplication.run(ConfigServerApplication.class, args);
		SpringApplication.run(ConfigServerApplication.class, new String[]{"--server.port=8001"});
	}

}
