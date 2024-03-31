package com.example.languagedevelopmentapp.ui.screen.main.practice.readingscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ReadingScreen(
    viewModel: ReadingScreenViewModel = hiltViewModel()
) {
    val words = viewModel.words.collectAsState().value
    if (words.readingText.isNotEmpty()) {
        LazyColumn {
            item {
                TextProcessing(
                    text = words.readingText,
                    onProcess = viewModel::processText
                )
            }
        }

    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun TextProcessing(
    modifier: Modifier = Modifier,
    text: String,
    onProcess: (String) -> Unit
) {
    val splitText = text.split(" ")
    ClickableText(
        text = buildAnnotatedString {
            splitText.forEachIndexed { index, word ->
                if (index > 0) {
                    append(" ")
                }
                append(word)
            }
        },
        onClick = { offset ->
            val wordStart = text.lastIndexOf(' ', offset) + 1
            val wordEnd = text.indexOf(' ', startIndex = offset).let { endIndex ->
                if (endIndex == -1) text.length else endIndex
            }
            val clickedWord = text.substring(wordStart, wordEnd)
            onProcess(clickedWord)
        },
        modifier = modifier.padding(8.dp),
        style = TextStyle(
            color = Color.Black,
            fontSize = 18.sp
        )
    )
}