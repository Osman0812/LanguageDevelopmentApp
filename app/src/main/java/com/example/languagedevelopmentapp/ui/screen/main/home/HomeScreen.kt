package com.example.languagedevelopmentapp.ui.screen.main.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
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
                .weight(0.9f)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenBody(
    modifier: Modifier = Modifier
) {
    val text =
        " Selected Text Selam ben nasılsın iyim privet asdsadsaasdasdas Selected Text Selam ben nasılsın iyim privet nataşa Selected Text Selam ben nasılsın iyim privet nataşa"
    val splittedText = text.split(" ")
    var isSelected by remember { mutableStateOf(false) }
    val qrCodeSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    FlowRow(
        modifier = modifier
            .padding(start = 15.dp, top = 5.dp, end = 15.dp)
    ) {
        splittedText.forEach { text ->
            ClickableText(
                text = AnnotatedString(text = text),
                onClick = {
                    Log.d("selected", text)
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
            onDismissRequest = { isSelected = false },
            sheetState = qrCodeSheetState,
            containerColor = MaterialTheme.colorScheme.primary,
        ) {
            FooterBody()
        }
    }
}
@Composable
fun FooterBody(
    modifier: Modifier = Modifier
) {
    Text(text = "Selected Text Selam ben nasılsın iyim privet asdsadsaasdasdas Selected Text Selam be Selected Text Selam ben nasılsın iyim privet asdsadsaasdasdas Selected Text Selam be")

}

@Preview
@Composable
fun HomeScreenReview() {
    HomeScreen()
}