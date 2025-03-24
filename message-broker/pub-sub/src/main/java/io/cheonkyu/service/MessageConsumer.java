package io.cheonkyu.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageConsumer {

  private final RedisTemplate<String, String> redisTemplate;
  private static final String QUEUE_NAME = "messageQueue";

  @Scheduled(fixedDelay = 2000) // 2초마다 실행
  public void consumeMessage() {
    String message = redisTemplate.opsForList().rightPop(QUEUE_NAME);
    if (message != null) {
      System.out.println("메시지 처리: " + message);
    }
  }
}
