package com.dontsu.layouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.dontsu.layouts.ui.theme.LayoutsTheme

class ConstraintActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutsTheme {
                DecoupledConstraintLayout()
            }
        }
    }

}

@Composable
fun DecoupledConstraintLayout() {
    BoxWithConstraints {
        val constraints = if (maxWidth < maxHeight) {
            decoupledConstraints(margin = 16.dp) // Portrait constraints
        } else {
            decoupledConstraints(margin = 32.dp) // Landscape constraints
        }

        ConstraintLayout(constraints) {
            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.layoutId("button")
            ) {
                Text("Button")
            }

            Text("Text", Modifier.layoutId("text"))
        }
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin= margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin)
        }
    }
}

@Composable
private fun LargeConstraintLayoutContent() {
    ConstraintLayout {
        val text = createRef() // reference를 하나만 만들 땐 crateRef() 를 사용

        val guideline = createGuidelineFromStart(fraction = 0.5f) // fraction : 부분, 일부
        Text(
            text = "This is suuuuuuuuuuuuuuuuuper long long long text",
            Modifier.constrainAs(text) {
                linkTo(start = guideline, end = parent.end)
                width = Dimension.preferredWrapContent
            }
        )
    }
}


@Preview(showBackground = true, name = "코드랩 Constraint Layout")
@Composable
private fun LargeConstraintLayoutPreview() {
//    ConstraintLayoutContent()
}

@Composable
private fun ConstraintLayoutContent() {
    ConstraintLayout {
        // 1. reference 만들기
        val (button1, button2, text) = createRefs()

        Button(
            onClick = { /*TODO*/ },
            // Button composable에 위에서 만든 button을 할당하기
            modifier = Modifier.constrainAs(button1) {
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text("Button 1")
        }

        // Text composable에 위에서 만든 text를 할당하기
        Text(text = "Text", Modifier.constrainAs(text) {
            top.linkTo(button1.bottom, margin = 16.dp)
            centerAround(button1.end)
        })
        
        val barrier = createEndBarrier(button1, text)
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.constrainAs(button2) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(barrier)
            }
        ) {
            Text(text = "Button 2")
        }

    }
}

@Preview(showBackground = true, name = "코드랩 Constraint Layout")
@Composable
private fun ConstraintLayoutPreview() {
//    ConstraintLayoutContent()
}