package com.example.languagedevelopmentapp.ui.screen.main.vocabulary

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class VocabularyScreenViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    private val firestore = Firebase.firestore
    private val reference = firestore.collection("Words")

    private val _wordListState = MutableStateFlow(VocabularyUiModel())
    val wordListState = _wordListState.asStateFlow()

    init {
        getWords()
        getLists()
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

    fun getLists() {
        viewModelScope.launch {
            try {
                val result = firestore.collection("Lists").get().await()
                val listNames = result.documents.mapNotNull { it.getString("name") }
                _wordListState.value = _wordListState.value.copy(listNames = listNames)
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    fun getWordsOfList(listName: String) {
        viewModelScope.launch {
            try {
                firestore.collection("Lists").document(listName).collection("words").get()
                    .addOnSuccessListener { result ->
                        val words = result.documents.map { it.id }
                        val updatedWordList = _wordListState.value.wordsOfList.toMutableList()
                        updatedWordList.addAll(words)
                        _wordListState.value =
                            _wordListState.value.copy(wordsOfList = updatedWordList)
                    }
            } catch (e: Exception) {
                Toast.makeText(
                    application.applicationContext,
                    e.message.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun onCreateList(listName: String) {
        viewModelScope.launch {
            Log.d("listName", listName)
            val listData = hashMapOf(
                "name" to listName,
                "createdAt" to System.currentTimeMillis()
            )
            firestore.collection("Lists").document(listName)
                .set(listData)
                .addOnSuccessListener {
                    Toast.makeText(
                        application.applicationContext,
                        "Created $listName!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(application.applicationContext, it.message, Toast.LENGTH_LONG)
                        .show()
                }
        }
    }

    fun deleteList(listName: String) {
        viewModelScope.launch {
            try {
                firestore.collection("Lists").document(listName).delete().await()
                val updatedListNames = _wordListState.value.listNames.toMutableList()
                updatedListNames.remove(listName)
                _wordListState.value = _wordListState.value.copy(listNames = updatedListNames)
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    fun clearState() {
        _wordListState.value =
            _wordListState.value.copy(
                wordsOfList = emptyList()
            )
    }
}