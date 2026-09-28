package com.example.cinema

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_account)

        val btnEditProfile =
            findViewById<Button>(
                R.id.btnEditProfile
            )

        val btnMyTickets =
            findViewById<Button>(
                R.id.btnMyTickets
            )

        val btnSettings =
            findViewById<Button>(
                R.id.btnSettings
            )

        val btnLogout =
            findViewById<Button>(
                R.id.btnLogout
            )

        // CHỈNH SỬA THÔNG TIN

        btnEditProfile.setOnClickListener {

            Toast.makeText(
                this,
                "Chức năng chỉnh sửa thông tin",
                Toast.LENGTH_SHORT
            ).show()
        }

        // VÉ CỦA TÔI

        btnMyTickets.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    TicketActivity::class.java
                )
            )
        }

        // CÀI ĐẶT

        btnSettings.setOnClickListener {

            Toast.makeText(
                this,
                "Chức năng cài đặt",
                Toast.LENGTH_SHORT
            ).show()
        }

        // ĐĂNG XUẤT

        btnLogout.setOnClickListener {

            Toast.makeText(
                this,
                "Đã đăng xuất",
                Toast.LENGTH_SHORT
            ).show()

            val intent =
                Intent(
                    this,
                    LoginActivity::class.java
                )

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            finish()
        }
    }
}