package com.frizovdanya.vektor.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorScreen(
    expression: String,
    preview: String,
    errorMessage: String?,
    degrees: Boolean,
    lastAnswer: String,
    palette: AppPalette,
    onKey: (String) -> Unit,
    onEvaluate: () -> Unit,
    onToggleAngle: () -> Unit,
    onOpenHistory: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BrandHeader(palette) {
            IconButton(onClick = onOpenHistory) {
                Icon(Icons.Outlined.History, contentDescription = "Открыть историю", tint = palette.muted)
            }
        }
        DisplayPanel(
            expression = expression,
            preview = preview,
            errorMessage = errorMessage,
            degrees = degrees,
            palette = palette,
            onToggleAngle = onToggleAngle,
            onDelete = { onKey("DEL") }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("sin", "cos", "tan", "√", "x²", "xʸ", "ln", "log", "π", "e", "!", "%", "ANS").forEach { key ->
                FunctionChip(key, palette) { onKey(key) }
            }
        }
        val rows = listOf(
            listOf("AC", "(", ")", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "−"),
            listOf("1", "2", "3", "+"),
            listOf("±", "0", ".", "=")
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { key ->
                        CalculatorKey(
                            key = key,
                            palette = palette,
                            modifier = Modifier.weight(1f),
                            onClick = { if (key == "=") onEvaluate() else onKey(key) }
                        )
                    }
                }
            }
        }
        if (lastAnswer.isNotBlank()) {
            Text(
                "ANS  ${lastAnswer.replace("-", "−")}",
                modifier = Modifier.fillMaxWidth(),
                color = palette.muted,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun DisplayPanel(
    expression: String,
    preview: String,
    errorMessage: String?,
    degrees: Boolean,
    palette: AppPalette,
    onToggleAngle: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = palette.display
    ) {
        Column(
            modifier = Modifier.padding(start = 20.dp, top = 16.dp, end = 12.dp, bottom = 17.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Выражение",
                    color = palette.muted,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f)
                )
                AssistChip(
                    onClick = onToggleAngle,
                    label = { Text(if (degrees) "DEG" else "RAD") },
                    shape = RoundedCornerShape(12.dp)
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Outlined.Backspace, contentDescription = "Удалить символ", tint = palette.muted, modifier = Modifier.size(19.dp))
                }
            }
            Text(
                text = expression.ifBlank { "Введите выражение" },
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                color = if (expression.isBlank()) palette.muted else palette.text,
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                textAlign = TextAlign.End,
                overflow = TextOverflow.Clip
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Результат", color = palette.muted, style = MaterialTheme.typography.labelMedium)
                    Text(
                        preview,
                        color = palette.accent,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text("=", color = palette.muted.copy(alpha = 0.6f), fontSize = 26.sp, fontWeight = FontWeight.Light, modifier = Modifier.padding(bottom = 5.dp, end = 8.dp))
            }
            if (!errorMessage.isNullOrBlank()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun FunctionChip(key: String, palette: AppPalette, onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = { Text(key) },
        shape = RoundedCornerShape(14.dp)
    )
}

@Composable
private fun CalculatorKey(
    key: String,
    palette: AppPalette,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val isEquals = key == "="
    val isClear = key == "AC"
    val isOperator = key in setOf("÷", "×", "−", "+")
    val tonalColors = when {
        isClear -> ButtonDefaults.filledTonalButtonColors(
            containerColor = palette.accentTint,
            contentColor = palette.accent
        )
        isOperator -> ButtonDefaults.filledTonalButtonColors(
            containerColor = palette.surfaceAlt,
            contentColor = palette.accent
        )
        else -> ButtonDefaults.filledTonalButtonColors(
            containerColor = palette.surfaceAlt.copy(alpha = 0.58f),
            contentColor = palette.text
        )
    }
    if (isEquals) {
        Button(
            onClick = onClick,
            modifier = modifier.height(55.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = palette.accent, contentColor = palette.onAccent),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(key, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        }
    } else {
        FilledTonalButton(
            onClick = onClick,
            modifier = modifier.height(55.dp),
            shape = RoundedCornerShape(18.dp),
            colors = tonalColors,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(key, fontSize = if (key == "AC") 15.sp else 22.sp, fontWeight = if (isClear || isOperator) FontWeight.SemiBold else FontWeight.Medium)
        }
    }
}
