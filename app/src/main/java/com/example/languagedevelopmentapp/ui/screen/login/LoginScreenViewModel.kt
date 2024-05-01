package com.example.languagedevelopmentapp.ui.screen.login

import androidx.lifecycle.ViewModel
import com.example.languagedevelopmentapp.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor() : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val _loginState = MutableStateFlow(LoginUiState())
    val loginState = _loginState.asStateFlow()

    fun onEmailChange(email: String) {
        _loginState.value = loginState.value.copy(email = email, errorMessage = null)
    }

    fun onPasswordChange(password: String) {
        _loginState.value = loginState.value.copy(password = password, errorMessage = null)
    }

    fun login() {
        _loginState.value = _loginState.value.copy(isLoading = true)
        val email = _loginState.value.email
        val password = _loginState.value.password

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        isLogged = true
                    )

                }
                .addOnFailureListener {
                    val errorMessage = it.message
                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        isLogged = false,
                        errorMessage = errorMessage
                    )
                }
        } else {
            _loginState.value = _loginState.value.copy(
                isLoading = false,
                isLogged = false,
                errorMessage = R.string.invalid_inputs_error
            )
        }
    }
}