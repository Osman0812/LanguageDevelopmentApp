package com.example.languagedevelopmentapp.ui.screen.main.practice.readingscreen

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagedevelopmentapp.BuildConfig
import com.example.languagedevelopmentapp.ui.screen.main.practice.prepracticescreen.PrePracticeUiModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
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

    val modelManager = RemoteModelManager.getInstance()
    val wordList = mutableListOf<String>()

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
                        wordList.add(word)
                        wordStringBuilder.append("$word, ")
                    }
                    _words.value = _words.value.copy(readingTextWordsList = wordList)
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
            Log.d("words",words)
            val prompt = "İ want to learn better these words:$words. Write a paragraph for me."
            val response = generativeModel.generateContent(prompt)
            val translatePrompt = "translate ${response.text.toString()} to turkish"
            val translateResponse = generativeModel.generateContent(translatePrompt)
            Log.d("translate",translateResponse.text.toString())
            _words.value = _words.value.copy(readingText = response.text.toString(), translatedReadingText = translateResponse.text.toString())
        }
    }

    fun processText(text: String) {
        viewModelScope.launch {
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

    fun translateModel(word: String = "") {
        viewModelScope.launch {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.TURKISH)
                .build()
            val englishTurkishTranslator = Translation.getClient(options)

            val turkishModel = TranslateRemoteModel.Builder(TranslateLanguage.TURKISH).build()
            val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()
            modelManager.download(turkishModel, conditions)
                .addOnSuccessListener {
                    Log.d("translated","asda")
                    if (word.isNotEmpty()) {
                        englishTurkishTranslator.translate(word)
                            .addOnSuccessListener {
                                _words.value = _words.value.copy(translatedSelectedText = it)

                            }
                    }
                }
        }
    }
    fun clearState() {
        _words.value = _words.value.copy(translatedSelectedText = "")
    }
}