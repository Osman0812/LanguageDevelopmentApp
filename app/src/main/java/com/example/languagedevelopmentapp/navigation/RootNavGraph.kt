package com.example.languagedevelopmentapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.languagedevelopmentapp.ui.screen.login.LoginScreen

@Composable
fun RootNavGraph(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = Graph.AUTH
    ) {
        authNavGraph(navController = navHostController)
        composable(route = Graph.AUTH) {
            LoginScreen()
        }
    }
}

object Graph {
    const val ROOT = "GRAPH_ROOT"
    const val MAIN = "MAIN_GRAPH"
    const val AUTH = "AUTH_GRAPH"
}