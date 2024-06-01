package com.example.languagedevelopmentapp.ui.screen.main.practice.practicescreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.languagedevelopmentapp.R
import com.example.languagedevelopmentapp.ui.component.CustomButton
import com.example.languagedevelopmentapp.ui.component.CustomTestField
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions

@Composable
fun PracticeScreen(
    testName: String,
    navigateToBack: () -> Unit,
    navigateToResultScreen: () -> Unit,
    viewModel: PracticeScreenViewModel
) {
    val remainingTime = viewModel.remainingTime.collectAsState(initial = 60).value
    val questionList = viewModel.questionList.collectAsState()
    val result = viewModel.resultScreenUiState.collectAsState()
    Log.d("testName", testName)
    LaunchedEffect(key1 = Unit) {
        viewModel.getQuestions(testName = testName)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        TopScreen(
            navigateToBack,
            remainingTime = remainingTime,
            onClearState = viewModel::clearState
        )
        BodyScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp, end = 30.dp),
            questionList = questionList.value.questionList,
            onStartCountDown = viewModel::startCountdown,
            navigateToResultScreen = navigateToResultScreen,
            onGetUserResult = viewModel::getUserLevel,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScreen(
    navigateToBack: () -> Unit,
    remainingTime: Int,
    modifier: Modifier = Modifier,
    onClearState: () -> Unit
) {
    Column(
        modifier = modifier
            .height(ScreenDimensions.screenWidth * 0.15f),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    IconButton(onClick = {
                        navigateToBack()
                        onClearState()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    text = "Aplitude Test"
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_timer),
                    contentDescription = "Timer"
                )
                Text(text = remainingTime.toString())
            }
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun BodyScreen(
    modifier: Modifier = Modifier,
    questionList: List<Question>,
    onStartCountDown: (Int) -> Unit,
    navigateToResultScreen: () -> Unit,
    onGetUserResult: (List<String>) -> Unit,
) {
    var questionNo by remember {
        mutableIntStateOf(0)
    }
    var isNextClicked by remember {
        mutableStateOf(false)
    }
    var selectedOptionIndex by remember { mutableIntStateOf(-1) }
    val selectedOptions = remember { mutableStateListOf<String>() }
    LaunchedEffect(key1 = questionNo) {
        isNextClicked = false
    }
    if (questionNo == questionList.size - 1) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "You Completed The Test!",
                style = MaterialTheme.typography.displayMedium
            )
            CustomButton(
                onClick = {
                    navigateToResultScreen()
                    onGetUserResult(selectedOptions.toList())
                },
                text = "See Results"
            )
        }
    } else {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (questionList.isNotEmpty() && questionNo < questionList.size) {
                Text(
                    text = "Question ${questionNo + 1} of ${questionList.size - 1}",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ScreenDimensions.screenHeight * 0.25f)
                ) {
                    if (questionList.isNotEmpty() && questionNo < questionList.size)
                        Column {
                            Text(text = questionList[questionNo].questionText)
                            if (questionList[questionNo].questionText2.isNotEmpty()) {
                                Text(text = questionList[questionNo].questionText2)
                            }
                        }
                }
                Column(
                    modifier = Modifier
                        .width(ScreenDimensions.screenWidth * 0.8f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    if (questionList.isNotEmpty() && questionNo < questionList.size && questionList[questionNo].answerOptions.isNotEmpty()) {
                        for (index in questionList[questionNo].answerOptions.indices) {
                            val option = questionList[questionNo].answerOptions[index]
                            CustomTestField(
                                onClick = {
                                    selectedOptions.add(option)
                                    selectedOptionIndex = index
                                },
                                text = option.takeIf { it.isNotEmpty() } ?: "",
                                containerColor = if (selectedOptionIndex == index) Color.Green else Color.Transparent,
                                borderColor = MaterialTheme.colorScheme.outline,
                                textColor = Color.Black
                            )
                        }
                    }
                }
            } else {
                CircularProgressIndicator()
            }
            Row {
                CustomButton(
                    modifier = Modifier
                        .width(ScreenDimensions.screenWidth * 0.8f),
                    onClick = {
                        if (questionNo < questionList.size - 1) {
                            questionNo += 1
                            isNextClicked = true
                            onStartCountDown(60)
                        }
                        selectedOptionIndex = -1
                    },
                    text = "Next ->"
                )
            }
        }
    }
}