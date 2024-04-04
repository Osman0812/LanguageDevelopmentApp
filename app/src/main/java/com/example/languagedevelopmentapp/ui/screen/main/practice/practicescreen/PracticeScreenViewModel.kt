package com.example.languagedevelopmentapp.ui.screen.main.practice.practicescreen


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.languagedevelopmentapp.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val application: Application
): AndroidViewModel(application) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )
    private val _remainingTime = MutableStateFlow<Int>(0)
    val remainingTime: StateFlow<Int> = _remainingTime

    private val _questionList = MutableStateFlow(PracticeScreenModel())
    val questionList = _questionList.asStateFlow()

    val list = mutableListOf<String>()

    init {
        val initialTime = savedStateHandle.get<Int>("initialTime") ?: 60
        _remainingTime.value = initialTime
        //startCountdown()
        getQuestions()
    }

    private fun startCountdown() {
        viewModelScope.launch {
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value -= 1
            }
        }
    }

    fun parseQuestions(responseText: String) {
        viewModelScope.launch {
            val questions = mutableListOf<Question>()

            val lines = responseText.lines()

            var questionNumber = ""
            var questionText = ""
            val options = mutableListOf<String>()

            for (line in lines) {
                if (line.startsWith(Regex("\\d+\\.").pattern) || line.contains("?")) {
                    questionNumber = line.substringBefore(".").trim()
                    questionText = line.substringAfter(".").trim()
                    questions.add(Question(questionNumber, questionText, options.toList()))
                    options.clear()
                } else if( line.contains("(") || line.contains("-")) {
                    options.add(line.trim())
                }
            }
            _questionList.value = _questionList.value.copy(questionList = questions)
            questions.forEachIndexed { index, question ->
                println("Question ${question.questionNumber}: ${question.questionText}")
                question.answerOptions.forEachIndexed { i, option ->
                    println("  (${('A' + i)}) ${option.substring(3)}")
                }
            }
        }

    }

    fun getQuestions() {
        viewModelScope.launch {
            val prompt =
                "Write a level determination english in test format, 20 questions,only questions and 4 options per each."
            val response = generativeModel.generateContent(prompt)
            Log.d("questions", response.text.toString())

            val questions = parseQuestions(response.text ?: "")
        }
    }
}