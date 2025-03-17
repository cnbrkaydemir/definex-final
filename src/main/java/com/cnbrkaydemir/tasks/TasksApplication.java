package com.cnbrkaydemir.tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScans({
		@ComponentScan("com.cnbrkaydemir.tasks.controller"),
		@ComponentScan("com.cnbrkaydemir.tasks.service"),
		@ComponentScan("com.cnbrkaydemir.tasks.config")
})
@EnableJpaRepositories("com.cnbrkaydemir.tasks.repository")
public class TasksApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasksApplication.class, args);
	}

}
