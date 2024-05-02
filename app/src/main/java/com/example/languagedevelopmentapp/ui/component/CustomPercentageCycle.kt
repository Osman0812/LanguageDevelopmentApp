package com.example.languagedevelopmentapp.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

@Composable
fun CustomPercentageCycle(
    successPercentage: String,
    strokeSize: Dp,
    diameter: Dp,
    strokeColor: Color
) {
    val cleanPercentageString = successPercentage.replace("%", "")
    val percentage = cleanPercentageString.toFloatOrNull()
    val progress = percentage?.div(100)

    Canvas(modifier = Modifier.size(diameter)) {
        val radius = size.minDimension / 2
        val centerX = size.width / 2
        val centerY = size.height / 2

        drawCircle(
            color = Color.LightGray,
            radius = radius,
            center = Offset(centerX, centerY),
            style = Stroke(width = strokeSize.toPx()),
            alpha = 0.5f
        )

        if (progress != null)
            drawArc(
                color = strokeColor,
                startAngle = 270f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = strokeSize.toPx())
            )
        drawCircle(
            color = Color.White,
            radius = radius - strokeSize.toPx() / 2f,
            center = Offset(centerX, centerY),
            style = Fill
        )
    }
}