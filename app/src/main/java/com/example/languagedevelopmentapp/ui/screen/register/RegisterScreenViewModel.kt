package com.example.languagedevelopmentapp.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor() : ViewModel() {
    private var auth = FirebaseAuth.getInstance()
    private val _registerState = MutableStateFlow(RegisterUiState())
    val registerState = _registerState.asStateFlow()

    fun onInputChange(field: RegisterInputField, value: Any) {
        when (field) {
            is RegisterInputField.Name -> {
                _registerState.value = _registerState.value.copyWith(name = value.toString())
            }

            is RegisterInputField.Surname -> {
                _registerState.value = _registerState.value.copyWith(surname = value.toString())
            }

            is RegisterInputField.Email -> {
                _registerState.value = _registerState.value.copyWith(email = value.toString())
            }

            is RegisterInputField.Password -> {
                _registerState.value = _registerState.value.copyWith(password = value.toString())
            }

            is RegisterInputField.PasswordRepeat -> {
                _registerState.value =
                    _registerState.value.copyWith(passwordRepeat = value.toString())
            }
        }
    }

    fun register() {
        val name = _registerState.value.name
        val surname = _registerState.value.surname
        val email = _registerState.value.email
        val password = _registerState.value.password
        val passwordRepeat = _registerState.value.passwordRepeat

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()) {
            _registerState.value =
                _registerState.value.copyWith(errorMessage = "Lütfen geçerli bilgiler giriniz!")
        } else {
            viewModelScope.launch {
                _registerState.value = _registerState.value.copy(isLoading = true)
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        _registerState.value = _registerState.value.copyWith(
                            successMessage = "Successfuly Registered!",
                            isLoading = false
                        )
                    }
                    .addOnFailureListener {
                        _registerState.value = _registerState.value.copyWith(
                            isLoading = false,
                            errorMessage = it.message.toString()
                        )
                    }
            }
        }
    }

    fun clearState() {
        _registerState.value =
            _registerState.value.copy(isLoading = null, successMessage = null, errorMessage = null)
    }
}