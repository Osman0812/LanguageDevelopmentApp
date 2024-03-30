package com.example.languagedevelopmentapp.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions

@Composable
fun CustomLevelField(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isLevelSelected: Boolean,
) {
    Row(
        modifier = modifier
            .clickable(
                onClick = {
                    onClick()
                }
            )
            .fillMaxWidth()
            .height(ScreenDimensions.screenWidth * 0.1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(imageVector = Icons.Outlined.Person, contentDescription = "")
        Text(
            text = text,
            color = if (isLevelSelected) Color.White else Color.Black
        )
    }
}