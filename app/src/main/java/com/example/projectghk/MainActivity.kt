package com.example.projectghk

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    var selectedDate: String = ""
    var startTime: String = ""
    var endTime: String = ""
    private lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputManagerName = findViewById<EditText>(R.id.inputManagerName)
        val inputEmployeeName = findViewById<EditText>(R.id.inputEmployeeName)
        val btnPickDate = findViewById<Button>(R.id.btnPickDate)
        val btnPickStartTime = findViewById<Button>(R.id.btnPickStartTime)
        val btnPickEndTime = findViewById<Button>(R.id.btnPickEndTime)
        val btnConfirm = findViewById<Button>(R.id.btnConfirm)
        val tvSelectedDate = findViewById<TextView>(R.id.tvSelectedDate)
        tableLayout = findViewById(R.id.tableLayout)
        // Gọi hàm hiển thị dialog đăng nhập ở góc trên phải
        // TextView liên kết Đăng nhập
        val tvLoginLink = findViewById<TextView>(R.id.tvLoginLink)

        // Khi bấm vào Đăng nhập
        tvLoginLink.setOnClickListener {
            showLoginDialog(tvLoginLink)
        }


        // Chọn ngày làm việc
        btnPickDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, monthOfYear, dayOfMonth)
                val dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
                selectedDate = "$dayOfWeek, $dayOfMonth/${monthOfYear + 1}/$year"
                tvSelectedDate.text = selectedDate
            }, year, month, day)

            datePickerDialog.show()
        }

        // Chọn giờ bắt đầu
        btnPickStartTime.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
                // Định dạng thời gian theo định dạng HH:mm
                startTime = String.format("%02d:%02d", hourOfDay, minute)
                btnPickStartTime.text = startTime // Cập nhật văn bản nút với thời gian đã chọn
            }, hour, minute, true)

            timePickerDialog.show()
        }

// Chọn giờ tan ca
        btnPickEndTime.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
                // Định dạng thời gian theo định dạng HH:mm
                endTime = String.format("%02d:%02d", hourOfDay, minute)
                btnPickEndTime.text = endTime // Cập nhật văn bản nút với thời gian đã chọn
            }, hour, minute, true)

            timePickerDialog.show()
        }


        // Xác nhận chấm công
        btnConfirm.setOnClickListener {
            val managerName = inputManagerName.text.toString()
            val employeeName = inputEmployeeName.text.toString()

            if (managerName.isEmpty() || employeeName.isEmpty() || selectedDate.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                // Hiển thị AlertDialog xác nhận
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Xác nhận thông tin")
                builder.setMessage("Bạn có chắc chắn muốn thêm thông tin này vào bảng chấm công?")

                builder.setPositiveButton("Có") { dialog, _ ->
                    // Tạo một hàng mới cho bảng thông tin chấm công
                    val tableRow = TableRow(this)

                    // Tạo các TextView cho mỗi cột
                    val tvManagerName = TextView(this).apply {
                        text = managerName
                        setPadding(8, 8, 8, 8) // Thêm một chút khoảng cách
                        setBackgroundResource(R.drawable.cell_border) // Thêm khung
                    }

                    val tvEmployeeName = TextView(this).apply {
                        text = employeeName
                        setPadding(8, 8, 8, 8)
                        setBackgroundResource(R.drawable.cell_border) // Thêm khung
                    }

                    val tvDate = TextView(this).apply {
                        text = selectedDate
                        setPadding(8, 8, 8, 8)
                        setBackgroundResource(R.drawable.cell_border) // Thêm khung
                    }

                    val tvStartTime = TextView(this).apply {
                        text = startTime
                        setPadding(8, 8, 8, 8)
                        setBackgroundResource(R.drawable.cell_border) // Thêm khung
                    }

                    val tvEndTime = TextView(this).apply {
                        text = endTime
                        setPadding(8, 8, 8, 8)
                        setBackgroundResource(R.drawable.cell_border) // Thêm khung
                    }

                    // Thêm các TextView vào hàng
                    tableRow.addView(tvManagerName)
                    tableRow.addView(tvEmployeeName)
                    tableRow.addView(tvDate)
                    tableRow.addView(tvStartTime)
                    tableRow.addView(tvEndTime)

                    // Thêm hàng vào TableLayout
                    tableLayout.addView(tableRow)

                    // Reset thông tin để nhập cho lần tiếp theo
                    inputManagerName.text.clear()
                    inputEmployeeName.text.clear()
                    selectedDate = ""
                    startTime = ""
                    endTime = ""
                    btnPickStartTime.text = "Giờ vào ca"
                    btnPickEndTime.text = "Giờ tan ca"

                    dialog.dismiss() // Đóng dialog
                }
                builder.setNegativeButton("Không") { dialog, _ ->
                    dialog.dismiss() // Đóng dialog
                }
                builder.show() // Hiển thị dialog
            }
        }
    }
    // Hàm hiển thị Custom AlertDialog đăng nhập
    private fun showLoginDialog(tvLoginLink: TextView) {
        // Inflate custom dialog layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_login, null)

        // Tạo AlertDialog
        val loginDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Thiết lập vị trí cho dialog: nằm ở góc trên phải
        val window = loginDialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(window.attributes)
            layoutParams.gravity = Gravity.TOP or Gravity.END // Đặt vị trí trên và phải
            layoutParams.x = 50 // Khoảng cách từ cạnh phải
            layoutParams.y = 100 // Khoảng cách từ cạnh trên
            window.attributes = layoutParams
        }

        // Lấy các thành phần từ layout dialog
        val etUsername = dialogView.findViewById<EditText>(R.id.etUsername)
        val etPassword = dialogView.findViewById<EditText>(R.id.etPassword)
        val btnLogin = dialogView.findViewById<Button>(R.id.btnLogin)

        // Xử lý sự kiện khi nhấn nút Đăng nhập
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                loginDialog.dismiss()

                // Sau khi đăng nhập thành công, thay đổi liên kết Đăng nhập thành "Xin chào, [Tên người dùng]"
                tvLoginLink.text = "Hi,$username"
                tvLoginLink.isClickable = false // Tắt chức năng click sau khi đăng nhập thành công
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }

        // Hiển thị dialog
        loginDialog.show()
    }
}

