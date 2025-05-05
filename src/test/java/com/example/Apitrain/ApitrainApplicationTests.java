package com.example.Apitrain;

import com.example.Apitrain.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Configuration
class ApitrainApplicationTests {

	@Test
	void contextLoads() {
	}

}
