package com.compose.noteapp.feature_note.presentation.notes

import com.compose.noteapp.feature_note.data.repository.FakeNoteRepository
import com.compose.noteapp.feature_note.domain.model.Note
import com.compose.noteapp.feature_note.domain.repository.NoteRepository
import com.compose.noteapp.feature_note.domain.use_case.DeleteNote
import com.compose.noteapp.feature_note.domain.use_case.GetNote
import com.compose.noteapp.feature_note.domain.use_case.GetNotes
import com.compose.noteapp.feature_note.domain.use_case.InsertNote
import com.compose.noteapp.feature_note.domain.use_case.NoteUseCases
import com.compose.noteapp.feature_note.domain.util.NoteOrder
import com.compose.noteapp.feature_note.domain.util.OrderType
import com.util.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@RunWith(Parameterized::class)
class NotesViewModelTest(private val noteOrder: NoteOrder) {

    @get:Rule var mainCoroutineRule = MainDispatcherRule()
    private lateinit var fakeNoteRepository: FakeNoteRepository
    private lateinit var getNote: GetNote
    private lateinit var getNotes: GetNotes
    private lateinit var deleteNote: DeleteNote
    private lateinit var insertNote: InsertNote
    private lateinit var useCases: NoteUseCases

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = listOf(
            arrayOf(NoteOrder.Title(OrderType.Ascending)),
            arrayOf(NoteOrder.Title(OrderType.Descending)),
            arrayOf(NoteOrder.Date(OrderType.Ascending)),
            arrayOf(NoteOrder.Date(OrderType.Descending)),
            arrayOf(NoteOrder.Color(OrderType.Ascending)),
            arrayOf(NoteOrder.Color(OrderType.Descending))
        )
    }

    @Before
    fun setup() {
        fakeNoteRepository = FakeNoteRepository()
        fakeNoteRepository.populateTables()

        getNote = GetNote(fakeNoteRepository)
        getNotes = GetNotes(fakeNoteRepository)
        deleteNote = DeleteNote(fakeNoteRepository)
        insertNote = InsertNote(fakeNoteRepository)

        useCases = NoteUseCases(
            getNote = getNote,
            getNotes = getNotes,
            deleteNote = deleteNote,
            insertNote = insertNote
        )
    }

    @Test
    fun `when order is triggered, getNotes should be called`() = runTest {
        val event: NotesEvents.Order = NotesEvents.Order(noteOrder)
        val presenter = NotesViewModel(useCases)

        presenter.onEvent(event)
        advanceUntilIdle()

        println("Note Order: ${presenter.viewState.value.noteOrder::class.java.name}")
        println("Expected Note Order: ${event.noteOrder::class.java.name}")

        println("Order Type: ${presenter.viewState.value.noteOrder.orderType}")
        println("Expected Order Type: ${event.noteOrder.orderType}")

        assertEquals(presenter.viewState.value.noteOrder::class, event.noteOrder::class)
        assert(presenter.viewState.value.noteOrder.orderType == event.noteOrder.orderType)
    }
}
