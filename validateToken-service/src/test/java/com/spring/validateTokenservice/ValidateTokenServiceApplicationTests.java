package com.spring.validateTokenservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application.properties")

class ValidateTokenServiceApplicationTests {

	@Value("${jwt.secret-key}")
	private String SecretKey;
	@Test
	void contextLoads() {
		System.out.println(SecretKey);
	}
	@Test
	public void main() {
		ValidateTokenServiceApplication.main(new String[] {});
	}

}
