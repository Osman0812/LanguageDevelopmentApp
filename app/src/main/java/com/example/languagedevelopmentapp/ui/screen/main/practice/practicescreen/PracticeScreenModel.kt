package com.example.languagedevelopmentapp.ui.screen.main.practice.practicescreen

data class PracticeScreenModel(
    val questionList: List<Question> = emptyList(),
    val questions: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: String? = null
)
