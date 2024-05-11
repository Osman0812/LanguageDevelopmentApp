package com.example.languagedevelopmentapp.ui.screen.main.practice.prepracticescreen

data class PrePracticeUiModel(
    val readingTextWords: String = "",
    val readingTextWordsList: List<String> = emptyList(),
    val readingText: String = "",
    val translatedReadingText: String = "",
    val translatedSelectedText: String = ""
)
