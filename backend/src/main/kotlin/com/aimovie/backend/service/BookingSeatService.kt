package com.aimovie.backend.service

import com.aimovie.backend.entity.BookingSeat
import com.aimovie.backend.repository.BookingSeatRepository
import org.springframework.stereotype.Service

@Service
class BookingSeatService(
    private val bookingSeatRepository: BookingSeatRepository
) {

    fun findAll(): List<BookingSeat> {
        return bookingSeatRepository.findAll()
    }

    fun findById(bookingSeatId: Long): BookingSeat? {
        return bookingSeatRepository.findById(bookingSeatId).orElse(null)
    }

    fun findByBookingId(bookingId: Long): List<BookingSeat> {
        return bookingSeatRepository.findByBookingId(bookingId)
    }
}