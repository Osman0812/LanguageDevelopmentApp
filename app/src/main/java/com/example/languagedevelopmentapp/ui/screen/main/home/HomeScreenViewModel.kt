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
    fun translate(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val generativeModel = GenerativeModel(
                modelName = "gemini-pro",
                apiKey = BuildConfig.GEMINI_API_KEY
            )
            val prompt = "Write with one word the meaning of $word in turkish."
            Log.d("word", word)
            val response = generativeModel.generateContent(content {
                text(prompt)
            })
            _wordState.value = _wordState.value.copy(translate = response.text ?: "")
            Log.d("response", _wordState.value.translate)
        }
    }
}