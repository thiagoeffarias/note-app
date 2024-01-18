package com.compose.noteapp.feature_note.presentation.notes

import com.compose.noteapp.feature_note.domain.model.Note
import com.compose.noteapp.feature_note.domain.util.NoteOrder

sealed class NotesEvents {
    data class Order(val noteOrder: NoteOrder) : NotesEvents()
    data class DeleteNote(val note: Note) : NotesEvents()
    data object RestoreNote : NotesEvents()
    data object ToggleOrderSection : NotesEvents()
}
