package com.example.languagedevelopmentapp.ui.screen.login

import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.languagedevelopmentapp.R
import com.example.languagedevelopmentapp.ui.component.CustomAuthTextField
import com.example.languagedevelopmentapp.ui.component.CustomButton
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions

@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(start = 30.dp, end = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        LoginScreenDetail()
    }
}

@Composable
fun LoginScreenDetail() {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
        Spacer(modifier = Modifier.padding(ScreenDimensions.screenWidth * 0.03f))
        CustomButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {},
            text = stringResource(id = R.string.sign_in)
        )
        Row {
            Text(
                text = stringResource(id = R.string.no_acc_q),
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = stringResource(id = R.string.sign_up)
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}