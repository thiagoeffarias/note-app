package com.compose.noteapp.feature_note.presentation.add_edit_note

import androidx.compose.runtime.IntState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.noteapp.feature_note.domain.model.InvalidNoteException
import com.compose.noteapp.feature_note.domain.model.Note
import com.compose.noteapp.feature_note.domain.use_case.NoteUseCases
import com.compose.noteapp.feature_note.presentation.util.Screen.Companion.NOTE_ID_PARAMETER_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    init {
        savedStateHandle.get<Int>(NOTE_ID_PARAMETER_KEY)?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.let { currentNote ->
                        currentNoteId = noteId
                        noteTitleState.value = noteTitleState.value.copy(
                            text = currentNote.title,
                            isHintVisible = false
                        )
                        noteContentState.value = noteContentState.value.copy(
                            text = currentNote.content,
                            isHintVisible = false
                        )
                        noteColorState.value = currentNote.color
                    }
                }
            }
        }
    }

    private val noteTitleState = mutableStateOf(NoteTextFieldState(hint = "Enter title..."))
    val noteTitle: State<NoteTextFieldState> = noteTitleState

    private val noteContentState = mutableStateOf(NoteTextFieldState(hint = "Enter some content..."))
    val noteContent: State<NoteTextFieldState> = noteContentState

    private val noteColorState = mutableIntStateOf(Note.noteColors.random().toArgb())
    val noteColor: IntState = noteColorState

    private val sharedEventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = sharedEventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.ChangeColor -> {
                noteColorState.value = event.color
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                noteContentState.value = noteContentState.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContentState.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                noteTitleState.value = noteTitleState.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitleState.value.text.isBlank()
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                noteContentState.value = noteContentState.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.EnteredTitle -> {
                noteTitleState.value = noteTitleState.value.copy(
                    text = event.value
                )
            }

            AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.insertNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                        sharedEventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        UiEvent.ShowSnackBar(e.message ?: "Couldn't save the note.")
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}