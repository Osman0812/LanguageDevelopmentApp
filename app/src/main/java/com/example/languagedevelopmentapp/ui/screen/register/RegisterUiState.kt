package com.example.languagedevelopmentapp.ui.screen.register

data class RegisterUiState(
    val isLoading: Boolean? = null,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val password: String = "",
    val passwordRepeat: String = ""
) {
    fun copyWith(
        isLoading: Boolean? = null,
        successMessage: String? = null,
        errorMessage: String? = null,
        name: String? = null,
        surname: String? = null,
        email: String? = null,
        password: String? = null,
        passwordRepeat: String? = null
    ): RegisterUiState {
        return RegisterUiState(
            isLoading = isLoading ?: this.isLoading,
            successMessage = successMessage ?: this.successMessage,
            errorMessage = errorMessage ?: this.errorMessage,
            name = name ?: this.name,
            surname = surname ?: this.surname,
            email = email ?: this.email,
            password = password ?: this.password,
            passwordRepeat = passwordRepeat ?: this.passwordRepeat
        )
    }
}

sealed class RegisterInputField {
    data object Name: RegisterInputField()
    data object Surname: RegisterInputField()
    data object Email: RegisterInputField()
    data object Password: RegisterInputField()
    data object PasswordRepeat: RegisterInputField()
}