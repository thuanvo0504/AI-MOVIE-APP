package com.aimovie.backend.service

import com.aimovie.backend.entity.Payment
import com.aimovie.backend.repository.BookingRepository
import com.aimovie.backend.repository.PaymentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val bookingRepository: BookingRepository
) {

    // ============================================================
    // DATA-LEVEL AUTHORIZATION
    // ============================================================

    fun findAllForUser(userId: Long): List<Payment> {
        return paymentRepository.findAll()
            .filter { payment ->
                val booking = bookingRepository.findById(payment.bookingId)
                    .orElse(null)

                booking?.userId == userId
            }
    }

    fun findByIdForUser(
        paymentId: Long,
        userId: Long
    ): Payment? {

        val payment = paymentRepository.findById(paymentId)
            .orElse(null)
            ?: return null

        val booking = bookingRepository.findById(payment.bookingId)
            .orElse(null)
            ?: return null

        return if (booking.userId == userId) {
            payment
        } else {
            null
        }
    }

    fun findByBookingIdForUser(
        bookingId: Long,
        userId: Long
    ): List<Payment> {

        val booking = bookingRepository.findById(bookingId)
            .orElse(null)
            ?: return emptyList()

        if (booking.userId != userId) {
            return emptyList()
        }

        return paymentRepository.findByBookingId(bookingId)
    }

    fun findByTransactionCodeForUser(
        transactionCode: String,
        userId: Long
    ): Payment? {

        val payment = paymentRepository
            .findByTransactionCode(transactionCode)
            ?: return null

        val booking = bookingRepository.findById(payment.bookingId)
            .orElse(null)
            ?: return null

        return if (booking.userId == userId) {
            payment
        } else {
            null
        }
    }

    // ============================================================
    // CREATE PAYMENT
    // ============================================================

    @Transactional
    fun createPayment(
        bookingId: Long,
        userId: Long,
        amount: BigDecimal,
        paymentMethod: String
    ): Payment {

        // 1. Kiểm tra Booking tồn tại
        val booking = bookingRepository.findById(bookingId)
            .orElseThrow {
                IllegalArgumentException("Booking not found")
            }

        // 2. DATA-LEVEL AUTHORIZATION
        // Booking phải thuộc về user đang đăng nhập
        if (booking.userId != userId) {
            throw IllegalStateException(
                "You are not allowed to pay for this booking"
            )
        }

        // 3. Booking phải ở trạng thái CONFIRMED
        if (booking.status != "CONFIRMED") {
            throw IllegalStateException(
                "Booking is not available for payment"
            )
        }

        // 4. Không cho thanh toán số tiền khác với Booking
        if (amount.compareTo(booking.totalAmount) != 0) {
            throw IllegalArgumentException(
                "Payment amount does not match booking total"
            )
        }

        // 5. Chỉ hỗ trợ MOCK ở giai đoạn hiện tại
        if (paymentMethod != "MOCK") {
            throw IllegalArgumentException(
                "Unsupported payment method"
            )
        }

        // 6. Không tạo payment SUCCESS thứ hai cho cùng Booking
        val existingSuccessfulPayment =
            paymentRepository.findByBookingId(bookingId)
                .firstOrNull { it.status == "SUCCESS" }

        if (existingSuccessfulPayment != null) {
            throw IllegalStateException(
                "Booking has already been paid"
            )
        }

        // 7. Mock payment thành công
        val payment = Payment().apply {
            this.bookingId = bookingId
            this.amount = booking.totalAmount
            this.paymentMethod = paymentMethod
            this.status = "SUCCESS"
            this.transactionCode = "TXN-${UUID.randomUUID()}"
            this.createdAt = LocalDateTime.now()
        }

        return paymentRepository.save(payment)
    }
}