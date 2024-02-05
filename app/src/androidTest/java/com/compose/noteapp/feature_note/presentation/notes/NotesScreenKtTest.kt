package com.compose.noteapp.feature_note.presentation.notes

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.compose.noteapp.di.TestAppModule
import com.compose.noteapp.feature_note.presentation.MainActivity
import com.compose.noteapp.feature_note.presentation.notes.NotesScreenTestTags.ORDER_SECTION
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(TestAppModule::class)
class NotesScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun clickToggleOrderSection_isVisible() {
        with(composeRule) {
            onNodeWithTag(ORDER_SECTION).assertDoesNotExist()
            onNodeWithContentDescription("Sort notes").performClick()
            onNodeWithTag(ORDER_SECTION).assertIsDisplayed()
        }
    }

    @Test
    fun clickOnAddNoteButton() {
        with(composeRule) {
            onNodeWithContentDescription("Add note").performClick()
            onNodeWithContentDescription("Save note").assertIsDisplayed()
            onNodeWithContentDescription("Add note").assertDoesNotExist()
            onBackPressed()
            onNodeWithContentDescription("Add note").assertIsDisplayed()
        }
    }

    private fun onBackPressed() {
        composeRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }
    }
}