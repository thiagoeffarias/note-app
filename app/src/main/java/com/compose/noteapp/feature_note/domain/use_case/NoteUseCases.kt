package com.compose.noteapp.feature_note.domain.use_case

data class NoteUseCases(
    val getNote: GetNote,
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val insertNote: InsertNote,
)
