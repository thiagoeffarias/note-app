package com.compose.noteapp.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.compose.noteapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.compose.noteapp.feature_note.presentation.notes.NotesScreen
import com.compose.noteapp.feature_note.presentation.util.Screen
import com.compose.noteapp.feature_note.presentation.util.Screen.Companion.ADD_EDIT_ROUTE_PARAMETERS
import com.compose.noteapp.feature_note.presentation.util.Screen.Companion.NOTE_COLOR_PARAMETER_KEY
import com.compose.noteapp.feature_note.presentation.util.Screen.Companion.NOTE_ID_PARAMETER_KEY
import com.compose.noteapp.ui.theme.NoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = Screen.NoteScreen.route) {
                        composable(route = Screen.NoteScreen.route) { NotesScreen(navController) }
                        composable(
                            route = Screen.AddEditNoteScreen.route + ADD_EDIT_ROUTE_PARAMETERS,
                            arguments = listOf(
                                navArgument(
                                    name = NOTE_ID_PARAMETER_KEY
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = NOTE_COLOR_PARAMETER_KEY
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                            )
                        ) {
                            val noteColor = it.arguments?.getInt(NOTE_COLOR_PARAMETER_KEY) ?: -1
                            AddEditNoteScreen(
                                navController = navController,
                                noteColor = noteColor
                            )
                        }
                    }
                }
            }
        }
    }
}