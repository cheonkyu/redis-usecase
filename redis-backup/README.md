# 백업

![backup](https://dfordebugging.wordpress.com/wp-content/uploads/2023/02/redis-v2-separate-06.jpg)

## RDB (스냅샷)

레디스 메모리 상에 있는 키,벨류를 rdb파일에 저장

### RDB Restoring

dump.rdb 파일을 restore하기 위해 단순히 Redis의 data dir에 넣고 Redis를 재시작
Data dir 위치는 redis.conf 또는 redis-cli에서 congfig get dir 명령어로 찾음

## AOF (Append Only File)

쓰기 명령에 대한 모든 로그를 남김

### AOF Restoring

flushall 명령어로 복원
