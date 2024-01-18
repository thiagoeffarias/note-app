package com.compose.noteapp.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.compose.noteapp.ui.theme.BabyBlue
import com.compose.noteapp.ui.theme.LightGreen
import com.compose.noteapp.ui.theme.RedOrange
import com.compose.noteapp.ui.theme.RedPink
import com.compose.noteapp.ui.theme.Violet
import java.sql.Timestamp

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Timestamp,
    val color: Int,
    @PrimaryKey val id: Int? = null
) {

    companion object {

        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(message: String) : Exception(message)
