package com.devcci.devtoy.member.config;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Tag(TestConstants.INTEGRATION_TEST)
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles(TestConstants.INTEGRATION_TEST)
@Import(TestContainerConfig.class)
@SpringBootTest
public @interface IntegrationTest {

}
