package com.example.languagedevelopmentapp.ui.screen.main.vocabulary

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.languagedevelopmentapp.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalPagerApi::class
)
@Composable
fun VocabularyScreen(
    viewModel: VocabularyScreenViewModel = hiltViewModel()
) {
    val pagerState = com.google.accompanist.pager.rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
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
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth()
        ) {
            val titles = listOf("Words", "Lists")
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(title) }
                )
            }
        }

        HorizontalPager(
            count = 2,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> VocabularyScreenBody(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(5.dp)
                        ),
                    wordListState = wordListState
                )

                1 -> ListsScreen(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(5.dp)
                        ),
                    wordListState = wordListState,
                    onCreateList = viewModel::onCreateList,
                    onDeleteListItem = viewModel::deleteList
                )
            }
        }


    }
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(key1 = true) {
            scope.launch {
                isRefreshing = true
                viewModel.getWords()
                viewModel.getLists()
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
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        PullToRefreshContainer(
            modifier = Modifier
                .align(Alignment.Center),
            state = pullToRefreshState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListsScreen(
    modifier: Modifier = Modifier,
    wordListState: VocabularyUiModel,
    onCreateList: (String) -> Unit,
    onDeleteListItem: (String) -> Unit
) {
    var searchText by remember {
        mutableStateOf("")
    }
    val fieldBackgroundColor = MaterialTheme.colorScheme.inverseOnSurface

    var showDialog by remember { mutableStateOf(false) }
    var listName by remember { mutableStateOf(TextFieldValue("")) }
    val swipeToDismissState = rememberSwipeToDismissBoxState()
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "My Lists",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                showDialog = true
                            }
                        ),
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add Icon"
                )
            }

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
                        text = stringResource(id = R.string.search_list_field_placeholder),
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
                items(
                    items =  wordListState.listNames,
                    key = {it}
                ) { name ->
                    SwipeToDeleteContainer(
                        item = name,
                        onDelete = onDeleteListItem
                    ) {
                        ListItemCard(
                            listName = name,
                            onClick = {
                                // Handle list item click
                            }
                        )
                    }
                }
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text(text = "Create New List")
                },
                text = {
                    Column {
                        Text(text = "Enter list name:")
                        TextField(
                            value = listName,
                            onValueChange = { listName = it },
                            placeholder = { Text("List Name") }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onCreateList(listName.text)
                            showDialog = false
                        }
                    ) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {

                            showDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
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

    val filteredWordList = remember(searchText, wordListState.wordList) {
        wordListState.wordList.filter { pair ->
            pair.first.contains(searchText, ignoreCase = true) || pair.second.contains(
                searchText,
                ignoreCase = true
            )
        }
    }
    val filteredSynonyms = remember(searchText, wordListState.synonyms) {
        wordListState.synonyms.filterIndexed { index, _ ->
            wordListState.wordList[index].first.contains(searchText, ignoreCase = true) ||
                    wordListState.wordList[index].second.contains(searchText, ignoreCase = true)
        }
    }
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
                items(filteredWordList.size) { index ->
                    SingleRowItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        wordListPair = filteredWordList[index],
                        hashMapList = filteredSynonyms[index]
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )
    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }
    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = state,
            backgroundContent = {
                DeleteBackground(swipeDismissState = state)
            },
            content = { content(item) },
            enableDismissFromEndToStart = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(swipeDismissState: SwipeToDismissBoxState) {
    val color = if (swipeDismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
        Color.Red
    } else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(color, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Icon",
            tint = Color.White
        )

    }
}


@Composable
fun ListItemCard(
    listName: String,
    onClick: () -> Unit,

    ) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
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

