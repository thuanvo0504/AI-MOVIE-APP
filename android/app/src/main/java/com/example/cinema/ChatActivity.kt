package com.example.cinema

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity() {

    private lateinit var chatContainer: LinearLayout
    private lateinit var scrollChat: ScrollView
    private lateinit var edtMessage: EditText

    private lateinit var btnSend: Button
    private lateinit var btnAction: Button
    private lateinit var btnComedy: Button
    private lateinit var btnRomance: Button

    private var loadingMessageView: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)


        // ==========================================
        // ÁNH XẠ VIEW
        // ==========================================

        chatContainer =
            findViewById(R.id.chatContainer)

        scrollChat =
            findViewById(R.id.scrollChat)

        edtMessage =
            findViewById(R.id.edtMessage)

        btnSend =
            findViewById(R.id.btnSend)

        btnAction =
            findViewById(R.id.btnAction)

        btnComedy =
            findViewById(R.id.btnComedy)

        btnRomance =
            findViewById(R.id.btnRomance)


        val btnBack =
            findViewById<TextView>(R.id.btnBack)


        // ==========================================
        // QUAY LẠI
        // ==========================================

        btnBack.setOnClickListener {

            finish()
        }


        // ==========================================
        // NÚT GỬI
        // ==========================================

        btnSend.setOnClickListener {

            val message =
                edtMessage
                    .text
                    .toString()
                    .trim()


            if (message.isNotEmpty()) {

                addUserMessage(message)

                edtMessage
                    .text
                    .clear()

                sendMessageToAi(message)
            }
        }


        // ==========================================
        // NÚT PHIM HÀNH ĐỘNG
        // ==========================================

        btnAction.setOnClickListener {

            sendQuickMessage(
                "Tôi muốn xem phim hành động"
            )
        }


        // ==========================================
        // NÚT PHIM HÀI
        // ==========================================

        btnComedy.setOnClickListener {

            sendQuickMessage(
                "Tôi muốn xem phim hài"
            )
        }


        // ==========================================
        // NÚT PHIM TÌNH CẢM
        // ==========================================

        btnRomance.setOnClickListener {

            sendQuickMessage(
                "Tôi muốn xem phim tình cảm"
            )
        }
    }


    // ==============================================
    // GỬI NHANH
    // ==============================================

    private fun sendQuickMessage(
        message: String
    ) {

        if (!btnSend.isEnabled) {
            return
        }

        addUserMessage(
            message
        )

        sendMessageToAi(
            message
        )
    }


    // ==============================================
    // GỬI TIN NHẮN TỚI AI
    // ==============================================

    private fun sendMessageToAi(
        message: String
    ) {

        val prefs =
            getSharedPreferences(
                "auth",
                MODE_PRIVATE
            )


        val token =
            prefs.getString(
                "token",
                null
            )


        // ==========================================
        // KIỂM TRA JWT
        // ==========================================

        if (token.isNullOrBlank()) {

            addBotMessage(
                "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại."
            )

            goToLogin()

            return
        }


        // ==========================================
        // KHÓA CÁC NÚT
        // ==========================================

        setChatButtonsEnabled(
            false
        )


        // ==========================================
        // HIỂN THỊ LOADING
        // ==========================================

        removeLoadingMessage()

        loadingMessageView =
            addBotMessage(
                "Đang tìm phim phù hợp... 🎬"
            )


        // ==========================================
        // REQUEST
        // ==========================================

        val request =
            AiChatRequest(
                message = message
            )


        // ==========================================
        // GỌI BACKEND
        // ==========================================

        RetrofitClient
            .apiService
            .chatWithAi(
                "Bearer $token",
                request
            )
            .enqueue(

                object :
                    Callback<AiChatResponse> {


                    // ==================================
                    // RESPONSE
                    // ==================================

                    override fun onResponse(
                        call: Call<AiChatResponse>,
                        response: Response<AiChatResponse>
                    ) {

                        removeLoadingMessage()

                        setChatButtonsEnabled(
                            true
                        )


                        // ==============================
                        // TOKEN HẾT HẠN
                        // ==============================

                        if (
                            response.code() == 401 ||
                            response.code() == 403
                        ) {

                            addBotMessage(
                                "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại."
                            )

                            goToLogin()

                            return
                        }


                        // ==============================
                        // THÀNH CÔNG
                        // ==============================

                        if (response.isSuccessful) {

                            val result =
                                response.body()


                            if (
                                result != null &&
                                result.reply.isNotBlank()
                            ) {

                                val cleanReply =
                                    cleanAiMessage(
                                        result.reply
                                    )

                                addBotMessage(
                                    cleanReply
                                )

                            } else {

                                addBotMessage(
                                    "AI chưa trả về câu trả lời."
                                )
                            }

                        } else {

                            val error =
                                response
                                    .errorBody()
                                    ?.string()


                            Log.e(
                                "AI_CHAT",
                                "HTTP ${response.code()} - $error"
                            )


                            addBotMessage(
                                "Không thể kết nối với AI lúc này. Vui lòng thử lại."
                            )
                        }
                    }


                    // ==================================
                    // LỖI KẾT NỐI
                    // ==================================

                    override fun onFailure(
                        call: Call<AiChatResponse>,
                        t: Throwable
                    ) {

                        removeLoadingMessage()

                        setChatButtonsEnabled(
                            true
                        )


                        Log.e(
                            "AI_CHAT",
                            "Lỗi kết nối: ${t.message}",
                            t
                        )


                        addBotMessage(
                            "Không kết nối được máy chủ AI. Vui lòng kiểm tra kết nối."
                        )
                    }
                }
            )
    }


    // ==============================================
    // XỬ LÝ TEXT AI
    // ==============================================

    private fun cleanAiMessage(
        message: String
    ): String {

        return message

            // Bỏ Markdown in đậm
            .replace(
                "**",
                ""
            )

            // Bỏ Markdown tiêu đề
            .replace(
                "### ",
                ""
            )

            .replace(
                "## ",
                ""
            )

            .replace(
                "# ",
                ""
            )

            // Bỏ dấu gạch Markdown
            .replace(
                Regex("""(?m)^\s*[-*]\s+"""),
                "• "
            )

            .trim()
    }


    // ==============================================
    // BẬT / TẮT NÚT CHAT
    // ==============================================

    private fun setChatButtonsEnabled(
        enabled: Boolean
    ) {

        btnSend.isEnabled =
            enabled

        btnAction.isEnabled =
            enabled

        btnComedy.isEnabled =
            enabled

        btnRomance.isEnabled =
            enabled

        edtMessage.isEnabled =
            enabled
    }


    // ==============================================
    // XÓA DÒNG LOADING
    // ==============================================

    private fun removeLoadingMessage() {

        loadingMessageView?.let {

            chatContainer.removeView(
                it
            )
        }

        loadingMessageView =
            null
    }


    // ==============================================
    // HIỂN THỊ TIN NHẮN USER
    // ==============================================

    private fun addUserMessage(
        message: String
    ) {

        val textView =
            TextView(this)


        textView.text =
            message

        textView.textSize =
            15f

        textView.setTextColor(
            Color.BLACK
        )


        textView.setPadding(
            18,
            12,
            18,
            12
        )


        textView.background =
            getDrawable(
                R.drawable.bg_user_message
            )


        val params =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )


        params.gravity =
            Gravity.END


        params.setMargins(
            50,
            8,
            0,
            8
        )


        textView.layoutParams =
            params


        chatContainer.addView(
            textView
        )


        scrollToBottom()
    }


    // ==============================================
    // HIỂN THỊ TIN NHẮN AI
    // ==============================================

    private fun addBotMessage(
        message: String
    ): TextView {

        val textView =
            TextView(this)


        textView.text =
            message

        textView.textSize =
            15f

        textView.setTextColor(
            Color.WHITE
        )


        textView.setPadding(
            18,
            12,
            18,
            12
        )


        textView.background =
            getDrawable(
                R.drawable.bg_bot_message
            )


        val params =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )


        params.gravity =
            Gravity.START


        params.setMargins(
            0,
            8,
            50,
            8
        )


        textView.layoutParams =
            params


        chatContainer.addView(
            textView
        )


        scrollToBottom()


        return textView
    }


    // ==============================================
    // CUỘN XUỐNG CUỐI CHAT
    // ==============================================

    private fun scrollToBottom() {

        scrollChat.post {

            scrollChat.fullScroll(
                View.FOCUS_DOWN
            )
        }
    }


    // ==============================================
    // QUAY VỀ LOGIN
    // ==============================================

    private fun goToLogin() {

        getSharedPreferences(
            "auth",
            MODE_PRIVATE
        )
            .edit()
            .clear()
            .apply()


        val intent =
            Intent(
                this,
                LoginActivity::class.java
            )


        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK


        startActivity(
            intent
        )

        finish()
    }
}