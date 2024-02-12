package com.example.languagedevelopmentapp.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.languagedevelopmentapp.navigation.BottomBarScreen
import com.example.languagedevelopmentapp.navigation.graphs.MainNavGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    val screens = listOf(
        BottomBarScreen.Vocabulary,
        BottomBarScreen.Home,
        BottomBarScreen.Practice,
        BottomBarScreen.Profile
    )
    Scaffold(
        bottomBar = {
            NavigationBar {
                screens.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        label = {
                            Text(text = screen.title)
                        },
                        selected = false,
                        onClick = { /*TODO*/ },
                        icon = {
                            BadgedBox(
                                badge = {

                                }) {
                                Icon(imageVector = screen.icon, contentDescription = "")
                            }
                        }
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = it.calculateBottomPadding()
                )
        ) {
            MainNavGraph(navController = navController)
        }
    }
}


