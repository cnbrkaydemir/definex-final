package com.cnbrkaydemir.tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScans({
		@ComponentScan("com.cnbrkaydemir.tasks.controller"),
		@ComponentScan("com.cnbrkaydemir.tasks.service"),
		@ComponentScan("com.cnbrkaydemir.tasks.config"),
		@ComponentScan("com.cnbrkaydemir.tasks.exception"),
})
@EnableJpaRepositories("com.cnbrkaydemir.tasks.repository")
@EnableTransactionManagement
@EnableCaching
public class TasksApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasksApplication.class, args);
	}

}
