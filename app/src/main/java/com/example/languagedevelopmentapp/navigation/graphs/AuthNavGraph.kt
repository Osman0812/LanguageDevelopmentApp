package com.example.languagedevelopmentapp.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.languagedevelopmentapp.navigation.Screens
import com.example.languagedevelopmentapp.ui.screen.login.LoginScreen
import com.example.languagedevelopmentapp.ui.screen.register.RegisterScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        startDestination = Screens.RegisterScreen.route,
        route = Graph.AUTH
    ) {
        composable(route = Screens.LoginScreen.route) {
            LoginScreen(
                navigateTo = {
                    navController.navigate(it)
                }
            )
        }
        composable(route = Screens.RegisterScreen.route) {
            RegisterScreen(
                navigateTo = {
                    navController.navigate(it)
                },
                navigateToMain = {
                    navController.apply {
                        popBackStack()
                        navigate(Graph.MAIN)
                    }
                }

            )
        }
    }
}