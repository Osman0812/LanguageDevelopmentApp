package com.example.languagedevelopmentapp.ui.screen.main.practice.readingscreen

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagedevelopmentapp.BuildConfig
import com.example.languagedevelopmentapp.ui.screen.main.practice.prepracticescreen.PrePracticeUiModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ReadingScreenViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )
    private val _prePracticeModel = MutableStateFlow(PrePracticeUiModel())
    val prePracticeModel = _prePracticeModel.asStateFlow()

    private val auth = FirebaseAuth.getInstance()


    val modelManager = RemoteModelManager.getInstance()
    val wordList = mutableListOf<String>()

    private val firestore = FirebaseFirestore.getInstance()

    init {
        getWords()
    }

    private fun getWords() {
        viewModelScope.launch {
            val firestore = Firebase.firestore
            val wordStringBuilder = StringBuilder()
            firestore.collection("Users").document(auth.currentUser?.email.toString()).collection("Words")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val word = document.id
                        wordList.add(word)
                        wordStringBuilder.append("$word, ")
                    }
                    _prePracticeModel.value =
                        _prePracticeModel.value.copy(readingTextWordsList = wordList)
                    val wordString = wordStringBuilder.toString().trimEnd { it == ',' }
                    _prePracticeModel.value =
                        _prePracticeModel.value.copy(readingTextWords = wordString)
                    readingExercise(wordString)

                }
                .addOnFailureListener {
                    Toast.makeText(application.applicationContext, it.message, Toast.LENGTH_LONG)
                        .show()
                }
        }
    }

    private fun readingExercise(words: String) {
        viewModelScope.launch {
            Log.d("words", words)
            val prompt = "İ want to learn better these words:$words. Write a paragraph for me."
            val response = generativeModel.generateContent(prompt)
            val translatePrompt = "translate ${response.text.toString()} to turkish"
            val translateResponse = generativeModel.generateContent(translatePrompt)
            Log.d("translate", translateResponse.text.toString())
            _prePracticeModel.value = _prePracticeModel.value.copy(
                readingText = response.text.toString(),
                translatedReadingText = translateResponse.text.toString()
            )
        }
    }

    fun getLists() {
        viewModelScope.launch {
            try {
                val db = FirebaseFirestore.getInstance()
                val snapshot = db.collection("Users").document(auth.currentUser?.email.toString()).collection("Lists").get().await()
                val names = snapshot.documents.map { it.id }
                _prePracticeModel.value = _prePracticeModel.value.copy(readingScreenLists = names)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun translateModel(word: String = "") {
        viewModelScope.launch {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.TURKISH)
                .build()
            val englishTurkishTranslator = Translation.getClient(options)

            val turkishModel = TranslateRemoteModel.Builder(TranslateLanguage.TURKISH).build()
            val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()
            modelManager.download(turkishModel, conditions)
                .addOnSuccessListener {
                    Log.d("translated", "asda")
                    if (word.isNotEmpty()) {
                        englishTurkishTranslator.translate(word)
                            .addOnSuccessListener {
                                _prePracticeModel.value =
                                    _prePracticeModel.value.copy(translatedSelectedText = it)
                            }
                    }
                }
        }
    }

    fun saveWordToWords(word: String) {
        viewModelScope.launch {
            val map = hashMapOf<String, Any>()
            map["word"] = word
        }
    }

    fun addWordToList(listId: String, word: String) {
        viewModelScope.launch {
            val wordData = hashMapOf(
                "word" to word,
                "addedAt" to System.currentTimeMillis()
            )
            firestore.collection("Users").document(auth.currentUser?.email.toString()).collection("Lists").document(listId).collection("words").document(word)
                .set(wordData)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(
                        application.applicationContext,
                        "$word added to $listId!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        application.applicationContext,
                        e.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    fun clearState() {
        _prePracticeModel.value = _prePracticeModel.value.copy(translatedSelectedText = "")
    }
}