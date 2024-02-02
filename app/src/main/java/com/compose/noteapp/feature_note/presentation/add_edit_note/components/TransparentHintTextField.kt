package com.compose.noteapp.feature_note.presentation.add_edit_note.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.noteapp.ui.theme.NoteAppTheme

@Composable
fun TransparentHintTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onFocusChang: (FocusState) -> Unit
) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .wrapContentHeight()
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            textStyle = textStyle,
            modifier = Modifier
                .fillMaxWidth()

                .onFocusChanged() {
                    onFocusChang(it)
                },
            maxLines = if (singleLine) 1 else 10
        )
        if (isHintVisible) {
            Text(text = hint, style = textStyle, color = Color.DarkGray)
        }
    }
}

@Composable
@Preview
fun TransparentHintTextFieldPreview() {
    NoteAppTheme {
        TransparentHintTextField(
            text = "Main Text",
            hint = "Hint",
            isHintVisible = false,
            onValueChange = {},
            onFocusChang = {}
        )
    }
}