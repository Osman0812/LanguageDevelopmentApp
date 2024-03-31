package com.example.languagedevelopmentapp.ui.screen.main.practice.prepracticescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagedevelopmentapp.R
import com.example.languagedevelopmentapp.navigation.Screens
import com.example.languagedevelopmentapp.ui.component.CustomButton
import com.example.languagedevelopmentapp.ui.component.CustomLevelField
import com.example.languagedevelopmentapp.ui.component.CustomProfileIcon
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions

@Composable
fun PrePracticeScreen(
    viewModel: PrePracticeScreenViewModel = hiltViewModel(),
    navigateTo: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        PrePracticeScreenBody(
            modifier = Modifier
                .fillMaxWidth()
                .height(ScreenDimensions.screenHeight * 0.75f)
                .align(Alignment.BottomCenter)
                .padding(10.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(15.dp)
                ),
            navigateTo = navigateTo
        )
        PrePracticeTop(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
        )
    }
}

@Composable
fun PrePracticeTop(
    modifier: Modifier = Modifier,

    ) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CustomProfileIcon(
            modifier = Modifier
                .size(ScreenDimensions.screenWidth * 0.2f),
            painter = R.drawable.ic_english
        )
        CustomProfileIcon(
            modifier = Modifier
                .size(ScreenDimensions.screenWidth * 0.2f)
        )
    }
}

@Composable
fun PrePracticeScreenBody(
    modifier: Modifier = Modifier,
    navigateTo: (String) -> Unit
) {
    var isReadingSelected by remember { mutableStateOf(false) }
    var isQuizSelected by remember { mutableStateOf(false) }
    var isShowDialog by remember { mutableStateOf(false) }
    val levelList = listOf(
        "Beginner",
        "Elementary",
        "Pre-Intermediate",
        "Intermediate",
        "Upper-Intermediate",
        "Advanced"
    )
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 20.dp),
            text = "Select what exercise you prefer?",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Item(
                painter = R.drawable.ic_reading,
                itemName = stringResource(id = R.string.reading_exercise_text),
                onItemClick = {
                    isReadingSelected = !isReadingSelected
                    isQuizSelected = false
                    isShowDialog = false
                },
                isOtherItemClicked = isQuizSelected
            )
            Item(
                painter = R.drawable.ic_quiz,
                itemName = stringResource(id = R.string.quiz_challenge_text),
                onItemClick = {
                    isQuizSelected = !isQuizSelected
                    isReadingSelected = false
                    isShowDialog = false
                },
                isOtherItemClicked = isReadingSelected
            )
        }
        CustomButton(
            modifier = Modifier
                .width(ScreenDimensions.screenWidth * 0.3f),
            onClick = {
                isShowDialog = !isShowDialog
                navigateTo(Screens.ReadingScreen.route)
            },
            text = stringResource(id = R.string.start_text),
            isEnabled = true.takeIf { isReadingSelected || isQuizSelected } ?: false
        )
        if (isShowDialog && isQuizSelected) {
            Dialog(
                modifier = Modifier
                    .height(ScreenDimensions.screenHeight * 0.6f)
                    .width(ScreenDimensions.screenWidth * 0.75f)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(10.dp),
                onChangeIsShowDialog = { isShowDialog = it },
                levelList = levelList,
                isQuizSelected = isQuizSelected
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dialog(
    modifier: Modifier = Modifier,
    onChangeIsShowDialog: (Boolean) -> Unit,
    levelList: List<String> = emptyList(),
    isQuizSelected: Boolean
) {
    var selectedLevel: String by remember { mutableStateOf("") }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onChangeIsShowDialog(false) }
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                levelList.forEach {
                    CustomLevelField(
                        modifier = Modifier
                            .background(
                                color = if (selectedLevel == it) Color.Green else Color.White,
                                shape = RoundedCornerShape(5.dp)
                            ),
                        text = it,
                        onClick = {
                            selectedLevel = it
                        },
                        isLevelSelected = selectedLevel == it,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                if (isQuizSelected) {
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        thickness = 1.dp
                    )
                    CustomLevelField(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .background(
                                color = if (selectedLevel == "test") Color.Green else Color.White,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .border(
                                color = if (selectedLevel == "test") Color.Black else Color.Green,
                                shape = RoundedCornerShape(5.dp),
                                width = 1.5.dp
                            ),
                        text = stringResource(id = R.string.level_determination_text),
                        onClick = {
                            selectedLevel = "test"
                        },
                        isLevelSelected = selectedLevel == "test",
                    )
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.screenWidth * 0.07f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomButton(
                    onClick = { },
                    text = stringResource(id = R.string.start_text),
                    isEnabled = selectedLevel.isNotEmpty()
                )
                Text(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                onChangeIsShowDialog(false)
                            }
                        ),
                    text = stringResource(id = R.string.close_text)
                )
            }
        }
    }
}

@Composable
fun Item(
    painter: Int,
    itemName: String,
    onItemClick: () -> Unit,
    isOtherItemClicked: Boolean
) {
    var isClicked by remember {
        mutableStateOf(false)
    }
    if (isOtherItemClicked && isClicked) {
        isClicked = false
    }
    Row(
        modifier = Modifier
            .alpha(0.95f)
            .height(ScreenDimensions.screenWidth * 0.25f)
            .width(ScreenDimensions.screenWidth * 0.8f)
            .background(
                color = if (isClicked) Color.Gray else MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(15.dp),
            )
            .border(
                width = if (isClicked) 2.dp else 0.dp,
                color = Color.Black,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(start = 5.dp, end = 10.dp)
            .clickable(
                onClick = {
                    onItemClick()
                    isClicked = !isClicked
                }
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .alpha(0.8f)
                    .size(ScreenDimensions.screenWidth * 0.2f)
                    .background(
                        color = MaterialTheme.colorScheme.onSecondary,
                        shape = RoundedCornerShape(15.dp)
                    )
            ) {
                Icon(
                    modifier = Modifier
                        .size(ScreenDimensions.screenWidth * 0.2f),
                    painter = painterResource(id = painter),
                    contentDescription = "Item Icon"
                )
            }
            Text(
                text = itemName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiaryContainer
            )
        }
        Icon(
            modifier = Modifier
                .size(30.dp),
            imageVector = Icons.Outlined.CheckCircle.takeIf { !isClicked }
                ?: Icons.Filled.CheckCircle,
            contentDescription = "Check Icon"
        )
    }
}
