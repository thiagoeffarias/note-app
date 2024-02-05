package com.compose.noteapp.feature_note.domain.use_case

import com.compose.noteapp.feature_note.data.repository.FakeNoteRepository
import com.compose.noteapp.feature_note.domain.model.Note
import com.compose.noteapp.feature_note.domain.util.NoteOrder
import com.compose.noteapp.feature_note.domain.util.OrderType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetNotesTest {

    private lateinit var getNotes: GetNotes
    private lateinit var fakeRepository: FakeNoteRepository

    @Before
    fun setup() {
        fakeRepository = FakeNoteRepository().apply { populateTables() }
        getNotes = GetNotes(fakeRepository)
    }

    @Test
    fun `when order notes by title ascending should return correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Title(OrderType.Ascending)).first()

        for(note in 0 .. notes.size -2 ) {
          assertThat(notes[note].title).isLessThan(notes[note + 1].title)
        }
    }

    @Test
    fun `when order notes by title descending should return correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Title(OrderType.Descending)).first()

        for(note in 0 .. notes.size -2 ) {
          assertThat(notes[note].title).isGreaterThan(notes[note + 1].title)
        }
    }
    @Test
    fun `when order notes by date ascending should return correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Date(OrderType.Ascending)).first()

        for(note in 0 .. notes.size -2 ) {
          assertThat(notes[note].timestamp).isLessThan(notes[note + 1].timestamp)
        }
    }
    @Test
    fun `when order notes by date descending should return correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Date(OrderType.Descending)).first()

        for(note in 0 .. notes.size -2 ) {
          assertThat(notes[note].timestamp).isGreaterThan(notes[note + 1].timestamp)
        }
    }
    @Test
    fun `when order notes by color ascending should return correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Color(OrderType.Ascending)).first()

        for(note in 0 .. notes.size -2 ) {
          assertThat(notes[note].color).isLessThan(notes[note + 1].color)
        }
    }
    @Test
    fun `when order notes by color descending should return correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Color(OrderType.Descending)).first()

        for(note in 0 .. notes.size -2 ) {
          assertThat(notes[note].color).isGreaterThan(notes[note + 1].color)
        }
    }
}
