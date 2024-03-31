package com.example.languagedevelopmentapp.ui.screen.main.practice.prepracticescreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.languagedevelopmentapp.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PrePracticeScreenViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _words = MutableStateFlow(PrePracticeUiModel())
    val words = _words.asStateFlow()

    init {
        getWords()
    }

    private fun getWords() {
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
            }
    }
}