package com.example.languagedevelopmentapp.ui.screen.main.home

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.languagedevelopmentapp.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
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
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    private var _wordState = MutableStateFlow(HomeScreenUiModel())
    var wordState = _wordState.asStateFlow()

    private val auth = FirebaseAuth.getInstance()

    val modelManager = RemoteModelManager.getInstance()
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

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
                    if (word.isNotEmpty()) {
                        englishTurkishTranslator.translate(word)
                            .addOnSuccessListener {
                                _wordState.value =
                                    _wordState.value.copy(translate = it, word = word)
                            }
                    }
                }
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
        viewModelScope.launch {
            _wordState.value = _wordState.value.copy(wordZip = zipList)
            val map = hashMapOf<String, Any>()
            map["word"] = wordState.word
            map["translate"] = wordState.translate
            map["synonyms"] = zipList
            map["date"] = Timestamp.now()
            val firestore = Firebase.firestore
            Log.d("wordState", wordState.word)
            val reference = firestore.collection("Users").document(auth.currentUser?.email.toString()).collection("Words").document(wordState.word)
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