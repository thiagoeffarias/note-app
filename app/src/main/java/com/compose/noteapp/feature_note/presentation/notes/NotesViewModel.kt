package com.compose.noteapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.noteapp.feature_note.domain.model.Note
import com.compose.noteapp.feature_note.domain.use_case.NoteUseCases
import com.compose.noteapp.feature_note.domain.util.NoteOrder
import com.compose.noteapp.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class NotesViewModel @Inject constructor(private val useCases: NoteUseCases) : ViewModel() {

    private val currentState = mutableStateOf(NotesState())
    internal val viewState: State<NotesState> = currentState
    private var lastDeletedNote: Note? = null
    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvents) {
        when (event) {
            is NotesEvents.Order -> {
                if (isSameOrder(event) && isSameOrderType(event)) return
                getNotes(event.noteOrder)
            }

            is NotesEvents.DeleteNote -> {
                viewModelScope.launch {
                    useCases.deleteNote(note = event.note)
                    lastDeletedNote = event.note
                }
            }

            NotesEvents.RestoreNote -> {
                viewModelScope.launch {
                    lastDeletedNote?.let { note -> useCases.insertNote(note = note) }
                }
            }

            NotesEvents.ToggleOrderSection -> {
                currentState.value = currentState.value.copy(
                    isOrderSectionVisible = !currentState.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = useCases.getNotes(noteOrder)
            .onEach { notes ->
                currentState.value = viewState.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }

    private fun isSameOrder(order: NotesEvents.Order): Boolean {
        return currentState.value.noteOrder::class == order.noteOrder::class
    }

    private fun isSameOrderType(order: NotesEvents.Order): Boolean {
        return this.currentState.value.noteOrder.orderType == order.noteOrder.orderType
    }
}