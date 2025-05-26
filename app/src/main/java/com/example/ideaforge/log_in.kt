package com.example.ideaforge

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var btnLogIn: MaterialButton
    private lateinit var btnForgotPassword: TextView
    private lateinit var btnSignUpNow: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in)

        // Initialize views
        emailInput = findViewById(R.id.logInEmailInput)
        passwordInput = findViewById(R.id.logInPasswordInput)
        btnLogIn = findViewById(R.id.btnLogIn)
        btnForgotPassword = findViewById(R.id.btnForgotPassword)
        btnSignUpNow = findViewById(R.id.btnSignUpNow)

        // Log In button click
        btnLogIn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            when {
                email.isEmpty() || password.isEmpty() -> {
                    showToast("Please enter your credentials")
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    showToast("Email is invalid")
                }
                password.length < 8 -> {
                    showToast("Your password must be at least 8 characters")
                }
                else -> {
                    showToast("Log In Successfully")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }

//        // Forgot Password click
//        btnForgotPassword.setOnClickListener {
//            startActivity(Intent(this, ForgotPasswordActivity::class.java))
//        }

        // Sign Up click
        btnSignUpNow.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
