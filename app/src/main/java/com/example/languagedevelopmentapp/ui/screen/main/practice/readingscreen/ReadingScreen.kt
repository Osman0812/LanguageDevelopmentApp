package com.example.languagedevelopmentapp.ui.screen.main.practice.readingscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagedevelopmentapp.ui.screen.main.practice.prepracticescreen.PrePracticeUiModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun ReadingScreen(
    viewModel: ReadingScreenViewModel = hiltViewModel()
) {
    val prePracticeModel = viewModel.prePracticeModel.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.getLists()
    }
    if (prePracticeModel.readingText.isNotEmpty()) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) {padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                TextProcessing(
                    uiModel = prePracticeModel,
                    onTranslate = viewModel::translateModel,
                    snackbarHostState = snackbarHostState
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
    uiModel: PrePracticeUiModel,
    onTranslate: (String) -> Unit,
    snackbarHostState: SnackbarHostState
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
    var showSnackBar by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        item {
            Box(modifier = Modifier) {
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
                        translate = true
                        showSnackBar = true
                        delay(3000)
                        translate = false
                        showSnackBar = false
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
                LaunchedEffect(key1 = showSnackBar) {
                    if (showSnackBar) {
                        val result = snackbarHostState.showSnackbar(
                            message = "Do you want to add to a list?",
                            actionLabel = "Yes",
                            withDismissAction = true
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            showDialog = true
                        }
                    }
                }

                if (showDialog) {
                    ListDialog(
                        showDialog = { showDialog = false },
                        listItems = uiModel.readingScreenLists,
                        onItemSelected = { selectedList ->
                            //addWordToList(selectedList, selectedWord.value)
                            showDialog = false
                        }
                    )
                }
            }
        }
        item {
            Column {
                Text(
                    text = "Translation",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = uiModel.translatedReadingText)
            }
        }
    }
}

@Composable
fun ListDialog(
    showDialog: () -> Unit,
    listItems: List<String>,
    onItemSelected: (String) -> Unit
) {
    Dialog(onDismissRequest = { showDialog() }) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column {
                Text(text = "Select a list")
                LazyColumn {
                    items(listItems) { listItem ->
                        Text(
                            text = listItem,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onItemSelected(listItem)
                                }
                                .padding(8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { showDialog() }) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}
