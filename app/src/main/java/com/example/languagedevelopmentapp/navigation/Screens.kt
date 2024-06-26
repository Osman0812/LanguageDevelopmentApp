package com.example.languagedevelopmentapp.navigation

open class Screens(val route: String) {
    data object LoginScreen : Screens(route = "LOGIN_SCREEN")
    data object RegisterScreen : Screens(route = "REGISTER_SCREEN")
    data object PracticeScreen : Screens(route = "PRACTICE_SCREEN")
    data object ReadingScreen : Screens(route = "READING_SCREEN")
    data object ResultScreen : Screens(route = "RESULT_SCREEN")
}