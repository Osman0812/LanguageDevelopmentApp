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

    fun getWords() {
        viewModelScope.launch {
            reference.get().addOnSuccessListener { snapshot ->
                val documents = snapshot.documents
                val updatedList = mutableListOf<Pair<String, String>>()
                val updatedSynonyms = mutableListOf<List<HashMap<String, String>>>()
                documents.forEach { documentSnapshot ->
                    val translate = documentSnapshot.getString("translate")
                    val word = documentSnapshot.id
                    val pair = Pair(word, translate ?: "")
                    updatedList.add(pair)
                    val synonyms = documentSnapshot.get("synonyms") as? List<HashMap<String, *>>?
                    synonyms?.let {
                        val synonymMap = mutableListOf<HashMap<String, String>>()
                        it.forEach { synonym ->
                            synonymMap.add(synonym as HashMap<String, String>)
                        }
                        updatedSynonyms.add(synonymMap) // Add HashMap to the list
                    }
                }
                _wordListState.value =
                    _wordListState.value.copy(wordList = updatedList, synonyms = updatedSynonyms)
            }.addOnFailureListener { exception ->
                Log.d("fail", exception.message.toString())
            }
        }
    }
}