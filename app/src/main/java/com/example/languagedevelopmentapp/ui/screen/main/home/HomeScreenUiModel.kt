package com.example.languagedevelopmentapp.ui.screen.main.home

data class HomeScreenUiModel(
    val word: String = "",
    val translate: String = "",
    val otherUsagesEnglish: List<String> = emptyList(),
    val otherUsagesTurkish: List<String> = emptyList(),
    val wordExampleText: String = ""
)
