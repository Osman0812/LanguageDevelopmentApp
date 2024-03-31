package com.example.languagedevelopmentapp.ui.screen.main.practice.readingscreen

import android.app.Application
import android.widget.Toast
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagedevelopmentapp.BuildConfig
import com.example.languagedevelopmentapp.ui.screen.main.practice.prepracticescreen.PrePracticeUiModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadingScreenViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )
    private val _words = MutableStateFlow(PrePracticeUiModel())
    val words = _words.asStateFlow()

    private val _selectedWord = MutableStateFlow("")
    val selectedWord: StateFlow<String> = _selectedWord

    init {
        getWords()
    }

    private fun getWords() {
        viewModelScope.launch {
            val firestore = Firebase.firestore
            val wordStringBuilder = StringBuilder()
            firestore.collection("Words")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val word = document.id
                        wordStringBuilder.append("$word, ")
                    }
                    val wordString = wordStringBuilder.toString().trimEnd { it == ',' }
                    _words.value = _words.value.copy(readingTextWords = wordString)
                    readingExercise(wordString)

                }
                .addOnFailureListener {
                    Toast.makeText(application.applicationContext, it.message, Toast.LENGTH_LONG)
                        .show()
                }
        }
    }

    private fun readingExercise(words: String) {
        viewModelScope.launch {
            val prompt = "Write a title and its story for me to clearly get meanings of $words"
            val response = generativeModel.generateContent(prompt)
            _words.value = _words.value.copy(readingText = response.text.toString())
        }
    }

    fun processText(text: String) {
        val splitText = text.split(" ")
        val annotatedString = buildAnnotatedString {
            splitText.forEachIndexed { index, word ->
                if (index > 0) {
                    append(" ")
                }
                append(word)
            }
        }
        _selectedWord.value = annotatedString.toString()
    }
}