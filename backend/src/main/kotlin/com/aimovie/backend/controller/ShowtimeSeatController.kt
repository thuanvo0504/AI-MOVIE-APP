package com.aimovie.backend.controller

import com.aimovie.backend.dto.SeatAvailabilityResponse
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/showtimes")
class ShowtimeSeatController(
    private val jdbcTemplate: JdbcTemplate
) {

    @GetMapping("/{showtimeId}/seats")
    fun getSeatsByShowtime(
        @PathVariable showtimeId: Long
    ): ResponseEntity<List<SeatAvailabilityResponse>> {

        val sql = """
            SELECT
                SS.SHOWTIME_SEAT_ID,
                SS.SHOWTIME_ID,

                S.SEAT_ID,
                S.ROW_NAME,
                S.SEAT_NUMBER,
                S.SEAT_TYPE_ID,

                SS.STATUS,

                P.PRICE

            FROM SHOWTIME_SEATS SS

            JOIN SEATS S
                ON S.SEAT_ID = SS.SEAT_ID

            LEFT JOIN SEAT_TYPE_PRICING P
                ON P.SHOWTIME_ID = SS.SHOWTIME_ID
                AND P.SEAT_TYPE_ID = S.SEAT_TYPE_ID

            WHERE SS.SHOWTIME_ID = ?

            ORDER BY
                S.ROW_NAME,
                S.SEAT_NUMBER
        """.trimIndent()


        val seats =
            jdbcTemplate.query(
                sql,
                { rs, _ ->

                    SeatAvailabilityResponse(

                        showtimeSeatId =
                            rs.getLong(
                                "SHOWTIME_SEAT_ID"
                            ),

                        showtimeId =
                            rs.getLong(
                                "SHOWTIME_ID"
                            ),

                        seatId =
                            rs.getLong(
                                "SEAT_ID"
                            ),

                        rowName =
                            rs.getString(
                                "ROW_NAME"
                            ),

                        seatNumber =
                            rs.getInt(
                                "SEAT_NUMBER"
                            ),

                        seatTypeId =
                            rs.getLong(
                                "SEAT_TYPE_ID"
                            ),

                        status =
                            rs.getString(
                                "STATUS"
                            ),

                        price =
                            rs.getDouble(
                                "PRICE"
                            )
                    )
                },

                showtimeId
            )


        return ResponseEntity.ok(
            seats
        )
    }
}