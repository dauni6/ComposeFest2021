package com.dontsu.layouts

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.dontsu.layouts.extension.firstBaseLineToTop
import com.dontsu.layouts.ui.theme.LayoutsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
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
fun BodyContent(modifier: Modifier = Modifier) {
    MyOwnColumn(modifier.padding(8.dp)) {
        Text("Custom custom")
        Text("1. measuring")
        Text("2. sizing")
        Text("3. placing")
    }
}

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    // custom layout attributes
    content : @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content // modifier와 content가 최소한의 parameter이고, 이 두 파라미터가 Layout에 전달된다.
    ) { measurables, constraints ->

        val placeables = measurables.map { measurable ->
            // 각 자식을 측정하기
            measurable.measure(constraints)
        }

        // 화면애 보여주기 전 column의 사이즈 측정하기
        var yPos = 0
        // 부모크기만큼 사이즈를 set
        layout(constraints.maxWidth, constraints.maxHeight) {
            // place children
            placeables.forEach { placeable ->
                // 화면에 아이템을 위치시키기
                placeable.placeRelative(x = 0, y = yPos)

                yPos += placeable.height
            }
        }


    }
}


@Preview
@Composable
fun TextWithPaddingToBaselinPreveiw() {
//    Text(text = "Hi there", Modifier.firstBaseLineToTop(32.dp))
}

@Preview
@Composable
fun TextWithNormalPaddingPrewview() {
//    Text(text = "Hi there", Modifier.padding(top = 32.dp))
}

@ExperimentalCoilApi
@Composable
fun ImageList() {
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState) {
        items(100) {
            ImageListItem(index = it)
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ImageListItem(index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = rememberImagePainter(
            data = "https://developer.android.com/images/brand/Android_Robot.png"
        ),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(Modifier.size(50.dp))
        Text(
            text = "Item #$index",
            style = MaterialTheme.typography.subtitle1
        )
    }
}


@ExperimentalCoilApi
@Preview(showBackground = true, name = "이미지로 리스트 보여주기")
@Composable
fun ImageListItemPreview() {
//    ImageListItem(0)
}


@ExperimentalCoilApi
@Composable
fun LazyList() {
    val listSize = 100
    val scrollState = rememberLazyListState() // scroll position을 save
    val coroutineScope = rememberCoroutineScope() // 애니메이션 스크롤이 실행되는 coroutine scope를 save

    Column {
        Row {
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }) {
                Text("최상위로 스크롤하기")
            }
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(listSize - 1)
                }
            }) {
                Text("최하단으로 스크롤하기")
            }
        }
        LazyColumn(state = scrollState) {
            items(listSize) {
                ImageListItem(index = it)
            }
        }
    }

}

@Preview(showBackground = true, name = "LazyColumn equals to RecyclerView")
@Composable
fun LazyListPreview() {
//    LazyList(null)
}

@Composable
fun SimpleList(context: Context?) {
    val scrollState = rememberScrollState()

    Column( // Column으로 리스트를 만들어버리면 한 번에 100개를 바로 만들어버린다. 마치 Android View에서의 ListView와 같다. 이러면 렌더링 이슈가 생긴다. => LazyColumn을 쓰자. 이는 Android View에서의 RecyclerView와 같다.
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        repeat(100) {
            Text(text = "Item $it")
        }
    }
}

@Preview(showBackground = true, name = "list 만들기")
@Composable
fun SimpleListPreview() {
//    SimpleList(null)
}



@Composable
fun Scaffolds(context: Context?) {
    // Scaffold는 기본적인 Material Design을 제공한다.
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "오늘의 날씨")
                },
                actions = {
                    Row{
                        IconButton(onClick = { Toast.makeText(context, "즐겨찾기를 클릭했어요.", Toast.LENGTH_SHORT).show() }) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = null,
                                modifier = Modifier.alpha(alpha = 0.4f)
                            )
                        }
                        IconButton(onClick = { Toast.makeText(context, "책을 클릭했어요.", Toast.LENGTH_SHORT).show() }) {
                            Icon(
                                imageVector = Icons.Filled.Book,
                                contentDescription = null,
                                modifier = Modifier.alpha(alpha = 0.4f)
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        ScaffoldBodyContent(Modifier.padding(innerPadding)) // 좀 더 reusable 하고 testable하게 만들기 위해서 따로 함수로 만든다.
    }
}

@Composable
fun ScaffoldBodyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Hi there!")
        Text(text = "오늘은 날씨가 춥네요? 그죠?")
    }
}

@Preview(showBackground = true, name = "scaffold")
@Composable
fun ScaffoldPreview() {
//    Scaffold(null)
}

@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
    /**
     * Modifier의 순서가 중요하다. 순서에 따라 view가 다르게 그려지고 반응한다.
     * */
    Row(
        modifier
            .padding(8.dp) // Row영역과 content 사이의 padding
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .clickable(onClick = { // 순서가 중요! padding을 먼저 선언하면, 전체영역에 clickable 이벤트가 나타나지 않는다!
                /*nothing now*/
            })
            .padding(16.dp) // content와 content를 감싸는 영역의 padding
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
        ) {
            // 이곳에 이미지
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = "Alfred Sisley.",
                fontWeight = FontWeight.Bold
            )
            // Composition tree를 통하여 암묵적으로 데이터를 전달받기 위해 CompositionLocalProvider를 사용
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Preview(showBackground = true, name = "코드랩 2주차 - 1")
@Composable
fun PhotographerCardPreview() {
//    PhotographerCard()
}
