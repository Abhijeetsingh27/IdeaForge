package com.example.ideaforge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.net.Uri

class SharedViewModel : ViewModel() {
    // LiveData to hold the idea data
    val ideaTitle = MutableLiveData<String>()
    val ideaDescription = MutableLiveData<String>()
    val problem = MutableLiveData<String>()
    val usp = MutableLiveData<String>()
    val attachedLinks = MutableLiveData<List<String>>()
    val attachedFiles = MutableLiveData<List<Uri>>()
    val copiedFiles = MutableLiveData<List<String>>() //For the file paths

    // Function to set the idea data
    fun setIdeaData(

        title: String,
        description: String,
        problem: String,
        usp: String,
        links: List<String>,
        files: List<Uri>,
        copiedFiles: List<String>
    ) {
        ideaTitle.value = title
        ideaDescription.value = description
        this.problem.value = problem
        this.usp.value = usp
        attachedLinks.value = links
        attachedFiles.value = files
        this.copiedFiles.value = copiedFiles
    }

    //Clear all data
    fun clearData() {
        ideaTitle.value = ""
        ideaDescription.value = ""
        this.problem.value = ""
        this.usp.value = ""
        attachedLinks.value = emptyList()
        attachedFiles.value = emptyList()
        this.copiedFiles.value = emptyList()
    }
}