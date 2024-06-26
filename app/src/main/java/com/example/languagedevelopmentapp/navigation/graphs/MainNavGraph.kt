package com.example.languagedevelopmentapp.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.languagedevelopmentapp.navigation.BottomBarScreen
import com.example.languagedevelopmentapp.navigation.Screens
import com.example.languagedevelopmentapp.ui.screen.main.home.HomeScreen
import com.example.languagedevelopmentapp.ui.screen.main.home.HomeScreenViewModel
import com.example.languagedevelopmentapp.ui.screen.main.practice.practicescreen.PracticeScreen
import com.example.languagedevelopmentapp.ui.screen.main.practice.practicescreen.PracticeScreenViewModel
import com.example.languagedevelopmentapp.ui.screen.main.practice.prepracticescreen.PrePracticeScreen
import com.example.languagedevelopmentapp.ui.screen.main.practice.readingscreen.ReadingScreen
import com.example.languagedevelopmentapp.ui.screen.main.profile.ProfileScreen
import com.example.languagedevelopmentapp.ui.screen.main.vocabulary.VocabularyScreen
import com.example.languagedevelopmentapp.ui.screen.resultscreen.ResultScreen

@Composable
fun MainNavGraph(
    navController: NavHostController,
    viewModel: PracticeScreenViewModel = hiltViewModel(),
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {
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
                    if (it == "READING_SCREEN") {
                        navController.navigate(it)
                    } else {
                        navController.navigate(it)
                    }
                }
            )
        }
        composable(route = BottomBarScreen.Vocabulary.route) {
            VocabularyScreen()
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(
                navController = navController
            )
        }
        authNavGraph(navController = navController)
        composable(route = Screens.ReadingScreen.route) {
            ReadingScreen(
                homeScreenViewModel = homeScreenViewModel
            )
        }
        composable(route = Screens.ResultScreen.route) {
            ResultScreen(
                practiceScreenViewModel = viewModel,
                navigateToHomeScreen = {
                    navController.navigate(BottomBarScreen.Home.route)
                    {
                        popUpTo(
                            navController.graph.startDestinationRoute.toString()
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = "${Screens.PracticeScreen.route}/{testName}") {
            PracticeScreen(
                testName = it.arguments?.getString("testName").toString(),
                navigateToBack = navigateToBack,
                navigateToResultScreen = { navController.navigate(Screens.ResultScreen.route) },
                viewModel = viewModel
            )
        }
    }
}