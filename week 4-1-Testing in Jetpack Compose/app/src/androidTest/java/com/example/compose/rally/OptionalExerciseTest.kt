package com.example.compose.rally

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class OptionalExerciseTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun rallyTopAppBarTest_checkTabSelected() {
        var currentScreen = RallyScreen.Overview
        composeTestRule.setContent {
            RallyApp(currentScreen) { screen ->
                currentScreen = screen
            }
        }
        RallyScreen.values().toList().forEach { screen ->
            composeTestRule
                .onNodeWithContentDescription(screen.name)
//                .assertIsSelected()
                .performClick()
            assert(currentScreen == screen) // 문제에서 상태 확인을 위한 주장
        }

    }

}