package com.compose.noteapp.feature_note.presentation.util

sealed class Screen(val route: String) {
    object NoteScreen : Screen("note_screen")
    object AddEditNoteScreen : Screen("add_edit_note_screen")

    companion object {

        const val NOTE_ID_PARAMETER_KEY = "noteId"
        const val NOTE_COLOR_PARAMETER_KEY = "noteColor"
        const val ADD_EDIT_ROUTE_PARAMETERS =
            "?$NOTE_ID_PARAMETER_KEY={$NOTE_ID_PARAMETER_KEY}&$NOTE_COLOR_PARAMETER_KEY={$NOTE_COLOR_PARAMETER_KEY}"
    }
}