package com.example.languagedevelopmentapp.ui.screen.main.practice.readingscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagedevelopmentapp.ui.screen.main.practice.prepracticescreen.PrePracticeUiModel
import kotlin.math.roundToInt

@Composable
fun ReadingScreen(
    viewModel: ReadingScreenViewModel = hiltViewModel()
) {
    val words = viewModel.words.collectAsState().value

    if (words.readingText.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {
                TextProcessing(
                    uiModel = words,
                    onTranslate = viewModel::translateModel
                )
            }
            item {
                Column {
                    Text(
                        text = "Translation",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = words.translatedReadingText)
                }

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
    uiModel: PrePracticeUiModel,
    onTranslate: (String) -> Unit
) {
    var textInput by remember { mutableStateOf(TextFieldValue(uiModel.readingText)) }
    var popupPosition by remember { mutableStateOf(Offset.Zero) }
    var selectedText2 by remember {
        mutableStateOf("")
    }
    val selectedText = remember {
        mutableStateOf("")
    }
    var translate by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        TextField(
            modifier = Modifier,
            value = textInput,
            onValueChange = { newValue ->
                textInput = newValue
                selectedText2 = textInput.getSelectedText().text
            },
            readOnly = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            interactionSource = remember {
                MutableInteractionSource()
            }
                .also { interactionSource ->
                    LaunchedEffect(key1 = interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Press) {
                                popupPosition = Offset(it.pressPosition.x, it.pressPosition.y)
                                selectedText.value = selectedText2

                            }
                        }
                    }
                }
        )
        LaunchedEffect(key1 = selectedText.value) {
            if (selectedText.value.isNotEmpty()) {
                onTranslate(selectedText.value)
                translate = !translate
            }
        }
        if (translate) {
            Log.d("translated", uiModel.translatedSelectedText)
            Text(
                text = uiModel.translatedSelectedText,
                modifier = Modifier
                    .offset {
                        IntOffset(popupPosition.x.roundToInt(), popupPosition.y.roundToInt())
                    }
                    .background(
                        shape = RoundedCornerShape(5.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    .pointerInput(Unit) {
                        detectTapGestures {
                            translate = !translate
                        }
                    },
                color = Color.Red
            )
        }
    }
}

fun calculatePopupPosition(selection: TextRange?): Offset {
    selection ?: return Offset.Zero
    val startX = selection.start
    val startY = selection.length - 100.dp.value
    return Offset(startX.toFloat(), startY)
}

@Composable
fun PopUpMessage(
    selectedWord: String,
    showPopup: Boolean,
    popupPosition: Offset,
    onClosePopup: () -> Unit
) {
    if (showPopup) {
        // Popup mesajı
        AlertDialog(
            onDismissRequest = onClosePopup,
            title = { Text(selectedWord) },
            text = { Text("Çevirisi burada olacak") },
            confirmButton = {

            },
            modifier = Modifier.offset {
                IntOffset(popupPosition.x.toInt(), popupPosition.y.toInt())
            }
        )
    }
}


/*
SelectionContainer {
    ClickableText(
        text = buildAnnotatedString {
            splitText.forEachIndexed { index, word ->
                if (index > 0) {
                    append(" ")
                }
                val color = if (wordsModel.readingTextWordsList.contains(word)) {
                    Color.Red
                } else {
                    Color.Black
                }
                withStyle(style = SpanStyle(color = color)) {
                    append(word)
                }
            }
        },
        onClick = { offset ->
            val wordStart = text.lastIndexOf(' ', offset) + 1
            val wordEnd = text.indexOf(' ', startIndex = offset).let { endIndex ->
                if (endIndex == -1) text.length else endIndex
            }
            val clickedWord = text.substring(wordStart, wordEnd)
            Log.d("selected", clickedWord)
            onProcess(clickedWord)
        },
        style = TextStyle(
            fontSize = 18.sp
        )
    )
}

 */

