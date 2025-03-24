package io.cheonkyu.redisleaderboard.controller

import io.cheonkyu.redisleaderboard.service.RankingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class RankingApiController( private val rankingService: RankingService) {


    // 학습 편의상 get 으로 진행
    @GetMapping("/setscore")
    fun setScore(@RequestParam userId: String, @RequestParam score: Int) : Boolean {
        return rankingService.setUserScore(userId,score)
    }

    @GetMapping("/rank")
    fun getUserRank(@RequestParam userId: String) : Long {
        return rankingService.getUserRanking(userId)
    }

    @GetMapping("/top")
    fun getTopRank(@RequestParam limit: Long) : List<String> {
        return rankingService.getTopRank(limit)
    }

}