package com.aimovie.backend.service

import tools.jackson.databind.JsonNode
import tools.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate

@Service
class AiService(

    private val jdbcTemplate: JdbcTemplate,

    private val objectMapper: ObjectMapper,

    @Value("\${openai.api-key}")
    private val apiKey: String

) {

    private val log =
        LoggerFactory.getLogger(
            AiService::class.java
        )


    private val restTemplate =
        RestTemplate()


    // ==========================================
    // AI CHAT
    // ==========================================

    fun chat(
        message: String
    ): String {

        println(
            "AI SERVICE - ĐÃ VÀO CHAT"
        )

        println(
            "MESSAGE = $message"
        )


        if (message.isBlank()) {

            return "Bạn hãy nhập câu hỏi hoặc sở thích phim nhé."
        }


        // ==========================================
        // LẤY DANH SÁCH PHIM TỪ ORACLE
        // ==========================================

        val movies =
            jdbcTemplate.query(

                """
                SELECT
                    MOVIE_ID,
                    TITLE,
                    GENRE,
                    DURATION_MINUTES,
                    AGE_RATING,
                    LANGUAGE,
                    STATUS

                FROM MOVIES

                ORDER BY MOVIE_ID
                """.trimIndent()

            ) { rs, _ ->

                """
                ID: ${rs.getLong("MOVIE_ID")}
                Tên: ${rs.getString("TITLE")}
                Thể loại: ${rs.getString("GENRE") ?: "Chưa cập nhật"}
                Thời lượng: ${rs.getInt("DURATION_MINUTES")} phút
                Phân loại: ${rs.getString("AGE_RATING") ?: "Chưa cập nhật"}
                Ngôn ngữ: ${rs.getString("LANGUAGE") ?: "Chưa cập nhật"}
                Trạng thái: ${rs.getString("STATUS")}
                """.trimIndent()
            }


        println(
            "AI SERVICE - LẤY ĐƯỢC ${movies.size} PHIM"
        )


        val movieContext =
            movies.joinToString(
                separator = "\n\n"
            )


        // ==========================================
        // SYSTEM INSTRUCTIONS
        // ==========================================

        val instructions =
            """
            Bạn là trợ lý AI của ứng dụng AI Movie Cinema.

            Nhiệm vụ của bạn:
            - Hỗ trợ người dùng tìm và lựa chọn phim.
            - Trả lời bằng tiếng Việt.
            - Trả lời ngắn gọn, tự nhiên và dễ hiểu.
            - Khi gợi ý phim, CHỈ được sử dụng phim nằm trong danh sách phim được cung cấp bên dưới.
            - Tuyệt đối không tự tạo hoặc bịa tên phim.
            - Nếu không có phim phù hợp với yêu cầu, hãy nói rõ hiện chưa có phim phù hợp.
            - Có thể hướng dẫn người dùng cách đặt vé, chọn suất chiếu, chọn ghế và thanh toán.
            - Khi gợi ý phim, hãy giải thích ngắn gọn lý do lựa chọn.
            - Ưu tiên phim có trạng thái phù hợp để người dùng xem hoặc đặt vé.

            DANH SÁCH PHIM TRONG HỆ THỐNG:

            $movieContext
            """.trimIndent()


        // ==========================================
        // REQUEST BODY
        // ==========================================

        val body =
            mapOf(
                "model" to "gpt-6-luna",
                "instructions" to instructions,
                "input" to message
            )


        // ==========================================
        // HTTP HEADERS
        // ==========================================

        val headers =
            HttpHeaders()

        headers.contentType =
            MediaType.APPLICATION_JSON

        headers.setBearerAuth(
            apiKey
        )


        val entity =
            HttpEntity(
                body,
                headers
            )


        // ==========================================
        // GỌI OPENAI
        // ==========================================

        println(
            "AI SERVICE - CHUẨN BỊ GỌI OPENAI"
        )


        val response =
            try {

                restTemplate.postForEntity(
                    "https://api.openai.com/v1/responses",
                    entity,
                    String::class.java
                )

            } catch (
                e: HttpStatusCodeException
            ) {

                println(
                    "========== OPENAI ERROR =========="
                )

                println(
                    "STATUS = ${e.statusCode}"
                )

                println(
                    "BODY = ${e.responseBodyAsString}"
                )

                println(
                    "=================================="
                )


                log.error(
                    "OPENAI STATUS = ${e.statusCode}"
                )

                log.error(
                    "OPENAI BODY = ${e.responseBodyAsString}"
                )


                throw IllegalStateException(
                    "OpenAI API lỗi: ${e.statusCode}"
                )

            } catch (
                e: Exception
            ) {

                println(
                    "========== OPENAI CONNECTION ERROR =========="
                )

                println(
                    "TYPE = ${e.javaClass.name}"
                )

                println(
                    "MESSAGE = ${e.message}"
                )

                println(
                    "================================================"
                )


                log.error(
                    "OPENAI CONNECTION ERROR",
                    e
                )


                throw e
            }


        // ==========================================
        // RESPONSE
        // ==========================================

        println(
            "AI SERVICE - OPENAI TRẢ RESPONSE"
        )


        val responseBody =
            response.body
                ?: return "AI không trả về dữ liệu."


        // Không in toàn bộ response để terminal đỡ rối
        log.debug(
            "OpenAI response nhận thành công"
        )


        return extractText(
            responseBody
        )
    }


    // ==========================================
    // LẤY TEXT TỪ OPENAI RESPONSE
    // ==========================================

    private fun extractText(
        json: String
    ): String {

        val root: JsonNode =
            objectMapper.readTree(
                json
            )


        val output =
            root.path(
                "output"
            )


        if (output.isArray) {

            for (item in output) {

                val content =
                    item.path(
                        "content"
                    )


                if (content.isArray) {

                    for (part in content) {

                        val type =
                            part
                                .path("type")
                                .asText()


                        if (
                            type == "output_text"
                        ) {

                            val text =
                                part
                                    .path("text")
                                    .asText()


                            if (
                                text.isNotBlank()
                            ) {

                                println(
                                    "AI SERVICE - ĐỌC RESPONSE THÀNH CÔNG"
                                )

                                return text
                            }
                        }
                    }
                }
            }
        }


        println(
            "AI SERVICE - KHÔNG TÌM THẤY OUTPUT_TEXT"
        )


        return "AI chưa thể tạo câu trả lời."
    }
}