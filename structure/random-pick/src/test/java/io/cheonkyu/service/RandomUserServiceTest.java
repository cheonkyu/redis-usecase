package io.cheonkyu.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RandomUserServiceTest {
  @Autowired
  private RandomUserService randomUserService;

  @DisplayName("레디스에세 랜덤으로 회원키 조회")
  @Test
  void testGetRandomUser() {
    randomUserService.saveUsers();
    String key = randomUserService.getRandomUserKey();
    String key1 = randomUserService.getRandomUserKey();
    assertThat(key).contains("user");
    assertFalse(key.equals(key1));
  }
}
