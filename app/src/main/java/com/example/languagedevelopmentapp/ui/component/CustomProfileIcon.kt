package com.example.languagedevelopmentapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.languagedevelopmentapp.R


@Composable
fun CustomProfileIcon(
    modifier: Modifier = Modifier,
    painter: Int = R.drawable.ic_profile_img

) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(35.dp)
            )

    ) {
        AsyncImage(
            model = painter,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Fit
        )
    }
}

