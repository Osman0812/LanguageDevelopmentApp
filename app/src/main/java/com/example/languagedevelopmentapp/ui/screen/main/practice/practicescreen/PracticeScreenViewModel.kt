package com.example.languagedevelopmentapp.ui.screen.main.practice.practicescreen


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PracticeScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _remainingTime = MutableStateFlow<Int>(0)
    val remainingTime: StateFlow<Int> = _remainingTime

    init {
        val initialTime = savedStateHandle.get<Int>("initialTime") ?: 60
        _remainingTime.value = initialTime
        startCountdown()
    }

    private fun startCountdown() {
        viewModelScope.launch {
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value -= 1
                Log.d("time: ",_remainingTime.value.toString())
            }
        }
    }
}