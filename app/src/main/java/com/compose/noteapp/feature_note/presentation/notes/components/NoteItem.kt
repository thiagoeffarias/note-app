package com.compose.noteapp.feature_note.presentation.notes.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.compose.noteapp.feature_note.domain.model.Note
import com.compose.noteapp.ui.theme.NoteAppTheme
import java.sql.Timestamp

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    onDeleteClicked: () -> Unit
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val clipPath = Path().apply {
                lineTo(x = size.width - cutCornerSize.toPx(), y = 0f)
                lineTo(x = size.width, y = cutCornerSize.toPx())
                lineTo(x = size.width, y = size.height)
                lineTo(x = 0f, size.height)
                close()
            }

            clipPath(clipPath) {
                drawRoundRect(color = Color(note.color), size = size, cornerRadius = CornerRadius(cornerRadius.toPx()))
            }
            clipPath(clipPath) {
                drawRoundRect(
                    color = Color(ColorUtils.blendARGB(note.color, Color.Black.toArgb(), 0.2f)),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100F),
                    size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(end = 32.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(onClick = onDeleteClicked, modifier = Modifier.align(Alignment.BottomEnd)) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete note")
        }
    }
}

@Preview
@Composable
fun NoteItemPreview() {
    NoteAppTheme {
        NoteItem(
            note = Note(
                title = "A new note",
                content = "content of the note goes here.",
                timestamp = System.currentTimeMillis(),
                color = MaterialTheme.colorScheme.errorContainer.toArgb() //RedOrange.toArgb()
            ),
            onDeleteClicked = { }
        )
    }
}
