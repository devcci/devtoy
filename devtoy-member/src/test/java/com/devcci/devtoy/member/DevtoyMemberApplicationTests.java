package com.devcci.devtoy.member;

import com.devcci.devtoy.member.config.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class DevtoyMemberApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testDatabaseConnection() {
        String query = "SELECT 1";
        Integer result = jdbcTemplate.queryForObject(query, Integer.class);
        assertThat(result).isEqualTo(1);
    }
}
