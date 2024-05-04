package com.example.languagedevelopmentapp.ui.screen.resultscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.languagedevelopmentapp.ui.component.CustomButton
import com.example.languagedevelopmentapp.ui.component.CustomPercentageCycle
import com.example.languagedevelopmentapp.ui.screen.main.practice.practicescreen.PracticeScreenViewModel
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions.Companion.screenHeight
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions.Companion.screenWidth

@Composable
fun ResultScreen(
    viewModel: PracticeScreenViewModel,
    navigateToHomeScreen: () -> Unit
) {
    val resultState = viewModel.resultScreenUiState.collectAsState().value
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, bottom = 10.dp, start = 25.dp, end = 25.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (resultState.correctCount != null) {
                Box {
                    CustomPercentageCycle(
                        successPercentage = (resultState.correctCount.times(10)).toString(),
                        strokeSize = screenWidth * 0.1f,
                        diameter = screenWidth * 0.8f,
                        strokeColor = if (resultState.correctCount >= 5) Color.Green else Color.Red
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "%${(resultState.correctCount.times(10))}",
                            style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )
                        Text(
                            text = "Başarı Oranı",
                            color = Color.Black
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Doğru Sayısı: ${resultState.correctCount}",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Text(
                        text = "Yanlış Sayısı: ${10 - resultState.correctCount}",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Spacer(modifier = Modifier.height(screenHeight * 0.08f))
                    CustomButton(
                        Modifier
                            .width(screenWidth * 0.8f)
                            .align(Alignment.CenterHorizontally),
                        onClick = {
                            navigateToHomeScreen()

                        },
                        text = "Bitir"
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(text = "Sonuçlar Yükleniyor...")
                }
            }
        }
    }
}

@Composable
fun BadgeAlert(
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        confirmButton = {

        }
    )
}