package com.example.languagedevelopmentapp.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.languagedevelopmentapp.navigation.BottomBarScreen
import com.example.languagedevelopmentapp.navigation.Screens
import com.example.languagedevelopmentapp.ui.screen.main.practice.PracticeScreen

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = Graph.SUB
    ) {
        composable(route = BottomBarScreen.Practice.route) {
            PracticeScreen(
                navigateToBack = { navController.popBackStack() }
            )
        }
        subNavGraph(navController)
    }
}

private fun NavGraphBuilder.subNavGraph(navController: NavController) {
    navigation(
        route = Graph.SUB,
        startDestination = Screens.PracticeScreen.route
    ) {
        composable(route = Screens.PracticeScreen.route) {
            PracticeScreen(
                navigateToBack = { navController.popBackStack() }
            )
        }
    }
}