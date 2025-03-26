package io.cheonkyu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.stream.IntStream;
import java.util.Random;

@Service
public class RandomUserService {

  private static final String HASH_NAME = "user_data"; // Redis Hash Key

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  // 100명의 사용자 저장
  public void saveUsers() {
    IntStream.rangeClosed(1, 100).forEach(i -> {
      String userId = "user_" + i;
      String userName = "User" + i;
      redisTemplate.opsForHash().put(HASH_NAME, userId, userName);
    });
  }

  public String getRandomUserKey() {
    Object randomKey = redisTemplate.opsForHash().randomKey(HASH_NAME);
    return (randomKey != null) ? randomKey.toString() : "No users found";
  }

  // 랜덤 Key에 해당하는 Value 가져오기
  public String getRandomUser() {
    Object randomKey = redisTemplate.opsForHash().randomKey(HASH_NAME);
    if (randomKey == null) {
      return "No users found";
    }
    Object userName = redisTemplate.opsForHash().get(HASH_NAME, randomKey);
    return randomKey + " -> " + userName;
  }
}
