package io.cheonkyu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class DAUService {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  private static final String DAU_KEY_PREFIX = "DAU:";

  // 특정 날짜에 대한 DAU 기록
  public void recordUserActivity(String userId, String currentDate) {
    String key = DAU_KEY_PREFIX + currentDate;

    ValueOperations<String, String> ops = redisTemplate.opsForValue();
    ops.setBit(key, getUserBitPosition(userId), true); // 사용자 ID에 해당하는 비트 위치를 'true'로 설정
  }

  // 특정 날짜에 대한 DAU 계산
  public Long calculateDAU(String date) {
    String key = DAU_KEY_PREFIX + date;

    final Long result = bitcount(key);
    // 비트 맵에서 1로 설정된 비트들의 개수를 셈
    return result;
  }

  public Long bitcount(final String key) {
    return redisTemplate.execute(new RedisCallback<Long>() {

      @Override
      public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {

        return redisConnection.bitCount(key.getBytes());
      }
    });
  }

  public void eventDateByBitAnd(String date1, String date2, String resultDate) {
    String key1 = DAU_KEY_PREFIX + date1;
    String key2 = DAU_KEY_PREFIX + date2;
    String resultKey = DAU_KEY_PREFIX + resultDate;

    redisTemplate.execute(new RedisCallback<Long>() {
      @Override
      public Long doInRedis(RedisConnection connection) {
        // BITOP AND 연산을 수행하고 결과를 resultKey에 저장
        return connection.bitOp(RedisStringCommands.BitOperation.AND, resultKey.getBytes(), key1.getBytes(),
            key2.getBytes());
      }
    });
  }

  // 사용자 ID를 비트 위치로 변환하는 메서드
  private int getUserBitPosition(String userId) {
    // 예시: 사용자 ID를 hashCode로 변환하여 비트맵 상의 위치로 매핑
    return userId.hashCode() & Integer.MAX_VALUE; // 양의 정수로 변환
  }
}
