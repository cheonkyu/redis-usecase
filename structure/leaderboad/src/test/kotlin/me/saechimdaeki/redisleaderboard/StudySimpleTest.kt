package io.cheonkyu.redisleaderboard

import io.cheonkyu.redisleaderboard.service.RankingService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration
import java.time.Instant
import java.util.Collections
import kotlin.random.Random

@SpringBootTest
class StudySimpleTest @Autowired constructor(
    private val rankingService: RankingService
){

    @Test
    @DisplayName("단순 학습용 테스트")
    fun getRanks(){
        rankingService.getTopRank(1)

        var beforeTime = Instant.now()
        val userRanking = rankingService.getUserRanking("user_100")
        var elapsed = Duration.between(beforeTime, Instant.now())

        println(" ${(elapsed.nano)/ 1000000}  ms")


        beforeTime = Instant.now()
        val topRank = rankingService.getTopRank(10)
        elapsed = Duration.between(beforeTime, Instant.now())
        println(" ${(elapsed.nano)/ 1000000}  ms")

    }


    @Test
    fun insertScore() {
        for (i in 0 until 1000000) {
            // 테스트를 위해 Java의 random을 사용하자 ( kotlin random X )
            val score = ( Math.random() * 1000000 ).toInt()
            val userId = "user_$i"
            rankingService.setUserScore(userId, score)
        }
    }

    @Test
    @DisplayName("레디스를 사용하지 않고 성능 테스트")
    fun inMemorySortPerformance() {
        val list = mutableListOf<Int>()
        for (i in 0 until 1000000) {
            // 테스트를 위해 Java의 random을 사용하자 ( kotlin random X )
           val score = ( Math.random() * 1000000 ).toInt()
            list.add(score)
        }

        val beforeTime = Instant.now()
        list.sort()
        val elapsed = Duration.between(beforeTime, Instant.now())

        println(" ${(elapsed.nano)/ 1000000}  ms")

    }

}