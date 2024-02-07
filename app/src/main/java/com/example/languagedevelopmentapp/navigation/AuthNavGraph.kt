package com.example.languagedevelopmentapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.languagedevelopmentapp.ui.screen.login.LoginScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        startDestination = Screens.LoginScreen.route,
        route = Graph.AUTH
    ) {
        composable(route = Screens.LoginScreen.route) {
            LoginScreen(
            )
        }
    }
}