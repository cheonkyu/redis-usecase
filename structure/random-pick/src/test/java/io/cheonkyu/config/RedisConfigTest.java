package io.cheonkyu.config;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisConfigTest {
  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Test
  void testStrings() {
    // given
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    String key = "stringKey";

    // when
    valueOperations.set(key, "hello");

    // then
    String value = valueOperations.get(key);
    assertEquals(value, "hello");
  }

  @Test
  void testSet() {
    // given
    SetOperations<String, String> setOperations = redisTemplate.opsForSet();
    String key = "setKey";

    // when
    setOperations.add(key, "h", "e", "l", "l", "o");

    // then
    Set<String> members = setOperations.members(key);
    Long size = setOperations.size(key);

    // assertThat(members).containsOnly("h", "e", "l", "o");
    assertEquals(size, 4);
  }

  @Test
  void testHash() {
    // given
    HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
    String key = "hashKey";

    // when
    hashOperations.put(key, "hello", "world");

    // then
    Object value = hashOperations.get(key, "hello");
    assertEquals(value, "world");

    Map<Object, Object> entries = hashOperations.entries(key);
    // assertEquals(entries.keySet(), "hello");
    // assertEquals(entries.values(), "world");

    Long size = hashOperations.size(key);
    assertEquals(size, entries.size());
  }
}
