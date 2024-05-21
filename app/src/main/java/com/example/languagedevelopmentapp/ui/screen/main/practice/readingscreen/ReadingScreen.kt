package com.example.languagedevelopmentapp.ui.screen.main.practice.readingscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagedevelopmentapp.ui.component.ListItemCard
import com.example.languagedevelopmentapp.ui.screen.main.home.HomeScreenUiModel
import com.example.languagedevelopmentapp.ui.screen.main.home.HomeScreenViewModel
import com.example.languagedevelopmentapp.ui.screen.main.practice.prepracticescreen.PrePracticeUiModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun ReadingScreen(
    viewModel: ReadingScreenViewModel = hiltViewModel(),
    homeScreenViewModel: HomeScreenViewModel
) {
    val wordState by homeScreenViewModel.wordState.collectAsState()
    val prePracticeModel = viewModel.prePracticeModel.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.getLists()
    }
    if (prePracticeModel.readingText.isNotEmpty()) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                TextProcessing(
                    uiModel = prePracticeModel,
                    onTranslate = homeScreenViewModel::translateModel,
                    snackbarHostState = snackbarHostState,
                    onAddWordToList = viewModel::addWordToList,
                    onSaveWordToWords = homeScreenViewModel::otherUsagesEnglish,
                    wordUiState = wordState,
                    onSaveToFirebase = homeScreenViewModel::saveToFirebase
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
    snackbarHostState: SnackbarHostState,
    onAddWordToList: (String, String) -> Unit,
    onSaveWordToWords: (String) -> Unit,
    wordUiState: HomeScreenUiModel,
    onSaveToFirebase: (HomeScreenUiModel, List<Pair<String, String>>) -> Unit
) {
    val list = wordUiState.otherUsagesEnglish?.zip(wordUiState.otherUsagesTurkish)
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
                                        popupPosition =
                                            Offset(it.pressPosition.x, it.pressPosition.y)
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
                        text = wordUiState.translate,
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    popupPosition.x.roundToInt(),
                                    popupPosition.y.roundToInt()
                                )
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
                            onAddWordToList(selectedList, selectedText.value)
                            onSaveWordToWords(selectedText.value)
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
    LaunchedEffect(key1 = list) {
        if (!list.isNullOrEmpty()) {
            Log.d("wordState",wordUiState.word)
            Log.d("list",list.toString())
            onSaveToFirebase(wordUiState, list)
        }
    }
}

@Composable
fun ListDialog(
    showDialog: () -> Unit,
    listItems: List<String>,
    onItemSelected: (String) -> Unit
) {
    var isListSelected by remember {
        mutableStateOf(false)
    }
    var listItem by remember {
        mutableStateOf("")
    }
    var selectedItemIndex by remember {
        mutableStateOf(-1)
    }
    Dialog(onDismissRequest = { showDialog() }) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column {
                Text(text = "Select a list")
                LazyColumn {
                    itemsIndexed(listItems) { index, listName ->
                        ListItemCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            listName = listName,
                            onClick = {
                                selectedItemIndex = index
                            },
                            isSelected = index == selectedItemIndex
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            if (selectedItemIndex != -1) {
                                onItemSelected(listItems[selectedItemIndex])
                                showDialog()
                            }
                        }
                    ) {
                        Text(text = "Add")
                    }
                    Button(
                        onClick = {
                            showDialog()
                        }
                    ) {
                        Text(text = "Cancel")
                    }
                }

            }
        }
    }
}
