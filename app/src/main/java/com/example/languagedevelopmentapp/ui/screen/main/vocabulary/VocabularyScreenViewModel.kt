package com.example.languagedevelopmentapp.ui.screen.main.vocabulary

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VocabularyScreenViewModel @Inject constructor(

) : ViewModel() {

    private val firestore = Firebase.firestore
    private val reference = firestore.collection("Words")

    private val _wordListState = MutableStateFlow(VocabularyUiModel())
    val wordListState = _wordListState.asStateFlow()

    init {
        getWords()
    }

    private fun getWords() {
        viewModelScope.launch {
            reference.get().addOnSuccessListener {
                val documents = it.documents
                val updatedList = mutableListOf<Pair<String, String>>()
                documents.forEach { documentSnapshot ->
                    val translate = documentSnapshot.data?.get("translate")
                    val word = documentSnapshot.id
                    val pair = Pair(word, translate.toString())
                    updatedList.add(pair)
                }
                _wordListState.value = _wordListState.value.copy(wordList = updatedList)
            }
                .addOnFailureListener {
                    Log.d("fail", it.message.toString())
                }
        }
    }
}