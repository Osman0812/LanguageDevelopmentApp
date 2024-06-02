package com.example.languagedevelopmentapp.ui.screen.main.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.languagedevelopmentapp.R
import com.example.languagedevelopmentapp.navigation.graphs.Graph
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val scrollState = rememberScrollState()
    val profileState by viewModel.profileState.collectAsState()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val email = currentUser?.email
    LaunchedEffect(key1 = Unit) {
        viewModel.getUserInfo(email.toString())
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Avatar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ScreenDimensions.screenHeight * 0.3f)
            )
            PersonalInfo(
                modifier = Modifier
                    .padding(10.dp),
                profileState = profileState
            )
            ProgressInfo(
                modifier = Modifier
                    .padding(10.dp),
                profileState = profileState
            )
            WinningsPart(
                modifier = Modifier
                    .padding(10.dp),
                profileState = profileState
            )
        }

        Text(
            modifier = Modifier
                .padding(bottom = 25.dp)
                .align(Alignment.CenterHorizontally)
                .clickable(
                    onClick = {
                        navController.apply {
                            popBackStack()
                            navigate(Graph.AUTH) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                ),
            text = "Log out",
            color = MaterialTheme.colorScheme.primary
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PersonalInfo(
    modifier: Modifier = Modifier,
    profileState: ProfileUiState
) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val email = currentUser?.email
    Column(
        modifier = modifier
    ) {
        Text(
            text = profileState.name.toString(),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = "Email: ${email.toString()}")
            Text(text = "${profileState.joinDate} tarihinde katıldı")
        }
    }
}

@Composable
fun ProgressInfo(
    modifier: Modifier = Modifier,
    profileState: ProfileUiState
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
                        text = profileState.level.toString()
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
    modifier: Modifier = Modifier,
    profileState: ProfileUiState
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Başarılar",
            style = MaterialTheme.typography.displaySmall
        )
        Row {
            profileState.achievements?.forEach {
                Image(
                    modifier = Modifier
                        .size(50.dp),
                    painter = painterResource(id = it),
                    contentDescription = "Rozet"
                )
            }
        }
    }
}

