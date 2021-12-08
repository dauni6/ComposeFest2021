package com.example.compose.rally

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.rally.ui.components.RallyTopAppBar
import org.junit.Rule
import org.junit.Test
import java.util.*

class TopAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Espresso를 사용하면 Android View에서 수행하는 것과 유사하게 갭의 메인 액티비티를
    // 시작할 수 있다. 아래의 코드로 가능
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule(RallyActivity::class.java)
    //그러나 컴포즈를 사용하면 컴포넌트 테스트를 더 단순하게 사용할 수 있다. 아래의 코드로 가능
//    @Test
//    fun myTest() {
//        composeTestRule.setContent {
//            Text("You can set any Compose content!")
//        }
//    }

    @Test
    fun rallyTopAppBarTest() {
        val allScreen = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreen,
                onTabSelected = { /*TODO*/ },
                currentScreen = RallyScreen.Accounts
            )
        }
        Thread.sleep(5000)
    }

    @Test
    fun rallyTopAppBarTest_currentTabSelected() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }
        // UI요소 찾기, 속성 확인 및 수행 패턴 => composeTestRule{.finder}{.assertion}{.action}
        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertIsSelected()
    }

    @Test
    fun rallyTopAppBarTest_currentLabelExists() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }

        // printToLog를 사용하여 시맨특 트리를 출력하기
        // 잠깐, 시맨틱트리(Semantics tree)란? 컴포즈 테스트에서 시맨틱트리를 통하여 화면에서 요소를 찾고 해당 속성을 읽는다
//        composeTestRule.onRoot().printToLog("currentLabelExists") // 실행을 하면 currentLabelExists를 찾을 수 있다. 또한 속성을 찾을 수 있다 했는데, contentDescription도 확인할 수 있다.

//        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists") // 시맨틱트리를 병합하지 않기

//        composeTestRule
//            .onNodeWithText(RallyScreen.Accounts.name.uppercase(Locale.getDefault()))
//            .assertExists()
        composeTestRule
            .onNode(
                hasText(RallyScreen.Accounts.name.uppercase(Locale.getDefault())) and
                        hasParent(
                            hasContentDescription(RallyScreen.Accounts.name)
                        ),
                useUnmergedTree = true
            )
            .assertExists()
    }




}
