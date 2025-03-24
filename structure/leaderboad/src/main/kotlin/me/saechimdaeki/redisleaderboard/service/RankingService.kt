package io.cheonkyu.redisleaderboard.service

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
class RankingService(
    private val redisTemplate: StringRedisTemplate
) {

    private val LEADER_BOARD = "leaderBoard"

    fun setUserScore(userId: String, score: Int) :Boolean {
        val zSet = redisTemplate.opsForZSet()
        zSet.add(LEADER_BOARD, userId, score.toDouble())

        return true
    }

    fun getUserRanking(userId: String) : Long {
        val zSet = redisTemplate.opsForZSet()
        val rank = zSet.rank(LEADER_BOARD, userId)
        return rank?: -1
    }

    fun getTopRank(limit : Long) : List<String> {
        val zSet = redisTemplate.opsForZSet()
        zSet.reverseRange(LEADER_BOARD, 0, limit - 1)?.let {
            return it.toList()
        }
        return listOf()
    }

}