package io.cheonkyu.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageProducer {

  private final RedisTemplate<String, String> redisTemplate;
  private static final String QUEUE_NAME = "messageQueue";

  public void sendMessage(String message) {
    redisTemplate.opsForList().leftPush(QUEUE_NAME, message);
    System.out.println("메시지 전송: " + message);
  }
}
