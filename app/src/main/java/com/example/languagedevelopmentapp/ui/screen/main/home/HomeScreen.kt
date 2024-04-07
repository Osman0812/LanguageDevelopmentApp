package com.example.languagedevelopmentapp.ui.screen.main.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagedevelopmentapp.R
import com.example.languagedevelopmentapp.ui.theme.ScreenDimensions

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val wordState by viewModel.wordState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.primary
            )
    ) {
        HomeScreenBody(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(10.dp)
                )
                .weight(0.9f),
            onTranslate = viewModel::translate,
            wordUiState = wordState,
            onOtherUsagesEnglish = viewModel::otherUsagesEnglish,
            onClearState = viewModel::clearState,
            onWordExample = viewModel::wordExamples,
            onSaveToFirebase = viewModel::saveToFirebase
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun HomeScreenBody(
    modifier: Modifier = Modifier,
    onTranslate: (String) -> Unit,
    onOtherUsagesEnglish: (String) -> Unit,
    wordUiState: HomeScreenUiModel,
    onWordExample: (String) -> Unit,
    onClearState: () -> Unit,
    onSaveToFirebase: (HomeScreenUiModel, List<Pair<String, String>>) -> Unit
) {
    var selectedWord by remember {
        mutableStateOf("")
    }
    var isSelected by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val text =
        " In his recent book, Return of the Bison: A Story of Survival, Restoration, and a Wilder World, Roger Di Silvestro describes how bison are being repopulated through conservation on federal public lands at national parks and national wildlife refuges, focusing on Yellowstone National Park throughout the book. He implements a narrative that tracks singular people in history that contributed to the establishment of Yellowstone National Park as a wildlife refuge for bison. In particular, he describes the events that precipitated the era of “Big Conservation” at the turn of the 20th century with executive orders by President Theodore Roosevelt and the various Congressional Acts that protected wildlife."
    val selectedText = remember {
        mutableStateOf("")
    }
    var textInput by remember { mutableStateOf(TextFieldValue(text)) }
    var selectedText2 by remember {
        mutableStateOf("")
    }
    TextField(
        modifier = modifier,
        value = textInput,
        onValueChange = { newValue ->
            textInput = newValue
            textInput.selection
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
                            Log.d("release", "true")
                            selectedText.value = selectedText2
                            Log.d("selected", selectedText2)
                        }
                    }
                }
            }
    )

    if (selectedText.value.isNotEmpty()) {
        ModalBottomSheet(
            onDismissRequest = {
                selectedText.value = ""
            },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            FooterBody(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ScreenDimensions.screenHeight * 0.9f)
                    .padding(start = 15.dp, end = 15.dp),
                word = selectedWord,
                wordUiState = wordUiState,
                onSaveToFirebase = onSaveToFirebase
            )
        }
        LaunchedEffect(key1 = Unit) {
            onTranslate(selectedText.value)
            onOtherUsagesEnglish(selectedText.value)
            onWordExample(selectedText.value)
        }
    }
    /*
    val text =
        " In his recent book, Return of the Bison: A Story of Survival, Restoration, and a Wilder World, Roger Di Silvestro describes how bison are being repopulated through conservation on federal public lands at national parks and national wildlife refuges, focusing on Yellowstone National Park throughout the book. He implements a narrative that tracks singular people in history that contributed to the establishment of Yellowstone National Park as a wildlife refuge for bison. In particular, he describes the events that precipitated the era of “Big Conservation” at the turn of the 20th century with executive orders by President Theodore Roosevelt and the various Congressional Acts that protected wildlife."
    val splitText = text.split(" ")
    var isSelected by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    FlowRow(
        modifier = modifier
            .padding(start = 15.dp, top = 5.dp, end = 15.dp)
    ) {
        splitText.forEach { text ->
            ClickableText(
                text = AnnotatedString(text = text),
                onClick = {
                    onClearState()
                    selectedWord = text
                    isSelected = true
                },
                softWrap = true,
                overflow = TextOverflow.Visible,
                style = TextStyle(lineBreak = LineBreak.Paragraph)
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
    }



    if (isSelected) {
        ModalBottomSheet(
            onDismissRequest = {
                isSelected = false
            },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.background,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            FooterBody(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ScreenDimensions.screenHeight * 0.9f)
                    .padding(start = 15.dp, end = 15.dp),
                word = selectedWord,
                wordUiState = wordUiState,
                onSaveToFirebase = onSaveToFirebase
            )
        }
        LaunchedEffect(key1 = Unit) {
            onTranslate(selectedWord)
            onOtherUsagesEnglish(selectedWord)
            onWordExample(selectedWord)
        }
    }

     */
}

@Composable
fun FooterBody(
    modifier: Modifier = Modifier,
    word: String,
    wordUiState: HomeScreenUiModel,
    onSaveToFirebase: (HomeScreenUiModel, List<Pair<String, String>>) -> Unit
) {
    val list = wordUiState.otherUsagesEnglish.zip(wordUiState.otherUsagesTurkish)
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.meaning_text),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "($word)",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (wordUiState.translate.isEmpty()) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.background
                    )
                }
                Text(
                    text = wordUiState.translate,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    modifier = Modifier
                        .size(ScreenDimensions.screenWidth * 0.1f)
                        .clickable(
                            onClick = {
                                onSaveToFirebase(wordUiState, list)
                                // navigateTo(BottomBarScreen.Vocabulary.route)
                            }
                        ),
                    painter = painterResource(id = R.drawable.ic_save_icon),
                    contentDescription = "Save Icon"
                )
            }
            Divider(
                modifier = Modifier
                    .padding(top = 10.dp),
                color = MaterialTheme.colorScheme.background,
                thickness = 1.dp
            )
        }
        item {
            Text(
                text = stringResource(id = R.string.synonyms_text),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Column {
                if (wordUiState.otherUsagesTurkish.isEmpty() || wordUiState.otherUsagesEnglish.isEmpty()) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.background
                    )
                }
                list.forEach {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = it.first,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "(${it.second})",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            Divider(
                modifier = Modifier
                    .padding(top = 10.dp),
                color = MaterialTheme.colorScheme.background,
                thickness = 1.dp
            )
        }
        item {
            Text(
                text = wordUiState.wordExampleText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}