package com.example.languagedevelopmentapp.ui.screen.main.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.languagedevelopmentapp.R
import com.example.languagedevelopmentapp.ui.component.CustomProfileIcon
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions

@Composable
fun PrePracticeScreen() {
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
    modifier: Modifier = Modifier
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 20.dp),
            text = "Select what exercise you prefer?",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Item(painter = R.drawable.ic_reading)
            Item(painter = R.drawable.ic_quiz)
        }

    }
}

@Composable
fun Item(
    painter: Int
) {
    Row(
        modifier = Modifier
            .alpha(0.95f)
            .height(ScreenDimensions.screenWidth * 0.25f)
            .width(ScreenDimensions.screenWidth * 0.8f)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(start = 5.dp, end = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
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
                contentDescription = ""
            )
        }
        Icon(
            modifier = Modifier
                .size(30.dp),
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = ""
        )

    }
}


@Preview
@Composable
fun PrePracticeScreenPreview() {
    PrePracticeScreen()
}