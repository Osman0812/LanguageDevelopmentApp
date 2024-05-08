package com.example.languagedevelopmentapp.ui.screen.resultscreen

import android.util.Log
import androidx.collection.MutableIntList
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagedevelopmentapp.R
import com.example.languagedevelopmentapp.ui.component.CustomButton
import com.example.languagedevelopmentapp.ui.component.CustomPercentageCycle
import com.example.languagedevelopmentapp.ui.screen.main.practice.practicescreen.PracticeScreenViewModel
import com.example.languagedevelopmentapp.ui.screen.main.practice.prepracticescreen.PrePracticeScreenViewModel
import com.example.languagedevelopmentapp.ui.screen.main.profile.ProfileUiState
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions.Companion.screenHeight
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions.Companion.screenWidth

@Composable
fun ResultScreen(
    practiceScreenViewModel: PracticeScreenViewModel,
    prePracticeScreenViewModel: PrePracticeScreenViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit
) {
    val resultState = practiceScreenViewModel.resultScreenUiState.collectAsState().value
    val userInfo by prePracticeScreenViewModel.userInfo.collectAsState()
    var isShowDialog by remember { mutableStateOf(false) }
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
                            isShowDialog = !isShowDialog
                            //navigateToHomeScreen()
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
        if (isShowDialog && userInfo.level == "Unknown") {
            BadgeAlert(
                modifier = Modifier
                    .alpha(0.9f)
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                onChangeIsShowDialog = { isShowDialog = it },
                userLevel = userInfo.level.toString(),
                navigateToHomeScreen = navigateToHomeScreen,
                onUpdateUserInfo = prePracticeScreenViewModel::saveInfoToFirebase
            )
        }
    }
}

@Composable
fun BadgeAlert(
    modifier: Modifier = Modifier,
    onChangeIsShowDialog: (Boolean) -> Unit,
    userLevel: String,
    navigateToHomeScreen: () -> Unit,
    onUpdateUserInfo: (ProfileUiState) -> Unit
) {
    val listAch = emptyList<Int>().toMutableList()
    Log.d("userInfo", userLevel)
    androidx.compose.ui.window.Dialog(
        onDismissRequest = {
            if (userLevel == "Unknown") {
                listAch.add(R.drawable.ic_badge_1)
                onUpdateUserInfo(ProfileUiState(level = "Beginner", achievements = listAch))
            }
            onChangeIsShowDialog(false)
            navigateToHomeScreen()
        }
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center
        ) {
            if (userLevel == "Unknown") {
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Tebrikler!",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "Kazanılan Başarılar:")
                Spacer(modifier = Modifier.height(10.dp))
                Image(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .size(80.dp),
                    painter = painterResource(id = R.drawable.ic_badge_1),
                    contentDescription = "First Badge"
                )
            }
        }
    }
}