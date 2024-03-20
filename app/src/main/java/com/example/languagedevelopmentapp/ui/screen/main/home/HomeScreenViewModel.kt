package com.example.languagedevelopmentapp.ui.screen.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagedevelopmentapp.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor() : ViewModel(){

    private var _wordState = MutableStateFlow(HomeScreenUiModel())
    var wordState = _wordState.asStateFlow()
    fun translate(word: String){
        viewModelScope.launch {
            val generativeModel = GenerativeModel(
                modelName = "gemini-pro",
                apiKey = BuildConfig.GEMINI_API_KEY
            )
            val prompt = "Write with a one word the meaning of $word in turkish."
            val response = generativeModel.generateContent(prompt)

            _wordState.value = _wordState.value.copy(translate = response.text.toString())
        }
    }
}