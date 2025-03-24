package io.cheonkyu.redisleaderboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RedisLeaderBoardApplication

fun main(args: Array<String>) {
    runApplication<RedisLeaderBoardApplication>(*args)
}
