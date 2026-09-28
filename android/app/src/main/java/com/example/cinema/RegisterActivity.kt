package com.example.cinema

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity :
    AppCompatActivity() {

    private lateinit var edtFullName:
            EditText

    private lateinit var edtEmail:
            EditText

    private lateinit var edtPhone:
            EditText

    private lateinit var edtPassword:
            EditText

    private lateinit var edtConfirmPassword:
            EditText

    private lateinit var btnRegister:
            Button


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_register
        )


        edtFullName =
            findViewById(
                R.id.edtFullName
            )

        edtEmail =
            findViewById(
                R.id.edtEmail
            )

        edtPhone =
            findViewById(
                R.id.edtPhone
            )

        edtPassword =
            findViewById(
                R.id.edtPassword
            )

        edtConfirmPassword =
            findViewById(
                R.id.edtConfirmPassword
            )

        btnRegister =
            findViewById(
                R.id.btnRegister
            )


        val tvBackLogin =
            findViewById<TextView>(
                R.id.tvBackLogin
            )


        btnRegister.setOnClickListener {

            validateAndRegister()
        }


        tvBackLogin.setOnClickListener {

            finish()
        }
    }


    private fun validateAndRegister() {

        val name =
            edtFullName
                .text
                .toString()
                .trim()

        val email =
            edtEmail
                .text
                .toString()
                .trim()

        val phone =
            edtPhone
                .text
                .toString()
                .trim()

        val password =
            edtPassword
                .text
                .toString()

        val confirmPassword =
            edtConfirmPassword
                .text
                .toString()


        if (name.isBlank()) {

            edtFullName.error =
                "Vui lòng nhập họ tên"

            edtFullName.requestFocus()

            return
        }


        if (
            email.isBlank() ||
            !Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {

            edtEmail.error =
                "Email không hợp lệ"

            edtEmail.requestFocus()

            return
        }


        if (password.length < 6) {

            edtPassword.error =
                "Mật khẩu phải có ít nhất 6 ký tự"

            edtPassword.requestFocus()

            return
        }


        if (password != confirmPassword) {

            edtConfirmPassword.error =
                "Mật khẩu xác nhận không khớp"

            edtConfirmPassword.requestFocus()

            return
        }


        registerAccount(
            name = name,
            email = email,
            phone = phone,
            password = password
        )
    }


    private fun registerAccount(
        name: String,
        email: String,
        phone: String,
        password: String
    ) {

        btnRegister.isEnabled =
            false

        btnRegister.text =
            "ĐANG ĐĂNG KÝ..."


        val request =
            RegisterRequest(
                name = name,
                email = email,
                password = password,
                phone =
                    phone.takeIf {
                        it.isNotBlank()
                    }
            )


        RetrofitClient
            .apiService
            .register(
                request
            )
            .enqueue(

                object :
                    Callback<RegisterResponse> {


                    override fun onResponse(
                        call:
                        Call<RegisterResponse>,

                        response:
                        Response<RegisterResponse>
                    ) {

                        restoreButton()


                        if (
                            response.isSuccessful
                        ) {

                            val result =
                                response.body()


                            Log.d(
                                "REGISTER_API",
                                "Đăng ký thành công userId=${result?.userId}"
                            )


                            Toast.makeText(
                                this@RegisterActivity,
                                result?.message
                                    ?: "Đăng ký thành công",
                                Toast.LENGTH_LONG
                            ).show()


                            // Quay về Login
                            finish()

                        } else {

                            val error =
                                response
                                    .errorBody()
                                    ?.string()


                            Log.e(
                                "REGISTER_API",
                                "HTTP ${response.code()} - $error"
                            )


                            val message =
                                when (
                                    response.code()
                                ) {

                                    409 ->
                                        "Email đã được sử dụng"

                                    400 ->
                                        "Thông tin đăng ký không hợp lệ"

                                    else ->
                                        "Đăng ký thất bại: HTTP ${response.code()}"
                                }


                            Toast.makeText(
                                this@RegisterActivity,
                                message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }


                    override fun onFailure(
                        call:
                        Call<RegisterResponse>,

                        t: Throwable
                    ) {

                        restoreButton()


                        Log.e(
                            "REGISTER_API",
                            "Lỗi kết nối",
                            t
                        )


                        Toast.makeText(
                            this@RegisterActivity,
                            "Không kết nối được server: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )
    }


    private fun restoreButton() {

        btnRegister.isEnabled =
            true

        btnRegister.text =
            "ĐĂNG KÝ"
    }
}