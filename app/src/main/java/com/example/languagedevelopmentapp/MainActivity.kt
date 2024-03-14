package com.example.languagedevelopmentapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.languagedevelopmentapp.navigation.graphs.RootNavGraph
import com.example.languagedevelopmentapp.ui.theme.LanguageDevelopmentAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            LanguageDevelopmentAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val currentUser = true.takeIf { auth.currentUser != null } ?: false
                    Log.d("boolean",currentUser.toString())
                    RootNavGraph(navHostController = rememberNavController(),currentUser)
                }
            }
        }
    }
}
