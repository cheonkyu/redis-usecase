# 레디스 구조적 활용

Sorted

## 랜덤 데이터 추출

관계형 데이터베이스에서 랜덤 데이터 추출을 사용할 때는 ORDER BY RAND() 함수 이용 하지만 데이터 건수가 1만 건 이상일 경우
쿼리 성능이 나빠져 부하가 많이 가게 됨

레디스에서는 O(1) 복잡도로 랜덤 데이터를 가져올 수 있음

- HASH - RANDFIELD
- SET - SRANDMEMBER
- SORTED SET - ZRANDMEMBER
