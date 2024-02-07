package com.example.languagedevelopmentapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class ScreenDimensions {
    companion object {

        val screenWidth: Dp
            @Composable
            @NonRestartableComposable
            @ReadOnlyComposable
            get() = LocalConfiguration.current.screenWidthDp.dp

        val screenHeight: Dp
            @Composable
            get() = LocalConfiguration.current.screenHeightDp.dp

    }
}