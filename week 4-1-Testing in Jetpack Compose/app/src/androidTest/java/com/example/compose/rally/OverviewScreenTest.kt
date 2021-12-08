package com.example.compose.rally

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.compose.rally.ui.overview.OverviewBody
import org.junit.Rule
import org.junit.Test

/**
 * 테스트는 테스트할 대상고 적절한 동기화가 필요하다.
 * 동기화가 되지 않은 상태에서 테스트하면 화면이 보이기전에 요소를 찾으려 하거나
 * 불필요하게 기다리게 된다.
 * */
class OverviewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun overviewScreen_alertsDisplayed() {
        composeTestRule.setContent {
            OverviewBody()
        }

        composeTestRule
            .onNodeWithText("Alerts") // 무한으로 깜빡이는 애니메이션(Repeated animation) 때문에 발생한 Idling resource timed out: possibly due to compose being busy. 를 확인할 수 있다.
            .assertIsDisplayed()
    }

}
