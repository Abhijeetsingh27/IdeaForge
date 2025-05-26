package com.example.ideaforge

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ideaforge.databinding.FragmentHomeBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class HomeFragment : Fragment() {

    // View Binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Shared ViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()

    // UI elements
    private lateinit var greetingTextView: TextView
    private lateinit var postIdeaButton: Button
    private lateinit var ideaTitleEditText: EditText
    private lateinit var ideaDescriptionEditText: EditText
    private lateinit var problemEditText: EditText
    private lateinit var uspEditText: EditText
    private lateinit var addLinkButton: Button
    private lateinit var linkCountTextView: TextView
    private lateinit var addFileButton: Button
    private lateinit var fileCountTextView: TextView

    // Data
    private val attachedLinks = mutableListOf<String>()
    private val attachedFiles = mutableListOf<Uri>()
    private var userName: String = "User"

    // Activity result launchers
    private val linkLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val link = data?.getStringExtra("link")
                if (link != null && link.isNotBlank()) {
                    attachedLinks.add(link)
                    linkCountTextView.text = "(${attachedLinks.size})"
                    enablePostButtonIfFieldsFilled()
                    showToast("Link Added")
                } else {
                    showToast("No link was added")
                }
            } else {
                showToast("Link adding cancelled")
            }
        }

    private val fileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                if (data?.data != null) {
                    val fileUri = data.data!!
                    attachedFiles.add(fileUri)
                    fileCountTextView.text = "(${attachedFiles.size})"
                    enablePostButtonIfFieldsFilled()
                    showToast("File Attached")
                }
            } else {
                showToast("File adding cancelled")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI elements using View Binding
        greetingTextView = binding.greetingTextView
        postIdeaButton = binding.postIdeaButton
        ideaTitleEditText = binding.ideaTitleEditText
        ideaDescriptionEditText = binding.ideaDescriptionEditText
        problemEditText = binding.problemEditText
        uspEditText = binding.uspEditText
        addLinkButton = binding.addLinkButton
        linkCountTextView = binding.linkCountTextView
        addFileButton = binding.addFileButton
        fileCountTextView = binding.fileCountTextView

        // Set up greeting
        userName = getUserName()
        greetingTextView.text = "Hello $userName!"

        // Set up listeners
        setupListeners()
        postIdeaButton.isEnabled = false
    }

    private fun setupListeners() {
        // Post Idea Button
        postIdeaButton.setOnClickListener {
            postIdea()
        }

        // Add Link Button
        addLinkButton.setOnClickListener {
            val intent = Intent(requireContext(), AddLinkActivity::class.java)
            linkLauncher.launch(intent)
        }

        // Add File Button
        addFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }
            fileLauncher.launch(intent)
        }

        // Text Watchers
        ideaTitleEditText.addTextChangedListener(textWatcher)
        ideaDescriptionEditText.addTextChangedListener(textWatcher)
        problemEditText.addTextChangedListener(textWatcher)
        uspEditText.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            enablePostButtonIfFieldsFilled()
        }
    }

    private fun enablePostButtonIfFieldsFilled() {
        val title = ideaTitleEditText.text.toString().trim()
        val description = ideaDescriptionEditText.text.toString().trim()
        val problem = problemEditText.text.toString().trim()
        val usp = uspEditText.text.toString().trim()

        postIdeaButton.isEnabled =
            title.isNotBlank() && description.isNotBlank() && problem.isNotBlank() && usp.isNotBlank()
    }

    private fun postIdea() {
        // Collect data from the UI
        val title = ideaTitleEditText.text.toString().trim()
        val description = ideaDescriptionEditText.text.toString().trim()
        val problem = problemEditText.text.toString().trim()
        val usp = uspEditText.text.toString().trim()

        // 1.  Validate Data
        if (title.isBlank() || description.isBlank() || problem.isBlank() || usp.isBlank()) {
            showToast("Please fill in all required fields.")
            return
        }

        // 2. Handle Files (Copy to app's storage)
        val copiedFiles = mutableListOf<String>()
        attachedFiles.forEach { uri ->
            val copiedFilePath = copyFileToInternalStorage(uri)
            if (copiedFilePath != null) {
                copiedFiles.add(copiedFilePath)
            } else {
                showToast("Failed to copy a file.")
                return
            }
        }

        // 3. Store Data in ViewModel
        sharedViewModel.setIdeaData(title, description, problem, usp, attachedLinks, attachedFiles, copiedFiles)

        showToast("Idea posted.  Navigating to Explore...")

        // 4. Navigate to Explore Fragment.  (Important:  Don't create a new instance!)
        val exploreFragment =
            parentFragmentManager.findFragmentByTag("ExploreFragment") as? ExploreFragment
        if (exploreFragment != null) {
            // If ExploreFragment exists, replace the current fragment.
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, exploreFragment, "ExploreFragment")
                .addToBackStack(null)
                .commit()
        } else {
            // If ExploreFragment doesn't exist (which is unlikely, but we handle it), create a new one.
            val newExploreFragment = ExploreFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newExploreFragment, "ExploreFragment")
                .addToBackStack(null)
                .commit()
        }
        clearInputFields()
    }

    private fun copyFileToInternalStorage(uri: Uri): String? {
        val context = context ?: return null
        val contentResolver = context.contentResolver ?: return null // Add null check
        val inputStream = contentResolver.openInputStream(uri) ?: return null

        val originalFileName = getFileName(uri) ?: "unknown_file"
        val fileExtension = getFileExtension(originalFileName)
        val uniqueFileName = generateUniqueFileName(fileExtension)

        val internalStorageDir = context.filesDir ?: return null // Add null check
        val file = File(internalStorageDir, uniqueFileName)

        try {
            val outputStream = FileOutputStream(file)
            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            outputStream.close()
            inputStream.close()
            return file.absolutePath
        } catch (e: IOException) {
            Log.e("HomeFragment", "Error copying file: ${e.message}")
            return null
        }
    }

    private fun getFileName(uri: Uri): String? {
        val context = context ?: return null
        val contentResolver = context.contentResolver ?: return null
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex =
                    it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    return it.getString(displayNameIndex)
                }
            }
        }
        return null
    }

    private fun getFileExtension(fileName: String): String {
        val dotIndex = fileName.lastIndexOf('.')
        return if (dotIndex >= 0 && dotIndex < fileName.length - 1) {
            fileName.substring(dotIndex + 1)
        } else {
            ""
        }
    }

    private fun generateUniqueFileName(extension: String): String {
        val timestamp = System.currentTimeMillis()
        val random = (1000..9999).random()
        return "file_${timestamp}_${random}.${extension}"
    }

    private fun getUserName(): String {
        return "Awesome User"
    }

    private fun showToast(message: String) {
        val context = context ?: activity
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInputFields() {
        ideaTitleEditText.text.clear()
        ideaDescriptionEditText.text.clear()
        problemEditText.text.clear()
        uspEditText.text.clear()
        attachedLinks.clear()
        attachedFiles.clear()
        linkCountTextView.text = "(0)"
        fileCountTextView.text = "(0)"
        postIdeaButton.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}