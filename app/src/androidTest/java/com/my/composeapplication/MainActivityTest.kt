package com.my.composeapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.my.composeapplication.scene.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by YourName on 2022/06/27.
 */
@RunWith(AndroidJUnit4::class)
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
        composeRule.onNodeWithTag("main").assertIsDisplayed()
    }
}