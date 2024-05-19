package com.example.languagedevelopmentapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListItemCard(
    modifier: Modifier = Modifier,
    listName: String,
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .background(if (isSelected) Color.Green else Color.Transparent)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "List Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = listName,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}