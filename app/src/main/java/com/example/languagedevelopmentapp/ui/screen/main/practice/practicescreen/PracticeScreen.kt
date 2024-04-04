package com.example.languagedevelopmentapp.ui.screen.main.practice.practicescreen

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagedevelopmentapp.R
import com.example.languagedevelopmentapp.ui.component.CustomButton
import com.example.languagedevelopmentapp.ui.component.CustomTestField
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions

@Composable
fun PracticeScreen(
    navigateToBack: () -> Unit,
    viewModel: PracticeScreenViewModel = hiltViewModel()
) {
    val remainingTime = viewModel.remainingTime.collectAsState(initial = 60).value
    val questionList = viewModel.questionList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        TopScreen(
            navigateToBack,
            remainingTime = remainingTime
        )
        BodyScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp, end = 30.dp),
            questionList = questionList.value.questionList
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScreen(
    navigateToBack: () -> Unit,
    remainingTime: Int,
    modifier: Modifier = Modifier
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
                    IconButton(onClick = { navigateToBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primary)
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun BodyScreen(
    modifier: Modifier = Modifier,
    questionList: List<Question>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Question 1 of 3",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(ScreenDimensions.screenHeight * 0.25f)
        ) {
            if (questionList.isNotEmpty())
            Text(text = questionList.first().questionText)
        }
        Column(
            modifier = Modifier
                .width(ScreenDimensions.screenWidth * 0.8f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CustomTestField(
                onClick = { /*TODO*/ },
                text = "Tabi Efenim",
                containerColor = Color.Transparent,
                borderColor = MaterialTheme.colorScheme.outline,
                textColor = Color.Black
            )
            CustomTestField(
                onClick = { /*TODO*/ },
                text = "Tabi Efenim",
                containerColor = Color.Transparent,
                borderColor = MaterialTheme.colorScheme.outline,
                textColor = Color.Black
            )
            CustomTestField(
                onClick = { /*TODO*/ },
                text = "Tabi Efenim",
                containerColor = Color.Transparent,
                borderColor = MaterialTheme.colorScheme.outline,
                textColor = Color.Black
            )
            CustomTestField(
                onClick = { /*TODO*/ },
                text = "Tabi Efenim",
                containerColor = Color.Transparent,
                borderColor = MaterialTheme.colorScheme.outline,
                textColor = Color.Black
            )
        }

        CustomButton(
            modifier = Modifier
                .width(ScreenDimensions.screenWidth * 0.8f),
            onClick = { /*TODO*/ },
            text = "Next ->"
        )
    }
}
