package com.aimovie.backend.service

import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ShowtimeScheduler(
    private val showtimeAutoService:
    ShowtimeAutoService
) {

    private val log =
        LoggerFactory.getLogger(
            ShowtimeScheduler::class.java
        )


    // ==========================================
    // CHẠY NGAY KHI BACKEND KHỞI ĐỘNG
    // ==========================================

    @EventListener(
        ApplicationReadyEvent::class
    )
    fun createShowtimesOnStartup() {

        log.info(
            "AUTO SHOWTIME - Backend vừa khởi động"
        )

        try {

            showtimeAutoService
                .ensureRollingShowtimes()

        } catch (e: Exception) {

            log.error(
                "AUTO SHOWTIME startup lỗi",
                e
            )
        }
    }


    // ==========================================
    // TỰ CHẠY MỖI NGÀY LÚC 00:05
    //
    // zone = giờ Việt Nam
    // ==========================================

    @Scheduled(
        cron = "0 5 0 * * *",
        zone = "Asia/Ho_Chi_Minh"
    )
    fun createShowtimesEveryDay() {

        log.info(
            "AUTO SHOWTIME - Đến lịch chạy hằng ngày"
        )

        try {

            showtimeAutoService
                .ensureRollingShowtimes()

        } catch (e: Exception) {

            log.error(
                "AUTO SHOWTIME daily lỗi",
                e
            )
        }
    }
}