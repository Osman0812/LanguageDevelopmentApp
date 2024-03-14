package com.example.languagedevelopmentapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.languagedevelopmentapp.navigation.graphs.RootNavGraph
import com.example.languagedevelopmentapp.ui.theme.LanguageDevelopmentAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LanguageDevelopmentAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    RootNavGraph(navHostController = rememberNavController())
                }
            }
        }
    }
}
