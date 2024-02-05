package com.compose.noteapp.feature_note.presentation.notes

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.compose.noteapp.feature_note.domain.model.Note
import com.compose.noteapp.feature_note.presentation.notes.NotesScreenTestTags.ORDER_SECTION
import com.compose.noteapp.feature_note.presentation.notes.NotesScreenTestTags.TOGGLE_BUTTON
import com.compose.noteapp.feature_note.presentation.notes.components.NoteItem
import com.compose.noteapp.feature_note.presentation.notes.components.OrderSection
import com.compose.noteapp.feature_note.presentation.util.Screen
import com.compose.noteapp.feature_note.presentation.util.Screen.Companion.NOTE_COLOR_PARAMETER_KEY
import com.compose.noteapp.feature_note.presentation.util.Screen.Companion.NOTE_ID_PARAMETER_KEY
import com.compose.noteapp.ui.theme.NoteAppTheme
import kotlinx.coroutines.launch
import java.sql.Timestamp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel(),
) {
    val state = viewModel.viewState.value
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditNoteScreen.route)
                },
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        },
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Your notes",
                    style = MaterialTheme.typography.headlineMedium
                )
                IconButton(
                    modifier = Modifier.testTag(TOGGLE_BUTTON),
                    onClick = { viewModel.onEvent(NotesEvents.ToggleOrderSection) },
                ) {
                    Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort notes")
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .testTag(ORDER_SECTION),
                    noteOrder = state.noteOrder,
                    onOrderChange = { nextOrder ->
                        viewModel.onEvent(NotesEvents.Order(nextOrder))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.notes) { note ->
                    NoteItem(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditNoteScreen.route +
                                        "?$NOTE_ID_PARAMETER_KEY=${note.id}&$NOTE_COLOR_PARAMETER_KEY=${note.color}"
                                )
                            },
                        onDeleteClicked = {
                            viewModel.onEvent(NotesEvents.DeleteNote(note))
                            scope.launch {
                                val result = snackBarHostState.showSnackbar(
                                    message = "Note deleted",
                                    actionLabel = "Undo",
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(NotesEvents.RestoreNote)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

object NotesScreenTestTags {
    const val ORDER_SECTION = "ORDER_SECTION"
    const val TOGGLE_BUTTON = "TOGGLE_BUTTON"
}

@Preview
@Composable
fun NoteItemPreview() {
    val notes = (1..3).map {
        Note(
            id = it,
            title = "Title $it",
            content = "Content $it",
            timestamp = System.currentTimeMillis(),
            color = Note.noteColors.random().toArgb()
        )
    }

    val viewModel = hiltViewModel<NotesViewModel>().apply {
      this.viewState.value.copy(notes = notes)
    }
    val navController: NavController = rememberNavController()

    NoteAppTheme {
        NotesScreen(navController, viewModel)
    }
}
