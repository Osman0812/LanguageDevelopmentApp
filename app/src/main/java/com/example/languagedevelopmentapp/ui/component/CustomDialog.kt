package com.example.languagedevelopmentapp.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    text: String,
    title: String,
    onClick: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        title = { Text(text = title) },
        text = { Text(text = text) },
        onDismissRequest = { onClick() },
        confirmButton = {
            TextButton(onClick = { onClick() }) {
                Text(text = "kapat")
            }
        }
    )
}