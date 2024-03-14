package com.example.languagedevelopmentapp.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.languagedevelopmentapp.ui.screen.main.MainScreen

@Composable
fun RootNavGraph(
    navHostController: NavHostController,
    currentUser: Boolean
) {
    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = Graph.MAIN.takeIf { currentUser } ?: Graph.AUTH
    ) {
        authNavGraph(navController = navHostController)
        composable(route = Graph.MAIN) {
            MainScreen()
        }
    }
}

object Graph {
    const val ROOT = "GRAPH_ROOT"
    const val MAIN = "MAIN_GRAPH"
    const val AUTH = "AUTH_GRAPH"
    const val SUB = "SUB_GRAPH"
}