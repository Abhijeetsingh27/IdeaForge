package com.example.ideaforge

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class SignUpActivity : AppCompatActivity() {

    private lateinit var nameInput: TextInputEditText
    private lateinit var usernameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var btnSignUp: MaterialButton
    private lateinit var backBtnImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        // Bind views
        nameInput = findViewById(R.id.nameInput)
        usernameInput = findViewById(R.id.usernameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        btnSignUp = findViewById(R.id.btnSignUp)
        backBtnImg = findViewById(R.id.backBtnImg)

        // Sign Up Button Click
        btnSignUp.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()

            // Validate inputs
            if (name.isEmpty()) {
                nameInput.error = "Name is required."
                nameInput.requestFocus()
                return@setOnClickListener
            }

            if (username.contains(" ")) {
                usernameInput.error = "Username should not contain spaces."
                usernameInput.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "Invalid email format."
                emailInput.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                passwordInput.error = "Password must be at least 8 characters."
                passwordInput.requestFocus()
                return@setOnClickListener
            }

            // All validation passed - proceed
            Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // Back Button Click
        backBtnImg.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
