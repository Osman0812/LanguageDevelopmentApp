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
import com.example.languagedevelopmentapp.ui.screen.main.home.HomeScreen
import com.example.languagedevelopmentapp.ui.screen.main.navigateToBottomBarScreen
import com.example.languagedevelopmentapp.ui.screen.main.practice.PracticeScreen
import com.example.languagedevelopmentapp.ui.screen.main.profile.ProfileScreen
import com.example.languagedevelopmentapp.ui.screen.main.vocabulary.VocabularyScreen

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(
                navController = navController,
                navigateToVocabularyScreen = {
                    navigateToBottomBarScreen(navController, BottomBarScreen.Vocabulary)
                }
            )
        }
        composable(route = BottomBarScreen.Practice.route) {
            PracticeScreen(
                navigateToBack = { navController.popBackStack() }
            )
        }
        composable(route = BottomBarScreen.Vocabulary.route) {
            VocabularyScreen()
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen()
        }
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