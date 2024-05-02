package com.example.languagedevelopmentapp.ui.screen.main.practice.practicescreen


import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.languagedevelopmentapp.BuildConfig
import com.example.languagedevelopmentapp.ui.screen.resultscreen.ResultScreenUiModel
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
) : AndroidViewModel(application) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )
    private val _remainingTime = MutableStateFlow<Int>(0)
    val remainingTime: StateFlow<Int> = _remainingTime

    private val _questionList = MutableStateFlow(PracticeScreenModel())
    val questionList = _questionList.asStateFlow()

    val list = mutableListOf<String>()

    private val _resultScreenUiState = MutableStateFlow(ResultScreenUiModel())
    val resultScreenUiState = _resultScreenUiState.asStateFlow()
    fun startCountdown(time: Int) {
        viewModelScope.launch {
            _remainingTime.value = time
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value -= 1
            }
        }
    }

    fun getQuestions() {
        viewModelScope.launch {
            val prompt =
                "Write a english level determination quiz, 11 questions,only questions and 4 options per each"
            val response = generativeModel.generateContent(prompt)
            Log.d("questions", response.text.toString())
            parseQuestions(response.text ?: "")
        }
    }

    fun getUserLevel(answers: List<String>) {
        Log.d("questions", _questionList.value.questionList.toString())
        Log.d("answers", answers.toString())
        var correctCount = 0
        viewModelScope.launch {
            answers.forEachIndexed { index, s ->
                val prompt =
                    "The Questions: ${_questionList.value.questionList[index].questionText + _questionList.value.questionList[index].questionText2}, answers: $s, give response is it correct or false?"
                val response = generativeModel.generateContent(prompt)
                if (response.text?.contains("correct") == true) {
                    correctCount += 1
                }
            }
            Log.d("correct count", correctCount.toString())
            _resultScreenUiState.value =
                _resultScreenUiState.value.copy(correctCount = correctCount)
        }
    }

    private fun parseQuestions(responseText: String) {
        _questionList.value = _questionList.value.copy(isLoading = true)
        viewModelScope.launch {
            val questions = mutableListOf<Question>()
            val lines = responseText.lines()
            val questionNumber = mutableStateOf("")
            val questionText = mutableStateOf("")
            val questionText2 = mutableStateOf("")
            val questionRegex = Regex("""\d+\.\s*.*""")
            val options = mutableListOf<String>()

            for (line in lines) {
                if (line.startsWith("**" + Regex("\\d+\\.").pattern) || line.contains(questionRegex)) {
                    questions.add(
                        Question(
                            questionNumber.value,
                            questionText.value,
                            questionText2.value,
                            options.toList()
                        )
                    )
                    questionNumber.value = line.substringBefore(".").trim()
                    questionText.value = line.substringAfter(".").substringBefore("(").trim()
                    options.clear()
                    questionText2.value = ""
                } else if (line.contains("(") || line.contains("-")) {
                    options.add(line.trim())
                } else if (line.contains("___") || line.contains("\"")) {
                    questionText2.value = line.substringBefore(".").trim()
                }
            }
            _questionList.value =
                _questionList.value.copy(questionList = questions, isLoading = false)
            questions.forEachIndexed { index, question ->
                println("Question ${question.questionNumber}: ${question.questionText}")
                question.answerOptions.forEachIndexed { i, option ->
                    println("  (${('A' + i)}) ${option.substring(3)}")
                }
            }
        }
    }
}