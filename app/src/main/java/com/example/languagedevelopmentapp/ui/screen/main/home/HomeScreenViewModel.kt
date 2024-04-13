package com.example.languagedevelopmentapp.ui.screen.main.home

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagedevelopmentapp.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    private var _wordState = MutableStateFlow(HomeScreenUiModel())
    var wordState = _wordState.asStateFlow()
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )
/*
    fun translate(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val prompt = "$word anlam ını ve $word dilindeki kelimenin esAnlam larını gsonstring olarak döndür."
            val response = generativeModel.generateContent(content {
                text(prompt)
            })
            Log.d("json", response.text.toString())
            val data = response.text?.trim()?.removePrefix("```json")?.removeSuffix("```")
            Log.d("data", data.toString())
            val gson = Gson()
            val jsonData = gson.fromJson(data, Map::class.java)

            _wordState.value = _wordState.value.copy(
                word = jsonData["kelime"].toString(),
                translate = jsonData["anlam"].toString()
            )
        }
    }

 */



    fun translate(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val prompt = "Write with one word the meaning of $word in turkish."
            val response = generativeModel.generateContent(content {
                text(prompt)
            })
            _wordState.value = _wordState.value.copy(word = word, translate = response.text ?: "")
        }
    }

    fun otherUsagesEnglish(word: String) {
        viewModelScope.launch {
            val prompt = "write 5 synonyms of \"$word\""
            val response = generativeModel.generateContent(prompt)
            val words = response.text
            val wordList = extractWords(words ?: "")
            Log.d("words", words.toString())
            _wordState.value = _wordState.value.copy(otherUsagesEnglish = wordList)
            launch {
                otherUsagesTurkish(words.toString())
            }
        }
    }


    private fun otherUsagesTurkish(word: String) {
        viewModelScope.launch {
            val prompt = "return only 5 turkish meanings of \"$word\""
            val response = generativeModel.generateContent(prompt)
            val words = response.text
            val wordList = extractWords(words ?: "")
            _wordState.value = _wordState.value.copy(otherUsagesTurkish = wordList)
        }
    }

    fun saveToFirebase(wordState: HomeScreenUiModel, zipList: List<Pair<String, String>>) {
        _wordState.value = _wordState.value.copy(wordZip = zipList)
        val map = hashMapOf<String, Any>()
        map["word"] = wordState.word
        map["translate"] = wordState.translate
        map["synonyms"] = zipList
        map["date"] = Timestamp.now()
        val firestore = Firebase.firestore
        val reference = firestore.collection("Words").document(wordState.word)
        reference.get()
            .addOnSuccessListener { docSnap ->
                if (docSnap.exists()) {
                    reference.addSnapshotListener { value, _ ->
                        if (value != null && value.exists()) {
                            Toast.makeText(
                                application.applicationContext,
                                "You already saved \"${wordState.word}\"",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                application.applicationContext,
                                "Updated \"${wordState.word}\"",
                                Toast.LENGTH_LONG
                            ).show()
                            reference.set(map)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        application.applicationContext,
                                        "${wordState.word} saved!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        application.applicationContext,
                                        it.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                    }
                } else {
                    reference.set(map)
                        .addOnSuccessListener {
                            Toast.makeText(
                                application.applicationContext,
                                "\"${wordState.word}\" saved!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                application.applicationContext,
                                it.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
            }
    }

    fun wordExamples(word: String) {
        viewModelScope.launch {
            val prompt = "Give examples of $word used in sentences such as singular, plural etc."
            val response = generativeModel.generateContent(prompt = prompt)
            _wordState.value = _wordState.value.copy(wordExampleText = response.text.toString())
        }
    }
    private fun extractWords(text: String): List<String> {
        return text.lines().filter { it.isNotBlank() }
    }

/*
    private fun extractWords(text: String): List<String> {
        val words = text.split("\\s".toRegex())
        return words.filter { it.matches(Regex("[a-zA-Z]+")) }
    }

 */

    private fun extractWordsTurkish(text: String): List<String> {
        val words = text.split("\\s".toRegex())
        return words.filter { it.matches(Regex("[a-zA-ZçğıöşüÇĞİÖŞÜ]+")) }
    }

    fun clearState() {
        _wordState.value = _wordState.value.copy(
            translate = "",
            otherUsagesEnglish = emptyList(),
            otherUsagesTurkish = emptyList(),
            wordExampleText = ""
        )
    }
}