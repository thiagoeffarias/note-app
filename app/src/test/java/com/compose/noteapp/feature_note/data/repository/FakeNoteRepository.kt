package com.compose.noteapp.feature_note.data.repository

import com.compose.noteapp.feature_note.domain.model.Note
import com.compose.noteapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class FakeNoteRepository : NoteRepository {

    val notesToInsert = mutableListOf<Note>()
    init {
        ('a'..'z').forEachIndexed { index, letter ->
            val note = Note(
                title = letter.toString(),
                content = letter.toString(),
                timestamp = index.toLong(),
                color = index
            )
            notesToInsert.add(note)
        }
        notesToInsert.shuffle()
    }

    private val notes = mutableListOf<Note>()
    override fun getNotes(): Flow<List<Note>> = flow { emit(notes) }

    override suspend fun getNoteById(id: Int): Note? = notes.find { it.id == id }

    override suspend fun insertNote(note: Note) {
        notes.add(note)
    }

    override suspend fun deleteNote(note: Note) {
        notes.remove(note)
    }

    fun populateTables() {
        runBlocking {
            notesToInsert.forEach {
                insertNote(it)
            }
        }
    }
}