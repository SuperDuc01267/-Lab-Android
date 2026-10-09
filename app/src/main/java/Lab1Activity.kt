
package com.example.baitaplab

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.baitaplab.databinding.ActivityLab1Binding

class Lab1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityLab1Binding

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)


        supportActionBar?.hide()

        binding = ActivityLab1Binding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnBack.setOnClickListener {
            finish()
        }


        binding.btnRegister.setOnClickListener {
            val name = binding.edtName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (
                name.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Vui lòng nhập đủ thông tin",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Đăng ký thành công!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

