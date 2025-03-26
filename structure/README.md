# 레디스 구조적 활용

Sorted

## 랜덤 데이터 추출

관계형 데이터베이스에서 랜덤 데이터 추출을 사용할 때는 ORDER BY RAND() 함수 이용 하지만 데이터 건수가 1만 건 이상일 경우
쿼리 성능이 나빠져 부하가 많이 가게 됨

레디스에서는 O(1) 복잡도로 랜덤 데이터를 가져올 수 있음

- HASH - RANDFIELD
- SET - SRANDMEMBER
- SORTED SET - ZRANDMEMBER

## DAU 구하기

레디스의 비트맵을 이용

![DAU](https://spoolblog.wordpress.com/wp-content/uploads/2011/11/union.png)

1천만명의 DAU를 측정하는 법

`redisTemplate`에서 `bitcount` 지원하지 않아 커스텀을 해줘야함

```java
  public Long bitcount(final String key) {
    return redisTemplate.execute(new RedisCallback<Long>() {

      @Override
      public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {

        return redisConnection.bitCount(key.getBytes());
      }
    });
  }
```
