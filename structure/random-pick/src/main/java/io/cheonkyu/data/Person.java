package io.cheonkyu.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

@RedisHash(value = "people", timeToLive = 6000) // 6000초
@Data
public class Person {
  @Id
  String id;
  String firstname;
  String lastname;
}