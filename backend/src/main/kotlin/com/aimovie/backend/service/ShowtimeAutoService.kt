package com.aimovie.backend.service

import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class ShowtimeAutoService(
    private val jdbcTemplate: JdbcTemplate
) {

    private val log =
        LoggerFactory.getLogger(
            ShowtimeAutoService::class.java
        )

    private val vietnamZone =
        ZoneId.of("Asia/Ho_Chi_Minh")


    // ==========================================
    // TEMPLATE SUẤT CHIẾU
    // ==========================================

    private data class TemplateShowtime(

        val showtimeId: Long,

        val movieId: Long,

        val roomId: Long,

        val startTime: LocalDateTime,

        val endTime: LocalDateTime,

        val basePrice: BigDecimal
    )


    // ==========================================
    // TEMPLATE GIÁ GHẾ
    // ==========================================

    private data class PriceTemplate(

        val seatTypeId: Long,

        val price: BigDecimal
    )


    // ==========================================
    // ID COUNTER
    //
    // Project hiện tại chạy 1 backend nên ta lấy
    // MAX(ID) + 1 để tránh phụ thuộc tên sequence.
    // ==========================================

    private data class IdCounters(

        var showtimeId: Long,

        var seatPricingId: Long,

        var showtimeSeatId: Long
    )


    // ==========================================
    // HÀM CHÍNH
    //
    // Luôn đảm bảo:
    //
    // Hôm nay
    // Ngày mai
    // Ngày kia
    //
    // có suất chiếu.
    // ==========================================

    @Transactional
    @Synchronized
    fun ensureRollingShowtimes() {

        val now =
            LocalDateTime.now(
                vietnamZone
            )

        val today =
            now.toLocalDate()


        log.info(
            "======================================"
        )

        log.info(
            "AUTO SHOWTIME - Bắt đầu kiểm tra"
        )

        log.info(
            "Thời gian hiện tại: {}",
            now
        )


        // ======================================
        // Tìm ngày có lịch chiếu đầy đủ nhất
        // để làm mẫu.
        // ======================================

        val templateDate =
            findBestTemplateDate()


        if (templateDate == null) {

            log.warn(
                "Không tìm thấy lịch chiếu mẫu trong SHOWTIMES"
            )

            return
        }


        log.info(
            "Ngày mẫu: {}",
            templateDate
        )


        val templates =
            loadTemplateShowtimes(
                templateDate
            )


        if (templates.isEmpty()) {

            log.warn(
                "Ngày mẫu không có suất chiếu"
            )

            return
        }


        log.info(
            "Số suất mẫu: {}",
            templates.size
        )


        // ======================================
        // Khởi tạo ID
        // ======================================

        val ids =
            IdCounters(

                showtimeId =
                    nextId(
                        "SHOWTIMES",
                        "SHOWTIME_ID"
                    ),

                seatPricingId =
                    nextId(
                        "SEAT_TYPE_PRICING",
                        "SEAT_TYPE_PRICING_ID"
                    ),

                showtimeSeatId =
                    nextId(
                        "SHOWTIME_SEATS",
                        "SHOWTIME_SEAT_ID"
                    )
            )


        // ======================================
        // Sinh lịch hôm nay + 2 ngày
        // ======================================

        for (dayOffset in 0..2) {

            val targetDate =
                today.plusDays(
                    dayOffset.toLong()
                )


            createShowtimesForDate(

                targetDate =
                    targetDate,

                today =
                    today,

                now =
                    now,

                templates =
                    templates,

                ids =
                    ids
            )
        }


        log.info(
            "AUTO SHOWTIME - Hoàn tất"
        )

        log.info(
            "======================================"
        )
    }


    // ==========================================
    // TÌM NGÀY LÀM MẪU
    //
    // Chọn ngày ACTIVE có nhiều suất nhất.
    // Nếu bằng nhau -> lấy ngày mới hơn.
    // ==========================================

    private fun findBestTemplateDate():
            LocalDate? {

        val sql =
            """
            SELECT SHOW_DATE
            FROM (
                SELECT
                    TRUNC(START_TIME) AS SHOW_DATE,
                    COUNT(*) AS TOTAL_SHOWTIMES
                FROM SHOWTIMES
                WHERE STATUS = 'ACTIVE'
                GROUP BY TRUNC(START_TIME)
                ORDER BY
                    TOTAL_SHOWTIMES DESC,
                    SHOW_DATE DESC
            )
            WHERE ROWNUM = 1
            """.trimIndent()


        val result =
            jdbcTemplate.query(
                sql
            ) { rs, _ ->

                rs.getDate(
                    "SHOW_DATE"
                ).toLocalDate()
            }


        return result.firstOrNull()
    }


    // ==========================================
    // ĐỌC TẤT CẢ SUẤT CỦA NGÀY MẪU
    // ==========================================

    private fun loadTemplateShowtimes(
        templateDate: LocalDate
    ): List<TemplateShowtime> {

        val sql =
            """
            SELECT
                SHOWTIME_ID,
                MOVIE_ID,
                ROOM_ID,
                START_TIME,
                END_TIME,
                BASE_PRICE
            FROM SHOWTIMES
            WHERE STATUS = 'ACTIVE'
              AND TRUNC(START_TIME) = ?
            ORDER BY START_TIME
            """.trimIndent()


        return jdbcTemplate.query(

            sql,

            { rs, _ ->

                TemplateShowtime(

                    showtimeId =
                        rs.getLong(
                            "SHOWTIME_ID"
                        ),

                    movieId =
                        rs.getLong(
                            "MOVIE_ID"
                        ),

                    roomId =
                        rs.getLong(
                            "ROOM_ID"
                        ),

                    startTime =
                        rs.getTimestamp(
                            "START_TIME"
                        )
                            .toLocalDateTime(),

                    endTime =
                        rs.getTimestamp(
                            "END_TIME"
                        )
                            .toLocalDateTime(),

                    basePrice =
                        rs.getBigDecimal(
                            "BASE_PRICE"
                        )
                )
            },

            Date.valueOf(
                templateDate
            )
        )
    }


    // ==========================================
    // SINH SUẤT CHIẾU CHO 1 NGÀY
    // ==========================================

    private fun createShowtimesForDate(

        targetDate: LocalDate,

        today: LocalDate,

        now: LocalDateTime,

        templates:
        List<TemplateShowtime>,

        ids: IdCounters
    ) {

        log.info(
            "Kiểm tra lịch ngày {}",
            targetDate
        )


        templates.forEach { template ->


            // ==================================
            // Giữ nguyên GIỜ của lịch mẫu
            // nhưng thay NGÀY.
            // ==================================

            val targetStart =
                LocalDateTime.of(

                    targetDate,

                    template
                        .startTime
                        .toLocalTime()
                )


            // ==================================
            // Nếu là hôm nay mà suất đã qua
            // thì không tạo nữa.
            // ==================================

            if (
                targetDate == today &&
                !targetStart.isAfter(now)
            ) {

                return@forEach
            }


            // ==================================
            // Không tạo trùng.
            // ==================================

            if (
                showtimeExists(

                    template.movieId,

                    template.roomId,

                    targetStart
                )
            ) {

                return@forEach
            }


            // ==================================
            // Giữ nguyên thời lượng phim.
            // ==================================

            val duration =
                Duration.between(

                    template.startTime,

                    template.endTime
                )


            val targetEnd =
                targetStart.plus(
                    duration
                )


            val newShowtimeId =
                ids.showtimeId++


            // ==================================
            // INSERT SHOWTIMES
            // ==================================

            jdbcTemplate.update(

                """
                INSERT INTO SHOWTIMES (
                    SHOWTIME_ID,
                    MOVIE_ID,
                    ROOM_ID,
                    START_TIME,
                    END_TIME,
                    BASE_PRICE,
                    CREATED_AT,
                    UPDATED_AT,
                    STATUS
                )
                VALUES (
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    CURRENT_TIMESTAMP,
                    CURRENT_TIMESTAMP,
                    'ACTIVE'
                )
                """.trimIndent(),

                newShowtimeId,

                template.movieId,

                template.roomId,

                Timestamp.valueOf(
                    targetStart
                ),

                Timestamp.valueOf(
                    targetEnd
                ),

                template.basePrice
            )


            log.info(
                "Tạo SHOWTIME {} - movie={} - {}",
                newShowtimeId,
                template.movieId,
                targetStart
            )


            // ==================================
            // TẠO GIÁ GHẾ
            // ==================================

            copySeatPricing(

                sourceShowtimeId =
                    template.showtimeId,

                newShowtimeId =
                    newShowtimeId,

                roomId =
                    template.roomId,

                basePrice =
                    template.basePrice,

                ids =
                    ids
            )


            // ==================================
            // TẠO SHOWTIME_SEATS
            // ==================================

            createShowtimeSeats(

                showtimeId =
                    newShowtimeId,

                roomId =
                    template.roomId,

                ids =
                    ids
            )
        }
    }


    // ==========================================
    // KIỂM TRA SUẤT ĐÃ TỒN TẠI
    // ==========================================

    private fun showtimeExists(

        movieId: Long,

        roomId: Long,

        startTime: LocalDateTime
    ): Boolean {

        val count =
            jdbcTemplate.queryForObject(

                """
                SELECT COUNT(*)
                FROM SHOWTIMES
                WHERE MOVIE_ID = ?
                  AND ROOM_ID = ?
                  AND START_TIME = ?
                  AND STATUS = 'ACTIVE'
                """.trimIndent(),

                Int::class.java,

                movieId,

                roomId,

                Timestamp.valueOf(
                    startTime
                )
            ) ?: 0


        return count > 0
    }


    // ==========================================
    // COPY GIÁ GHẾ TỪ SHOWTIME MẪU
    // ==========================================

    private fun copySeatPricing(

        sourceShowtimeId: Long,

        newShowtimeId: Long,

        roomId: Long,

        basePrice: BigDecimal,

        ids: IdCounters
    ) {

        val prices =
            jdbcTemplate.query(

                """
                SELECT
                    SEAT_TYPE_ID,
                    PRICE
                FROM SEAT_TYPE_PRICING
                WHERE SHOWTIME_ID = ?
                ORDER BY SEAT_TYPE_ID
                """.trimIndent(),

                { rs, _ ->

                    PriceTemplate(

                        seatTypeId =
                            rs.getLong(
                                "SEAT_TYPE_ID"
                            ),

                        price =
                            rs.getBigDecimal(
                                "PRICE"
                            )
                    )
                },

                sourceShowtimeId
            )


        // ======================================
        // Nếu suất mẫu có bảng giá
        // thì copy nguyên bảng giá.
        // ======================================

        if (prices.isNotEmpty()) {

            prices.forEach { price ->

                jdbcTemplate.update(

                    """
                    INSERT INTO SEAT_TYPE_PRICING (
                        SEAT_TYPE_PRICING_ID,
                        SHOWTIME_ID,
                        SEAT_TYPE_ID,
                        PRICE
                    )
                    VALUES (
                        ?,
                        ?,
                        ?,
                        ?
                    )
                    """.trimIndent(),

                    ids.seatPricingId++,

                    newShowtimeId,

                    price.seatTypeId,

                    price.price
                )
            }


            return
        }


        // ======================================
        // FALLBACK
        //
        // Nếu suất mẫu chưa có pricing
        // thì lấy các loại ghế của phòng.
        // ======================================

        val seatTypes =
            jdbcTemplate.query(

                """
                SELECT DISTINCT
                    SEAT_TYPE_ID
                FROM SEATS
                WHERE ROOM_ID = ?
                ORDER BY SEAT_TYPE_ID
                """.trimIndent(),

                { rs, _ ->

                    rs.getLong(
                        "SEAT_TYPE_ID"
                    )
                },

                roomId
            )


        seatTypes.forEach { seatTypeId ->

            val extra =
                when (seatTypeId) {

                    1L ->
                        BigDecimal.ZERO

                    2L ->
                        BigDecimal("30000")

                    3L ->
                        BigDecimal("50000")

                    else ->
                        BigDecimal.ZERO
                }


            val finalPrice =
                basePrice.add(
                    extra
                )


            jdbcTemplate.update(

                """
                INSERT INTO SEAT_TYPE_PRICING (
                    SEAT_TYPE_PRICING_ID,
                    SHOWTIME_ID,
                    SEAT_TYPE_ID,
                    PRICE
                )
                VALUES (
                    ?,
                    ?,
                    ?,
                    ?
                )
                """.trimIndent(),

                ids.seatPricingId++,

                newShowtimeId,

                seatTypeId,

                finalPrice
            )
        }
    }


    // ==========================================
    // TẠO TOÀN BỘ GHẾ CHO SUẤT CHIẾU
    // ==========================================

    private fun createShowtimeSeats(

        showtimeId: Long,

        roomId: Long,

        ids: IdCounters
    ) {

        val seatIds =
            jdbcTemplate.query(

                """
                SELECT
                    SEAT_ID
                FROM SEATS
                WHERE ROOM_ID = ?
                ORDER BY
                    ROW_NAME,
                    SEAT_NUMBER
                """.trimIndent(),

                { rs, _ ->

                    rs.getLong(
                        "SEAT_ID"
                    )
                },

                roomId
            )


        seatIds.forEach { seatId ->

            jdbcTemplate.update(

                """
                INSERT INTO SHOWTIME_SEATS (
                    SHOWTIME_SEAT_ID,
                    SHOWTIME_ID,
                    SEAT_ID,
                    STATUS,
                    HOLD_UNTIL,
                    BOOKED_AT,
                    HELD_AT
                )
                VALUES (
                    ?,
                    ?,
                    ?,
                    'AVAILABLE',
                    NULL,
                    NULL,
                    NULL
                )
                """.trimIndent(),

                ids.showtimeSeatId++,

                showtimeId,

                seatId
            )
        }


        log.info(
            "Showtime {} có {} ghế",
            showtimeId,
            seatIds.size
        )
    }


    // ==========================================
    // LẤY MAX(ID) + 1
    // ==========================================

    private fun nextId(

        tableName: String,

        columnName: String
    ): Long {

        return jdbcTemplate
            .queryForObject(

                """
                SELECT
                    NVL(MAX($columnName), 0) + 1
                FROM $tableName
                """.trimIndent(),

                Long::class.java
            )
            ?: 1L
    }
}