package com.example.languagedevelopmentapp.ui.screen.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagedevelopmentapp.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor() : ViewModel() {

    private var _wordState = MutableStateFlow(HomeScreenUiModel())
    var wordState = _wordState.asStateFlow()
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    fun translate(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val prompt = "Write with one word the meaning of $word in turkish."
            val response = generativeModel.generateContent(content {
                text(prompt)
            })
            _wordState.value = _wordState.value.copy(translate = response.text ?: "")
        }
    }

    fun otherUsagesEnglish(word: String) {
        viewModelScope.launch {
            val prompt = "write 5 synonyms of \"$word\""
            val response = generativeModel.generateContent(prompt)
            val words = response.text
            val wordList = extractWords(words ?: "")
            Log.d("words", words.toString())
            _wordState.value = _wordState.value.copy(otherUsagesEnglish = wordList)
            launch {
                otherUsagesTurkish(words.toString())
            }
        }
    }

    private fun otherUsagesTurkish(word: String) {
        viewModelScope.launch {
            val prompt = "return only 5 turkish meanings of \"$word\""
            val response = generativeModel.generateContent(prompt)
            val words = response.text
            val wordList = extractWordsTurkish(words ?: "")
            Log.d("turkish", words.toString())
            _wordState.value = _wordState.value.copy(otherUsagesTurkish = wordList)
        }
    }

    fun wordExamples(word: String) {
        viewModelScope.launch {
            val prompt = "Give examples of $word used in sentences such as singular, plural etc."
            val response = generativeModel.generateContent(prompt = prompt)
            _wordState.value = _wordState.value.copy(wordExampleText = response.text.toString())
        }
    }

    private fun extractWords(text: String): List<String> {
        val words = text.split("\\s".toRegex())
        return words.filter { it.matches(Regex("[a-zA-Z]+")) }
    }

    private fun extractWordsTurkish(text: String): List<String> {
        val words = text.split("\\s".toRegex())
        return words.filter { it.matches(Regex("[a-zA-ZçğıöşüÇĞİÖŞÜ]+")) }
    }
    fun clearState() {
        _wordState.value = _wordState.value.copy(
            translate = "",
            otherUsagesEnglish = emptyList(),
            otherUsagesTurkish = emptyList()
        )
    }
}