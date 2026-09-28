package com.example.cinema

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_login
        )


        // ==========================================
        // ÁNH XẠ VIEW
        // ==========================================

        val email =
            findViewById<EditText>(
                R.id.edtEmail
            )

        val password =
            findViewById<EditText>(
                R.id.edtPassword
            )

        val login =
            findViewById<Button>(
                R.id.btnLogin
            )

        val register =
            findViewById<TextView>(
                R.id.tvRegister
            )


        // ==========================================
        // NÚT ĐĂNG NHẬP
        // ==========================================

        login.setOnClickListener {

            val emailText =
                email.text
                    .toString()
                    .trim()

            val passwordText =
                password.text
                    .toString()


            if (emailText.isEmpty()) {

                email.error =
                    "Vui lòng nhập email"

                email.requestFocus()

                return@setOnClickListener
            }


            if (passwordText.isEmpty()) {

                password.error =
                    "Vui lòng nhập mật khẩu"

                password.requestFocus()

                return@setOnClickListener
            }


            loginUser(
                emailText,
                passwordText
            )
        }


        // ==========================================
        // CHUYỂN SANG ĐĂNG KÝ
        // ==========================================

        register.setOnClickListener {

            val intent =
                Intent(
                    this,
                    RegisterActivity::class.java
                )

            startActivity(
                intent
            )
        }
    }


    // ==============================================
    // GỌI API ĐĂNG NHẬP
    // ==============================================

    private fun loginUser(
        email: String,
        password: String
    ) {

        val request =
            LoginRequest(
                email = email,
                password = password
            )


        RetrofitClient
            .apiService
            .login(
                request
            )
            .enqueue(

                object :
                    Callback<LoginResponse> {


                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {

                        // ==================================
                        // LOGIN THÀNH CÔNG
                        // ==================================

                        if (response.isSuccessful) {

                            val result =
                                response.body()


                            if (result != null) {

                                saveLoginData(
                                    result
                                )


                                Log.d(
                                    "LOGIN_TEST",
                                    "Đăng nhập thành công - token đã được lưu"
                                )


                                Toast.makeText(
                                    this@LoginActivity,
                                    "Đăng nhập thành công",
                                    Toast.LENGTH_SHORT
                                ).show()


                                val intent =
                                    Intent(
                                        this@LoginActivity,
                                        HomeActivity::class.java
                                    )


                                // Xóa Login khỏi stack
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK


                                startActivity(
                                    intent
                                )


                                finish()

                            } else {

                                Toast.makeText(
                                    this@LoginActivity,
                                    "Server trả về dữ liệu rỗng",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }

                        // ==================================
                        // SAI EMAIL / PASSWORD
                        // ==================================

                        else if (
                            response.code() == 401
                        ) {

                            Toast.makeText(
                                this@LoginActivity,
                                "Email hoặc mật khẩu không đúng",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        // ==================================
                        // LỖI KHÁC
                        // ==================================

                        else {

                            val error =
                                response
                                    .errorBody()
                                    ?.string()


                            Log.e(
                                "LOGIN_TEST",
                                "HTTP ${response.code()} - $error"
                            )


                            Toast.makeText(
                                this@LoginActivity,
                                "Lỗi server: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                    // ======================================
                    // KHÔNG KẾT NỐI ĐƯỢC BACKEND
                    // ======================================

                    override fun onFailure(
                        call: Call<LoginResponse>,
                        t: Throwable
                    ) {

                        Toast.makeText(
                            this@LoginActivity,
                            "Không kết nối được server: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()


                        Log.e(
                            "LOGIN_TEST",
                            "Lỗi kết nối",
                            t
                        )
                    }
                }
            )
    }


    // ==============================================
    // LƯU TOKEN + THÔNG TIN USER
    // ==============================================

    private fun saveLoginData(
        result: LoginResponse
    ) {

        val prefs =
            getSharedPreferences(
                "auth",
                MODE_PRIVATE
            )


        prefs
            .edit()

            .putString(
                "token",
                result.token
            )

            .putLong(
                "userId",
                result.userId
            )

            .putString(
                "fullName",
                result.fullName
            )

            .putString(
                "email",
                result.email
            )

            .putString(
                "role",
                result.role
            )

            .apply()
    }
}