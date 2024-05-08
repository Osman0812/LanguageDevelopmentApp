package com.example.languagedevelopmentapp.ui.screen.main.practice.prepracticescreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagedevelopmentapp.ui.screen.main.profile.ProfileUiState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrePracticeScreenViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _words = MutableStateFlow(PrePracticeUiModel())
    val words = _words.asStateFlow()

    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    private val firestore = Firebase.firestore
    private val userReference = firestore.collection("Users").document(currentUser?.email.toString())

    private val _userInfo = MutableStateFlow(ProfileUiState())
    val userInfo = _userInfo.asStateFlow()

    init {
        getWords()
        getUserLevelInfo()
    }

    private fun getWords() {
        viewModelScope.launch {
            val wordStringBuilder = StringBuilder()
            firestore.collection("Words")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val word = document.id
                        wordStringBuilder.append("$word, ")
                    }
                    val wordString = wordStringBuilder.toString().trimEnd { it == ',' }
                    _words.value = _words.value.copy(readingTextWords = wordString)
                }
        }
    }

    fun saveInfoToFirebase(profileUiState: ProfileUiState) {
        viewModelScope.launch {
            val list = profileUiState.achievements?.toList()
            val map = hashMapOf<String,Any>()
            map["level"] = profileUiState.level.toString()
            if (list != null) {
                map["achievements"] = list
            }
            userReference.update(map).addOnSuccessListener {
                Log.d("Updated","User Info Updated")
            }
        }
    }

    private fun getUserLevelInfo() {
        viewModelScope.launch {
            userReference.get().addOnSuccessListener {
                val level = it.getString("level")
                Log.d("level",level.toString())
                _userInfo.value = _userInfo.value.copy(level = level)
            }
        }
    }
}