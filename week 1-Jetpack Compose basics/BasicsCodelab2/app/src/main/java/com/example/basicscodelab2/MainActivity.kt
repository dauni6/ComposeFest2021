package com.example.basicscodelab2

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basicscodelab2.ui.theme.BasicsCodelab2Theme
import kotlin.math.exp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { // xml 대신 레이아웃을 구성할 Composable 함수가 들어가게 됨.
            BasicsCodelab2Theme {
                // A surface container using the 'background' color from the theme
                MyApp() // MyApp() Composable function을 만들어 놓으면 좀 더 reusable하게 사용할 수 있다.
            }
        }
    }
}

@Composable
private fun MyApp(name: List<String> = listOf("World", "Compose")) {
//    Surface(color = MaterialTheme.colors.background) {
//        Greeting(name = "Android")
//    }
//    Column(modifier = Modifier.padding(vertical = 4.dp)) {
//        name.forEach { _name -> // 이런식으로 코틀린 문법으로 사용가능
//            Greeting(name = _name)
//        }
//    }
    var shouldShowOnBoarding by rememberSaveable { mutableStateOf(true) }
    if (shouldShowOnBoarding) {
        OnBoardingScreen(onContinueClicked = { shouldShowOnBoarding = false }) // 콜백을 이용하자
    } else {
        Greetings()
    }
}

@Composable
private fun Greetings(names: List<String> = List(1000) { "$it" }) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Composable
private fun Greeting(name: String) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(name)
    }
}

@Composable
private fun CardContent(name: String) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(text = "Hello,")
            Text(
                text = name,
                style = MaterialTheme.typography.h4.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (expanded) {
                Text(
                    text = ("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(4)
                )
            }
        }
        IconButton(onClick = { expanded = expanded.not() }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }
            )
        }
    }

}

/*@Composable // @Composable을 사용하면 Composable function을 만들 수 있게 된다.
private fun Greeting(name: String) {
    var expanded by remember { mutableStateOf(false) } // rememberSaveable를 사용하면 아래로 스크롤을 한 뒤에도 show 상태가 유지 remember를 사용하면 스크롤 후 다시 돌아가면 닫혀잇다.
    val extraPadding by animateDpAsState(
        if (expanded) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        )

    )
    Surface( // Surface를 통하여 Background 지정
         color = MaterialTheme.colors.primary,
         modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(bottom = extraPadding.coerceAtLeast(0.dp))
            ) {
                Text(text = "Hello,")
                Text(text = name,
                    color = Color.LightGray,
                    style = MaterialTheme.typography.h4.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
            OutlinedButton(
                onClick = {
                    expanded = expanded.not()
                }
            ) {
                Text(if (expanded) "Show less" else "Show more")
            }
        }
//        Text(
//            text = "Hello $name!",
//            color = Color.LightGray, // 테스트 겸 추가
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .padding(24.dp) // Modifier를 통해 padding을 지정, Margin 개념을 padding으로 적용할 수 있는 듯하다. Surface와의 padding
//                .border(1.dp, Color.White) // border 그리기
//                .padding(8.dp) // border와의 padding
//                .border(1.dp, Color.Yellow) // 두 번째 border 그리기
//                .padding(8.dp) // 두 번째 border와 text 사이의 padding
//        )
    }
}*/

@Composable
fun OnBoardingScreen(onContinueClicked: () -> Unit) {

    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome to the Basics CodeLab~!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text(text = "Continue")
            }
        }
    }

}

// @Preview를 통해 다양한 프리뷰를 볼 수 있게 된다. 프리뷰는 여러개 사용가능하다.

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
private fun OnBoardingPreview() {
    BasicsCodelab2Theme {
//        MyApp() // 프리뷰에서 겹쳐지지만 먼저 호출되므로 뒤로 가려진다
        OnBoardingScreen(onContinueClicked = {}) // 아무것도 하지 않음
        Greetings()
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
private fun DefaultPreview() {
    BasicsCodelab2Theme {
        Greetings()
    }
}