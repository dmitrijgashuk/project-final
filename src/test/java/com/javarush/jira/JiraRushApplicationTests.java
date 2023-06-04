package com.javarush.jira;

import com.javarush.jira.login.internal.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yaml")
class JiraRushApplicationTests {

	@Autowired
	UserRepository repository;

	@Test
	void contextLoads() {
		repository.findAll()
				.forEach(System.out::println);
	}
}
