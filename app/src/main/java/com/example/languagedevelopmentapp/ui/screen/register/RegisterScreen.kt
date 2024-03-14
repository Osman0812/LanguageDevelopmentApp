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
import com.example.languagedevelopmentapp.ui.component.CustomDialog
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions

@Composable
fun RegisterScreen(
    navigateTo: (String) -> Unit,
    navigateToMain: () -> Unit,
    viewModel: RegisterScreenViewModel = hiltViewModel()
) {
    val registerState by viewModel.registerState.collectAsState()
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
            },
            onRegister = viewModel::register,
            registerState = registerState,
            onInputChange = viewModel::onInputChange,
            onClearState = viewModel::clearState,
            navigateToMain = navigateToMain
        )
    }
}

@Composable
fun RegisterScreenDetail(
    navigateToLogin: () -> Unit,
    onClearState: () -> Unit,
    onRegister: () -> Unit,
    registerState: RegisterUiState,
    onInputChange: (RegisterInputField, Any) -> Unit,
    navigateToMain: () -> Unit
) {
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
            value = registerState.name,
            onValueChange = { onInputChange(RegisterInputField.Name, it) },
            placeHolder = stringResource(id = R.string.name),
            leadingIcon = Icons.Default.AccountCircle
        )
        CustomAuthTextField(
            value = registerState.surname,
            onValueChange = { onInputChange(RegisterInputField.Surname, it) },
            placeHolder = stringResource(id = R.string.surname),
            leadingIcon = Icons.Default.AccountCircle
        )
        CustomAuthTextField(
            value = registerState.email,
            onValueChange = { onInputChange(RegisterInputField.Email, it) },
            placeHolder = stringResource(id = R.string.email),
            leadingIcon = Icons.Default.Email
        )
        CustomAuthTextField(
            value = registerState.password,
            onValueChange = { onInputChange(RegisterInputField.Password, it) },
            placeHolder = stringResource(id = R.string.password),
            leadingIcon = Icons.Default.Lock
        )
        CustomAuthTextField(
            value = registerState.passwordRepeat,
            onValueChange = { onInputChange(RegisterInputField.PasswordRepeat, it) },
            placeHolder = stringResource(id = R.string.password_repeat),
            leadingIcon = Icons.Default.Lock
        )
        Spacer(modifier = Modifier.padding(ScreenDimensions.screenWidth * 0.03f))
        CustomButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { onRegister() },
            text = stringResource(id = R.string.sign_up)
        )
        if (registerState.successMessage != null) {
            CustomDialog(
                text = registerState.successMessage,
                title = stringResource(id = R.string.register_alert_dialog_title),
                onClick = onClearState
            )
            navigateToMain()
        }
        if (registerState.errorMessage != null) {
            CustomDialog(
                text = registerState.errorMessage,
                title = stringResource(id = R.string.register_alert_dialog_title),
                onClick = { onClearState() }
            )
        }
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
