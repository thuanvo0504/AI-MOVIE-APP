package com.aimovie.backend.controller

import com.aimovie.backend.dto.AiChatRequest
import com.aimovie.backend.dto.AiChatResponse
import com.aimovie.backend.service.AiService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ai")
class AiController(
    private val aiService: AiService
) {

    private val log =
        LoggerFactory.getLogger(
            AiController::class.java
        )

    @PostMapping("/chat")
    fun chat(
        @RequestBody request: AiChatRequest
    ): ResponseEntity<AiChatResponse> {

        println("===================================")
        println("AI CONTROLLER - ĐÃ NHẬN REQUEST")
        println("MESSAGE = ${request.message}")
        println("===================================")

        return try {

            val reply =
                aiService.chat(
                    request.message
                )

            println(
                "AI CONTROLLER - THÀNH CÔNG"
            )

            ResponseEntity.ok(
                AiChatResponse(
                    reply = reply
                )
            )

        } catch (e: Exception) {

            println("===================================")
            println("AI CONTROLLER ERROR")
            println("TYPE = ${e.javaClass.name}")
            println("MESSAGE = ${e.message}")
            println("===================================")

            e.printStackTrace()

            log.error(
                "AI Controller lỗi",
                e
            )

            ResponseEntity
                .internalServerError()
                .body(
                    AiChatResponse(
                        reply =
                            "Backend AI lỗi: ${e.message}"
                    )
                )
        }
    }
}