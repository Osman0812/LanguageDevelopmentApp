package com.example.languagedevelopmentapp.ui.screen.main.vocabulary

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagedevelopmentapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyScreen(
    viewModel: VocabularyScreenViewModel = hiltViewModel()
) {
    val wordListState = viewModel.wordListState.collectAsState().value
    val pullToRefreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
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
                ),
            wordListState = wordListState
        )
    }
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(key1 = true) {
            scope.launch {
                isRefreshing = true
                viewModel.getWords()
                delay(3000L)
                isRefreshing = false
            }
        }
    }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            pullToRefreshState.startRefresh()
        } else {
            pullToRefreshState.endRefresh()
        }
    }
    PullToRefreshContainer(
        modifier = Modifier,
        state = pullToRefreshState
    )
}

@Composable
fun VocabularyScreenBody(
    modifier: Modifier = Modifier,
    wordListState: VocabularyUiModel
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
                repeat(wordListState.wordList.size) {
                    item {
                        SingleRowItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp),
                            wordListPair = wordListState.wordList[it],
                            hashMapList = wordListState.synonyms[it]
                        )
                        HorizontalDivider(
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
    modifier: Modifier = Modifier,
    wordListPair: Pair<String, String>,
    hashMapList: List<HashMap<String, String>>
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = wordListPair.first,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "(${wordListPair.second})",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Icon(imageVector = Icons.Outlined.Star, contentDescription = "Star Icon")
        }
        hashMapList.forEach {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = it["first"].toString(),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "(${it["second"].toString()})"
                    )
                }
            }
        }
    }
}

