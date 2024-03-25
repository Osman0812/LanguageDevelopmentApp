package com.example.languagedevelopmentapp.ui.screen.main.vocabulary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.languagedevelopmentapp.R
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions

@Composable
fun VocabularyScreen(
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.primary
            )
    ) {
        VocabularyScreenBody(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(5.dp)
                )
        )
    }
}

@Composable
fun VocabularyScreenBody(
    modifier: Modifier = Modifier
) {
    var searchText by remember {
        mutableStateOf("")
    }
    val fieldBackgroundColor = MaterialTheme.colorScheme.inverseOnSurface
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Start),
                text = "My Words",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = searchText,
                onValueChange = { searchText = it },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "TextField Search Icon"
                    )
                },
                placeholder = {
                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.search_text_field_placeholder),
                        textAlign = TextAlign.Justify
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = fieldBackgroundColor,
                    unfocusedContainerColor = fieldBackgroundColor,
                    disabledContainerColor = fieldBackgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )

            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                repeat(25) {
                    item {
                        SingleRowItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp)
                                .height(ScreenDimensions.screenWidth * 0.15f)
                        )
                        Divider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SingleRowItem(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "A Word",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "(Meaning)",
                color = MaterialTheme.colorScheme.primary
            )
        }
        Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = "")
    }
}
