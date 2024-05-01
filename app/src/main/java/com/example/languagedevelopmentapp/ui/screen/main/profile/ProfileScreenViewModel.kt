package com.example.languagedevelopmentapp.ui.screen.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class ProfileScreenViewModel @Inject constructor(

) : ViewModel() {
    private val firestore = Firebase.firestore

    private val _profileState = MutableStateFlow(ProfileUiState())
    val profileState = _profileState.asStateFlow()

    fun getUserInfo(
        email: String
    ) {

        val reference = firestore.collection("Users").document(email)
        viewModelScope.launch {
            reference.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("name")
                        val surname = document.getString("surname")
                        val joinDate = document.getTimestamp("joinDate")
                        val password = document.getString("password")

                        val date = joinDate?.let { timestampToDate(it) }
                        _profileState.value = _profileState.value.copy(
                            name = name,
                            surname = surname,
                            password = password,
                            joinDate = date
                        )
                    }
                }
        }
    }

    fun timestampToDate(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }
}