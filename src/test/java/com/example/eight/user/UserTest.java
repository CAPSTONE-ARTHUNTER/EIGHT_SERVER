/*
package com.example.eight.user;


import com.example.eight.user.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.*;

public class UserTest {

    @Test
    public void 유저_Builder_테스트(){

        //given
        String name = "test1";
        String email = "test1@gmail.com";
        double exp = 3.7;
        LocalDateTime createdAt = LocalDateTime.of(2023, 8,14, 13, 30, 45);
        LocalDateTime updatedAt = LocalDateTime.now();
        Role role = Role.USER;

        //when
        User user1 = User.builder()
                .name(name)
                .email(email)
                .exp(exp)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .role(role).build();

        //then
        assertThat(user1.getName()).isEqualTo(name);
        assertThat(user1.getEmail()).isEqualTo(email);
        assertThat(user1.getExp()).isEqualTo(exp);
        assertThat(user1.getCreatedAt()).isEqualTo(createdAt);
        assertThat(user1.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(user1.getRole()).isEqualTo(role);
    }
}
 */