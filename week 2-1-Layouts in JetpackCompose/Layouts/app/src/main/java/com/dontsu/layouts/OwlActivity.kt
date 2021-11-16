package com.dontsu.layouts

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dontsu.layouts.extension.padding
import com.dontsu.layouts.ui.theme.LayoutsTheme

val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

class OwlActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutsTheme {
                OwlBodyContent()
            }
        }
    }
}

@Composable
fun FinalLayout(context: Context) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "코트랩2주차 1차시")
                },
                actions = {
                    IconButton(onClick = { Toast.makeText(context, "버튼 클릭", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Filled.Favorite, contentDescription = null )
                    }
                }
            )
        }
    ) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = Modifier,
        content = content
    ) { measurables, constraints ->
        // 항상 여기서 자식들의 제약조건 로직을 작성함
        // 항상 중요한건 자식을 딱 한 번만 measuring 해야 한다는 것!

        // Keep track of the width of each row
        val rowWidths = IntArray(rows) { 0 }

        // Keep track of the max height of each row
        val rowHeights = IntArray(rows) { 0 }

        // 항상 순서는 1. measuring 2. sizing 3. placing
        val placeable = measurables.mapIndexed { index, measurable ->
            // 각 자식들을 측정하기
            val placeable = measurable.measure(constraints)

            // track the width and max height of each row
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = Math.max(rowHeights[row], placeable.height)

            placeable
        }

        // coerceIn을 통하여 강제 형변환
        val width = rowWidths.maxOrNull()?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth))
            ?: constraints.minWidth

        val height = rowHeights.sumOf { it }.coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // 이전 row의 축적된 hegiht로부터 각 열의 y position 구하기
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i - 1] + rowHeights[i - 1]
        }

        // screen에 배치하기 위하여 placeable.placeRelative() 사용하기
        layout(width, height) {
            val rowX = IntArray(rows) { 0 }
            placeable.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }

    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

@Composable
fun OwlBodyContent(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Row(
        modifier = modifier
            .background(color = Color.LightGray)
            .padding(16.dp) // padding, size의 순서에 따라 UI 크기가 바뀐다.
            .size(200.dp)
            .horizontalScroll(scrollState)
    ) {
        StaggeredGrid(modifier = modifier, rows = 5) {
            topics.forEach { topic ->
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }
        }
    }
}

@Preview
@Composable
fun ChipPreview() {
    Chip(text = "Hi there")
}

@Preview(showBackground = true, name = "Complex custom layout")
@Composable
fun OwlActivityPreview() {
    BodyContent()
}