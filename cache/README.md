# redis cache

## 캐시 전략

### 읽기 전략 - look aside

![look aside](https://learn.microsoft.com/en-us/azure/architecture/patterns/_images/cache-aside-diagram.png)

### 쓰기 전략

1. write through
   레디스에 먼저 데이터를 쓴 후, db에 쓰기 요청
2. cache invalidation
   db 데이터 갱신 시, 레디스 캐시 삭제
3. write behind
   레디스에 캐시를 쓴 후, 일정 주기마다 db에 갱신

![](https://media2.dev.to/dynamic/image/width=1000,height=420,fit=cover,gravity=auto,format=auto/https%3A%2F%2Fdev-to-uploads.s3.amazonaws.com%2Fuploads%2Farticles%2Fujv2bb7y4lpzsxpp44ob.gif)

## 메모리 관리

`--maxmemory-policy allkeys-lru` 다음 옵션 적용
["redis-server", "--appendonly", "no", "--maxmemory", "500mb", "--maxmemory-policy", "allkeys-lru"]

https://redis.io/chat?q=maxmemory-policy&page=1

### Noeviction

레디스에 데이터가 가득 차더라도 임의로 데이터를 삭제하지 않고데이터를 저장할 수 없다는 에러를 반환

### LRU eviction

데이터가 가득 찼을 때 가장 최근에 사용 되지 않은 데이터부터 삭제하는 정책

- volatile-LRU: 만료 시간이 설정돼 있는 키에 한해서 LRU 방식으로 키 삭재
- allkeys-LRU: 모든 키에 대해 LRU 알고리즘으로 삭제 (레디스 공식 문서에서 추천)

### LFU eviction

데이터가 가득 찼을 때 가장 자주 사횽 되지 않은 데이터부터 삭제하는 정책

- volatile-LFU: 만료 시간이 설정돼 있는 키에 한해서 LFU 방식으로 키 삭재
- allkeys-LFU: 모든 키에 대해 LFU 알고리즘으로 삭제 (레디스 공식 문서에서 추천)

### Random eviction

레디스에 저장된 키 중 하나를 임의로 골라 삭제

- volatile-random : 만료 시간이 설정돼 있는 키에 한해 랜덤하게 키를 삭제
- allkeys-random : 모든 키에 대해 랜덤하게 키를 삭제

### volatile-TTL

만료 시간이 가장 작은 키를 삭제

## 캐시 스탬피드 현상

look aside 방식으로 레디스를 사용하고 있다고 가정

특정 키가 만료됐을때, 여러 개의 애플리케이션에서 바라보던 키가 레디스에서 만료돼 삭제된다면 서버들은 한꺼번에 데이터베이스에 가서 데이터를 읽어오는 과정을 거침 (중복 일기)

각 애플리케이션은 읽어온 데이터를 레디쓰에 쓰게 됨 (중복 쓰기)

![cache stampede](https://image.toast.com/aaaadh/real/2020/techblog/1%2831%29.png)

### 캐시 스탬피드 대응법 - simple

레디스가 실제로 만료되기 전에 랜덤한 확률로 데이터베이스에 접근해 데이터를 읽어와 캐시의 값을 갱신

```python
def fetch(key, expire_gap):
  ttl = redis.ttl(key)

  if ttl - (random() * expriy_gap) > 0:
    return redis.get(key)
  else:
    value = db.fetch(key)
    redis.set(value KEY_TTL)
    return value
```

### 캐시 스탬피드 대응법 - PER 알고리즘 (Probabilistic Early Recomputation)

```
currentTime - (timeToCompute * beta * log(rand())) > expiry
```

currentTime: 현재 남은 만료 시간
timeToCompute: 케시된 값을 다시 계산하는 데 걸리는 시간
beta: 기본적으로 1. 0보다 큰 값으로 설정 가능
rand(): 0과 1 사이의 랜덤 값을 반환하는 함수
expiry: 키를 재설정할 때 새로 놓어줄 만료 시간
