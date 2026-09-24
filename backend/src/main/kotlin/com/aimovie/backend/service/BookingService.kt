package com.aimovie.backend.service

import com.aimovie.backend.dto.ConfirmBookingRequest
import com.aimovie.backend.dto.ConfirmBookingResponse
import com.aimovie.backend.entity.Booking
import com.aimovie.backend.repository.BookingRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class BookingService(
    private val bookingRepository: BookingRepository,
    private val jdbcTemplate: JdbcTemplate
) {

    private val log =
        LoggerFactory.getLogger(
            BookingService::class.java
        )


    // =====================================================
    // CÁC HÀM BOOKING CŨ
    // =====================================================

    fun findAll(): List<Booking> {

        return bookingRepository.findAll()
    }


    fun findById(
        bookingId: Long
    ): Booking? {

        return bookingRepository
            .findById(bookingId)
            .orElse(null)
    }


    fun findByBookingCode(
        bookingCode: String
    ): Booking? {

        return bookingRepository
            .findAll()
            .firstOrNull {
                it.bookingCode == bookingCode
            }
    }


    fun createBooking(
        booking: Booking
    ): Booking {

        return bookingRepository.save(
            booking
        )
    }


    // =====================================================
    // DỮ LIỆU GHẾ NỘI BỘ
    // =====================================================

    private data class SeatInfo(

        val showtimeSeatId: Long,

        val status: String,

        val price: BigDecimal
    )


    // =====================================================
    // XÁC NHẬN ĐẶT VÉ + THANH TOÁN
    // =====================================================

    @Transactional
    fun confirmBooking(
        request: ConfirmBookingRequest
    ): ConfirmBookingResponse {

        // =================================================
        // STEP 1
        // =================================================

        log.info(
            "BOOKING STEP 1 - Bắt đầu confirmBooking"
        )


        // =================================================
        // KIỂM TRA REQUEST
        // =================================================

        if (request.userId <= 0) {

            throw IllegalArgumentException(
                "USER_ID không hợp lệ"
            )
        }


        if (request.showtimeId <= 0) {

            throw IllegalArgumentException(
                "SHOWTIME_ID không hợp lệ"
            )
        }


        if (
            request.showtimeSeatIds.isEmpty()
        ) {

            throw IllegalArgumentException(
                "Bạn chưa chọn ghế"
            )
        }


        val seatIds =
            request
                .showtimeSeatIds
                .distinct()


        if (
            seatIds.size !=
            request.showtimeSeatIds.size
        ) {

            throw IllegalArgumentException(
                "Danh sách ghế bị trùng"
            )
        }


        log.info(
            "BOOKING STEP 2 - userId={}, showtimeId={}, seats={}",
            request.userId,
            request.showtimeId,
            seatIds
        )


        // =================================================
        // KIỂM TRA PHƯƠNG THỨC THANH TOÁN
        // =================================================

        val paymentMethod =
            request
                .paymentMethod
                .trim()
                .uppercase()


        val allowedMethods =
            setOf(
                "MOCK",
                "CASH",
                "CARD",
                "E_WALLET"
            )


        if (
            paymentMethod !in allowedMethods
        ) {

            throw IllegalArgumentException(
                "Phương thức thanh toán không hợp lệ"
            )
        }


        // =================================================
        // TẠO ? CHO IN (...)
        // =================================================

        val placeholders =
            seatIds.joinToString(",") {
                "?"
            }


        // =================================================
        // SELECT GHẾ + KHÓA GHẾ
        // =================================================

        val sqlSeats =
            """
            SELECT
                SS.SHOWTIME_SEAT_ID,
                SS.STATUS,
                P.PRICE

            FROM SHOWTIME_SEATS SS

            JOIN SEATS S
                ON S.SEAT_ID =
                   SS.SEAT_ID

            JOIN SEAT_TYPE_PRICING P
                ON P.SHOWTIME_ID =
                   SS.SHOWTIME_ID

                AND P.SEAT_TYPE_ID =
                    S.SEAT_TYPE_ID

            WHERE SS.SHOWTIME_ID = ?

            AND SS.SHOWTIME_SEAT_ID
                IN ($placeholders)

            FOR UPDATE NOWAIT
            """.trimIndent()


        val args =
            mutableListOf<Any>(
                request.showtimeId
            )


        args.addAll(
            seatIds
        )


        log.info(
            "BOOKING STEP 3 - Chuẩn bị SELECT ghế FOR UPDATE NOWAIT"
        )


        val seats =
            try {

                jdbcTemplate.query(

                    sqlSeats,

                    { rs, _ ->

                        SeatInfo(

                            showtimeSeatId =
                                rs.getLong(
                                    "SHOWTIME_SEAT_ID"
                                ),

                            status =
                                rs.getString(
                                    "STATUS"
                                ),

                            price =
                                rs.getBigDecimal(
                                    "PRICE"
                                )
                        )
                    },

                    *args.toTypedArray()
                )

            } catch (
                e: DataAccessException
            ) {

                log.error(
                    "BOOKING ERROR - Không khóa/lấy được ghế: ${e.message}",
                    e
                )

                throw IllegalStateException(
                    "Ghế đang được xử lý hoặc bị khóa bởi giao dịch khác"
                )
            }


        // =================================================
        // STEP 4
        // =================================================

        log.info(
            "BOOKING STEP 4 - Lấy được {} ghế",
            seats.size
        )


        // =================================================
        // KIỂM TRA GHẾ CÓ THUỘC SUẤT KHÔNG
        // =================================================

        if (
            seats.size != seatIds.size
        ) {

            throw IllegalArgumentException(
                "Có ghế không thuộc suất chiếu này"
            )
        }


        // =================================================
        // KIỂM TRA GHẾ AVAILABLE
        // =================================================

        val unavailableSeats =
            seats.filter {

                !it.status.equals(
                    "AVAILABLE",
                    ignoreCase = true
                )
            }


        if (
            unavailableSeats.isNotEmpty()
        ) {

            throw IllegalStateException(
                "Một hoặc nhiều ghế đã được đặt"
            )
        }


        // =================================================
        // TÍNH TỔNG TIỀN TỪ ORACLE
        // =================================================

        val totalAmount =
            seats.fold(
                BigDecimal.ZERO
            ) { total, seat ->

                total + seat.price
            }


        log.info(
            "BOOKING STEP 5 - Tổng tiền={} - Chuẩn bị lấy SEQ_BOOKINGS",
            totalAmount
        )


        // =================================================
        // TẠO BOOKING ID
        // =================================================

        val bookingId =
            nextSequence(
                "SEQ_BOOKINGS"
            )


        val bookingCode =
            "BK" +
                    bookingId
                        .toString()
                        .padStart(
                            8,
                            '0'
                        )


        log.info(
            "BOOKING STEP 6 - bookingId={}, bookingCode={}",
            bookingId,
            bookingCode
        )


        // =================================================
        // TRẠNG THÁI PAYMENT / BOOKING
        // =================================================

        val paymentStatus =
            if (
                paymentMethod == "CASH"
            ) {

                "PENDING"

            } else {

                "SUCCESS"
            }


        val bookingStatus =
            if (
                paymentStatus == "SUCCESS"
            ) {

                "CONFIRMED"

            } else {

                "PENDING"
            }


        // =================================================
        // INSERT BOOKINGS
        // =================================================

        jdbcTemplate.update(
            """
            INSERT INTO BOOKINGS (
                BOOKING_ID,
                USER_ID,
                SHOWTIME_ID,
                TOTAL_AMOUNT,
                STATUS,
                CREATED_AT,
                BOOKING_CODE,
                UPDATED_AT
            )
            VALUES (
                ?,
                ?,
                ?,
                ?,
                ?,
                CURRENT_TIMESTAMP,
                ?,
                CURRENT_TIMESTAMP
            )
            """.trimIndent(),

            bookingId,
            request.userId,
            request.showtimeId,
            totalAmount,
            bookingStatus,
            bookingCode
        )


        log.info(
            "BOOKING STEP 7 - INSERT BOOKINGS xong"
        )


        // =================================================
        // INSERT BOOKING_SEATS
        // =================================================

        seats.forEach { seat ->

            val bookingSeatId =
                nextSequence(
                    "SEQ_BOOKING_SEATS"
                )


            jdbcTemplate.update(
                """
                INSERT INTO BOOKING_SEATS (
                    BOOKING_SEAT_ID,
                    BOOKING_ID,
                    SHOWTIME_SEAT_ID,
                    PRICE
                )
                VALUES (
                    ?,
                    ?,
                    ?,
                    ?
                )
                """.trimIndent(),

                bookingSeatId,
                bookingId,
                seat.showtimeSeatId,
                seat.price
            )
        }


        log.info(
            "BOOKING STEP 8 - INSERT BOOKING_SEATS xong"
        )


        // =================================================
        // UPDATE SHOWTIME_SEATS -> BOOKED
        // =================================================

        seats.forEach { seat ->

            val updated =
                jdbcTemplate.update(
                    """
                    UPDATE SHOWTIME_SEATS
                    SET
                        STATUS = 'BOOKED',
                        BOOKED_AT =
                            CURRENT_TIMESTAMP,
                        HELD_AT = NULL,
                        HOLD_UNTIL = NULL
                    WHERE SHOWTIME_SEAT_ID = ?
                    AND STATUS = 'AVAILABLE'
                    """.trimIndent(),

                    seat.showtimeSeatId
                )


            if (
                updated != 1
            ) {

                throw IllegalStateException(
                    "Ghế ${seat.showtimeSeatId} vừa được người khác đặt"
                )
            }
        }


        log.info(
            "BOOKING STEP 9 - UPDATE SHOWTIME_SEATS thành BOOKED xong"
        )


        // =================================================
        // TẠO PAYMENT ID
        // =================================================

        val paymentId =
            nextSequence(
                "SEQ_PAYMENTS"
            )


        val transactionCode =
            "TX" +
                    paymentId
                        .toString()
                        .padStart(
                            8,
                            '0'
                        )


        // =================================================
        // INSERT PAYMENTS
        // =================================================

        jdbcTemplate.update(
            """
            INSERT INTO PAYMENTS (
                PAYMENT_ID,
                BOOKING_ID,
                AMOUNT,
                PAYMENT_METHOD,
                STATUS,
                TRANSACTION_CODE,
                CREATED_AT
            )
            VALUES (
                ?,
                ?,
                ?,
                ?,
                ?,
                ?,
                CURRENT_TIMESTAMP
            )
            """.trimIndent(),

            paymentId,
            bookingId,
            totalAmount,
            paymentMethod,
            paymentStatus,
            transactionCode
        )


        log.info(
            "BOOKING STEP 10 - INSERT PAYMENTS xong"
        )


        // =================================================
        // HOÀN TẤT
        // =================================================

        log.info(
            """
            BOOKING STEP 11 - HOÀN TẤT
            bookingId={}
            bookingCode={}
            paymentId={}
            transactionCode={}
            total={}
            bookingStatus={}
            paymentStatus={}
            """.trimIndent(),

            bookingId,
            bookingCode,
            paymentId,
            transactionCode,
            totalAmount,
            bookingStatus,
            paymentStatus
        )


        return ConfirmBookingResponse(

            bookingId =
                bookingId,

            bookingCode =
                bookingCode,

            paymentId =
                paymentId,

            transactionCode =
                transactionCode,

            totalAmount =
                totalAmount,

            bookingStatus =
                bookingStatus,

            paymentStatus =
                paymentStatus
        )
    }


    // =====================================================
    // LẤY NEXTVAL CỦA SEQUENCE
    // =====================================================

    private fun nextSequence(
        sequenceName: String
    ): Long {

        log.info(
            "Lấy NEXTVAL của {}",
            sequenceName
        )


        return jdbcTemplate
            .queryForObject(
                """
                SELECT $sequenceName.NEXTVAL
                FROM DUAL
                """.trimIndent(),

                Long::class.java
            )
            ?: throw IllegalStateException(
                "Không lấy được sequence $sequenceName"
            )
    }
}