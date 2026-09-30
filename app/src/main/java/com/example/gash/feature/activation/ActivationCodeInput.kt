package com.example.gash.feature.activation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import com.example.gash.ui.theme.GashBorder
import com.example.gash.ui.theme.GashGreen
import com.example.gash.ui.theme.GashError_RED
import com.example.gash.ui.theme.GashOrange

@Composable
fun ActivationCodeInput(
    code: String,
    isError: Boolean,
    onCodeChange: (String) -> Unit,
    onFocus: () -> Unit
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    BasicTextField(
        value = code,
        onValueChange = { value ->
            onCodeChange(
                value
                    .filter { it.isDigit() }
                    .take(ActivationViewModel.CODE_LENGTH)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                isFocused = it.isFocused

                if (it.isFocused) {
                    onFocus()
                }
            },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        textStyle = TextStyle(
            color = Color.Transparent,
            fontSize = 1.sp
        ),
        decorationBox = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(ActivationViewModel.CODE_LENGTH) { index ->

                    val digit = code.getOrNull(index)?.toString() ?: "−"

                    CodeBox(
                        digit = digit,
                        isError = isError,
                        isActive = isFocused && index == code.length,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    )
}
@Composable
private fun CodeBox(
    digit: String,
    isError: Boolean,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val borderColor = when {
        isError -> GashError_RED
        isActive -> GashOrange
        else -> GashBorder
    }

    val textColor = when {
        isError -> GashError_RED
        else -> GashGreen
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .border(
                width = if (isActive || isError) 2.dp else 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(24.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = digit,
            color = textColor,
            fontSize = 24.sp
        )
    }
}