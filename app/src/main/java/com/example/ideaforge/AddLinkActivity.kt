package com.example.ideaforge

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ideaforge.databinding.ActivityAddLinkBinding

class AddLinkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLinkBinding
    private lateinit var linkEditText: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLinkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UI elements
        linkEditText = binding.linkEditText
        addButton = binding.addButton
        cancelButton = binding.cancelButton

        // Set up listeners
        addButton.setOnClickListener {
            val link = linkEditText.text.toString().trim()
            if (link.isNotBlank()) {
                // Prepare the result intent
                val resultIntent = Intent()
                resultIntent.putExtra("link", link) // Use the key "link"
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Return to HomeFragment
            } else {
                Toast.makeText(this, "Please enter a link", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
