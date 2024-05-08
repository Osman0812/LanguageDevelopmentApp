package com.example.languagedevelopmentapp.ui.screen.main.profile

import androidx.collection.MutableIntList

data class ProfileUiState(
    val name: String? = null,
    val surname: String? = null,
    val password: String? = null,
    val joinDate: String? = null,
    val level: String? = null,
    val achievements: List<Int>? = null
)
