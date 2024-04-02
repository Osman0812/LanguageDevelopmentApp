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
import com.example.languagedevelopmentapp.ui.screen.main.practice.practicescreen.PracticeScreen
import com.example.languagedevelopmentapp.ui.screen.main.practice.prepracticescreen.PrePracticeScreen
import com.example.languagedevelopmentapp.ui.screen.main.practice.readingscreen.ReadingScreen
import com.example.languagedevelopmentapp.ui.screen.main.profile.ProfileScreen
import com.example.languagedevelopmentapp.ui.screen.main.vocabulary.VocabularyScreen

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = BottomBarScreen.Home.route
    ) {
        val navigateToBack = {
            val backStackEntry = navController.previousBackStackEntry
            if (backStackEntry != null) {
                navController.popBackStack()
            }
        }
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen()
        }
        composable(route = BottomBarScreen.Practice.route) {
            PrePracticeScreen(
                navigateTo = {
                    if (it == "READING_SCREEN"){
                        navController.navigate(Screens.ReadingScreen.route)
                    }else {
                        navController.navigate(Screens.PracticeScreen.route)
                    }
                }
            )
        }
        composable(route = BottomBarScreen.Vocabulary.route) {
            VocabularyScreen()
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen()
        }
        composable(route = Screens.ReadingScreen.route) {
            ReadingScreen()
        }
        composable(route = Screens.PracticeScreen.route) {
            PracticeScreen(
                navigateToBack = navigateToBack
            )
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