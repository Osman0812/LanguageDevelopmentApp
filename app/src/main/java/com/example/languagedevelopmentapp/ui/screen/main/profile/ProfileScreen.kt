package com.example.languagedevelopmentapp.ui.screen.main.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.languagedevelopmentapp.R
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions

@Composable
fun ProfileScreen() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
    ) {

        Avatar(
            modifier = Modifier
                .fillMaxWidth()
                .height(ScreenDimensions.screenHeight * 0.3f)
        )
        PersonalInfo(
            modifier = Modifier
                .padding(10.dp)
        )
        ProgressInfo(
            modifier = Modifier
                .padding(10.dp)
        )
        WinningsPart(
            modifier = Modifier
                .padding(10.dp)
        )
    }

}

@Composable
fun Avatar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.ic_avatar),
            contentDescription = "Avatar"
        )
    }
}

@Composable
fun PersonalInfo(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Osman",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = "Email")
            Text(text = "Join Date tarihinde katıldı")
        }
    }
}

@Composable
fun ProgressInfo(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Progress",
            style = MaterialTheme.typography.displaySmall
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(ScreenDimensions.screenHeight * 0.1f)
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(5.dp),
                    color = MaterialTheme.colorScheme.outline
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.CenterStart)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Text(text = "Level:")
                    Text(
                        text = "Beginner"
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Text(text = "Days in a row:")
                    Text(text = "5")
                }
            }
        }
    }
}

@Composable
fun WinningsPart(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Başarılar",
            style = MaterialTheme.typography.displaySmall
        )
        Row {
            Image(
                modifier = Modifier
                    .size(50.dp),
                painter = painterResource(id = R.drawable.ic_badge_1),
                contentDescription = "Rozet"
            )
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}