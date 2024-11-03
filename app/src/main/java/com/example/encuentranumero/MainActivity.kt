package com.example.encuentranumero
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Composable
fun NumericKeypad(
    onNumberClick: (String) -> Unit,
    onDelete: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    buttonColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    borderColor: Color = MaterialTheme.colorScheme.outline
) {
    val numbers = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "⌫")
    )

    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        numbers.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { number ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        if (number.isNotEmpty()) {
                            Surface(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        1.dp,
                                        borderColor,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable(enabled = enabled) {
                                        if (number == "⌫") {
                                            onDelete()
                                        } else {
                                            onNumberClick(number)
                                        }
                                    },
                                color = buttonColor
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = number,
                                        fontSize = 24.sp,
                                        color = if (enabled) textColor else textColor.copy(alpha = 0.38f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    heightDp = 400,
    name = "Numeric Keypad Preview"
)
@Composable
fun NumericKeypadPreview() {
    MaterialTheme {
        Surface {
            NumericKeypad(
                onNumberClick = { },
                onDelete = { },
                enabled = true,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

// Preview con el teclado deshabilitado
@Preview(
    showBackground = true,
    widthDp = 320,
    heightDp = 400,
    name = "Numeric Keypad Disabled Preview"
)
@Composable
fun NumericKeypadDisabledPreview() {
    MaterialTheme {
        Surface {
            NumericKeypad(
                onNumberClick = { },
                onDelete = { },
                enabled = false,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

// Preview con tema oscuro
@Preview(
    showBackground = true,
    widthDp = 320,
    heightDp = 400,
    name = "Numeric Keypad Dark Theme Preview",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun NumericKeypadDarkPreview() {
    MaterialTheme {
        Surface {
            NumericKeypad(
                onNumberClick = { },
                onDelete = { },
                enabled = true,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}