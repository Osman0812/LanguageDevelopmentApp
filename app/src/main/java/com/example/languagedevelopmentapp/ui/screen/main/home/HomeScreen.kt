package com.example.languagedevelopmentapp.ui.screen.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
            onOtherUsages = viewModel::otherUsages
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenBody(
    modifier: Modifier = Modifier,
    onTranslate: (String) -> Unit,
    onOtherUsages: (String) -> Unit,
    wordUiState: HomeScreenUiModel
) {
    var selectedWord by remember {
        mutableStateOf("")
    }
    val text =
        " My name is Thomas Shelby, i am 37 yers old. Born in Birmingham in 1921."
    val splittedText = text.split(" ")
    var isSelected by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    FlowRow(
        modifier = modifier
            .padding(start = 15.dp, top = 5.dp, end = 15.dp)
    ) {
        splittedText.forEach { text ->
            ClickableText(
                text = AnnotatedString(text = text),
                onClick = {
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
                    .height(ScreenDimensions.screenHeight * 0.9f)
                    .padding(start = 15.dp, end = 15.dp),
                word = selectedWord,
                wordUiState = wordUiState
            )
        }
        LaunchedEffect(key1 = Unit) {
            onTranslate(selectedWord)
            onOtherUsages(selectedWord)
        }
    }
}

@Composable
fun FooterBody(
    modifier: Modifier = Modifier,
    word: String,
    wordUiState: HomeScreenUiModel
) {
    LazyColumn(
        modifier = modifier
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Meaning ",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "($word)",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row {
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
            }
        }
        item {
            Text(
                text = "Synonyms ",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row {
                if (wordUiState.otherUsages.isEmpty()) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.background
                    )
                }
                Text(
                    text = wordUiState.otherUsages,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }

    }

}

@Preview
@Composable
fun HomeScreenReview() {
    HomeScreen()
}