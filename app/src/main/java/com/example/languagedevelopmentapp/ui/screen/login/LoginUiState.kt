package com.example.languagedevelopmentapp.ui.screen.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLogged: Boolean? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean? = null,
    val errorMessage: Any? = null
)
