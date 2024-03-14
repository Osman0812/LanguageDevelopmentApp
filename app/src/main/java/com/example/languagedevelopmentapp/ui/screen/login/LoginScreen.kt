package com.example.languagedevelopmentapp.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagedevelopmentapp.R
import com.example.languagedevelopmentapp.navigation.Screens
import com.example.languagedevelopmentapp.ui.component.CustomAuthTextField
import com.example.languagedevelopmentapp.ui.component.CustomButton
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = hiltViewModel(),
    navigateTo: (String) -> Unit,
    navigateToMain: () -> Unit
) {
    val loginState by viewModel.loginState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(start = 30.dp, end = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        LoginScreenDetail(
            navigateToRegister = {
                navigateTo(Screens.RegisterScreen.route)
            },
            loginState = loginState,
            viewModel::login,
            navigateToMain = navigateToMain,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
        )
    }
}

@Composable
fun LoginScreenDetail(
    navigateToRegister: () -> Unit,
    loginState: LoginUiState,
    onLogin: () -> Unit,
    navigateToMain: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
) {
    Text(
        text = stringResource(id = R.string.sign_in_title),
        style = MaterialTheme.typography.displayMedium,
        color = MaterialTheme.colorScheme.primary
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomAuthTextField(
            value = loginState.email,
            onValueChange = { onEmailChange(it) },
            placeHolder = stringResource(id = R.string.email),
            leadingIcon = Icons.Default.Email
        )
        CustomAuthTextField(
            value = loginState.password,
            onValueChange = { onPasswordChange(it) },
            placeHolder = stringResource(id = R.string.password),
            leadingIcon = Icons.Default.Lock
        )
        Spacer(modifier = Modifier.padding(ScreenDimensions.screenWidth * 0.03f))
        CustomButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onLogin()
            },
            text = stringResource(id = R.string.sign_in)
        )
        Row {
            Text(
                text = stringResource(id = R.string.no_acc_q),
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                modifier = Modifier
                    .clickable(
                        onClick = {
                            navigateToRegister()
                        }
                    ),
                text = stringResource(id = R.string.sign_up)

            )
        }
    }
    LaunchedEffect(key1 = loginState) {
        if (loginState.isLogged == true) {
            navigateToMain()
        }
    }
}
