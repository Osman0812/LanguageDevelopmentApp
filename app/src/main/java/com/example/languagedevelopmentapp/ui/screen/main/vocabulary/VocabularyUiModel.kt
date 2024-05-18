package com.example.languagedevelopmentapp.ui.screen.main.vocabulary

data class VocabularyUiModel(
    var wordList: List<Pair<String,String>> = emptyList(),
    val synonyms: List<List<HashMap<String, String>>> = emptyList(),
    val listNames: List<String> = emptyList()
)
