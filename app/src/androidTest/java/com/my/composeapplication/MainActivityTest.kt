package com.my.composeapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by YourName on 2022/06/27.
 */
class MainActivityTest {
    @Rule
    @JvmField
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testDisplay() {
        val form = "Hello %s!"
        composeRule.onNodeWithText(String.format(form,"안드로이드")).assertIsDisplayed()
        composeRule.onNodeWithText(String.format(form,"Android")).assertIsDisplayed()
    }
}