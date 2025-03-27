# 레디스

## 환경구성 open files 확인

maxclients 설정값은 10000

레디스 프로세스 내부적으로 사용하기 위해 예약한 파일 디스크립터 수는 32개

```
ulimit -a | grep open
```

/etc/security/limits.conf

## 환경구성 THP 비활성화

리눅스는 메모리를 페이지 단위로 관리, 기본 페이지는 4096바이트(4kb)로 고정

메모리 크기가 커질수록 페이지를 관리하는 테이블인 TLB의 크기도 커져 오버헤드가 발생하는 이슈로

페이지를 크게 만든 뒤 자동으로 관리하는 THP(Transparent Huge Page) 기능 도입

레디스와 같은 데이터베이스 애플리케이션에서는 THP를 사용할 때 퍼포먼스가 떨어지고 레이턴시가 올라가는 현상이 발생

다음 명령어로 비활성화

```sh
echo never > /sys/kernel/mm/transparent_hugpage/enabled
```

THP 비활성와 영구 적용

파일 : /etc/rc.local

```sh
if test -f /sys/kernel/mm/transparent_hugpage/enabled; then
  echo never > /sys/kernel/mm/transparent_hugpage/enabled
fi
```

## vm.overcommit_memory = 1로 변경

레디스는 디스크에 파일을 저장할 때 fork()를 이용해 벡그라운드 프로세스를 만듬 COW(Copy On Write) 매커니즘이 동작

부모 프로세스와 자식 프로세스가 동일한 메모리 페이지를 공유하다가 레디스의 데이터가 변경될 때마다 메모리 페이지를 복사하기 때문에 데이터 변경이 많이 발생하면 메모리 사용량이 빠르게 증가

레디스 프로세스가 실행되는 도중 메모리를 순간적으로 초과해 할당해야 하는 상황이 발생하기 때문에,
vm.overcommit_memory =1 로 설정

## redis cli

![cli](https://estuary.dev/static/4ff882a8a0e3d2ffe69114d10bc2ca58/6435f/02_Redis_Data_Types_Data_Types_51e474ec05.png)

- [EXISTS](https://redis.io/docs/latest/commands/exists/)
- [KEYS](https://redis.io/docs/latest/commands/keys/)

- [SCAN](https://redis.io/docs/latest/commands/scan/)

```
SCAN cursor [MATCH pattern] [COUNT count] [TYPE type]
```

레디스는 메모리를 스캔하여 데이터가 저장된 형상에 따라 몇 개의 키를 더 읽는 것이 효율적이라고 판단되면 1~2개의 키를 더 읽은 뒤 함께 반환하기도 함

SCAN, MATCH 옵션을 이요해서 필터링
