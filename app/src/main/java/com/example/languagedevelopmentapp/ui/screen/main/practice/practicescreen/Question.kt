package com.example.languagedevelopmentapp.ui.screen.main.practice.practicescreen

data class Question(
    val questionNumber: String = "",
    val questionText: String = "",
    val answerOptions: List<String> = emptyList(),
    val correctAnswer: String = ""
)
