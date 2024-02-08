package com.example.languagedevelopmentapp.ui.screen.register

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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.languagedevelopmentapp.R
import com.example.languagedevelopmentapp.navigation.Screens
import com.example.languagedevelopmentapp.ui.component.CustomAuthTextField
import com.example.languagedevelopmentapp.ui.component.CustomButton
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions

@Composable
fun RegisterScreen(
    navigateTo: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(start = 30.dp, end = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        RegisterScreenDetail(
            navigateToLogin = {
                navigateTo(Screens.LoginScreen.route)
            }
        )
    }
}

@Composable
fun RegisterScreenDetail(
    navigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRepeat by remember { mutableStateOf("") }

    Text(
        text = stringResource(id = R.string.sign_up_title),
        style = MaterialTheme.typography.displayMedium,
        color = MaterialTheme.colorScheme.primary
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomAuthTextField(
            value = name,
            onValueChange = { name = it },
            placeHolder = stringResource(id = R.string.name),
            leadingIcon = Icons.Default.AccountCircle
        )
        CustomAuthTextField(
            value = surname,
            onValueChange = { surname = it },
            placeHolder = stringResource(id = R.string.surname),
            leadingIcon = Icons.Default.AccountCircle
        )
        CustomAuthTextField(
            value = email,
            onValueChange = { email = it },
            placeHolder = stringResource(id = R.string.email),
            leadingIcon = Icons.Default.Email
        )
        CustomAuthTextField(
            value = password,
            onValueChange = { password = it },
            placeHolder = stringResource(id = R.string.password),
            leadingIcon = Icons.Default.Lock
        )
        CustomAuthTextField(
            value = passwordRepeat,
            onValueChange = { passwordRepeat = it },
            placeHolder = stringResource(id = R.string.password_repeat),
            leadingIcon = Icons.Default.Lock
        )
        Spacer(modifier = Modifier.padding(ScreenDimensions.screenWidth * 0.03f))
        CustomButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {},
            text = stringResource(id = R.string.sign_up)
        )
        Row {
            Text(
                text = stringResource(id = R.string.have_acc_q),
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                modifier = Modifier
                    .clickable(
                        onClick = {
                            navigateToLogin()
                        }
                    ),
                text = stringResource(id = R.string.sign_in)
            )
        }
    }
}
