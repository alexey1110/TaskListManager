package com.example.taskListManager;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.example.taskListManager.Repository")
@SpringBootApplication
public class TaskListManagerApplication {
	@Autowired
	private HikariDataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(TaskListManagerApplication.class, args);
	}

	@PreDestroy
	public void shutdown() {
		if (dataSource != null) {
			dataSource.close();
		}
	}
}
