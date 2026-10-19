package com.frizovdanya.vektor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                fontSize = 11.sp,
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
        shape = RoundedCornerShape(27.dp),
        color = palette.display
    ) {
        Column(
            modifier = Modifier.padding(start = 20.dp, top = 16.dp, end = 12.dp, bottom = 17.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "ВЫРАЖЕНИЕ",
                    color = Color(0xFFB7CDC3),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.3.sp,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    modifier = Modifier
                        .clickable(onClick = onToggleAngle)
                        .padding(end = 2.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.11f)
                ) {
                    Text(
                        if (degrees) "DEG" else "RAD",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(38.dp)) {
                    Icon(Icons.Outlined.Backspace, contentDescription = "Удалить символ", tint = Color(0xFFB7CDC3), modifier = Modifier.size(19.dp))
                }
            }
            Text(
                text = expression.ifBlank { "Введите выражение" },
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                color = if (expression.isBlank()) Color(0xFF88A198) else Color.White,
                fontSize = 23.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                textAlign = TextAlign.End,
                overflow = TextOverflow.Clip
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("РЕЗУЛЬТАТ", color = Color(0xFFB7CDC3), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.3.sp)
                    Text(
                        preview,
                        color = Color(0xFF8CE6C6),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text("=", color = Color.White.copy(alpha = 0.35f), fontSize = 26.sp, fontWeight = FontWeight.Light, modifier = Modifier.padding(bottom = 5.dp, end = 8.dp))
            }
            if (!errorMessage.isNullOrBlank()) {
                Text(errorMessage, color = Color(0xFFFFB5A9), fontSize = 11.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun FunctionChip(key: String, palette: AppPalette, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .height(36.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(13.dp),
        color = palette.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, palette.outline.copy(alpha = 0.7f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                key,
                modifier = Modifier.padding(horizontal = 13.dp),
                color = palette.text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
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
    val background = when {
        isEquals -> palette.accent
        isClear -> palette.accentTint
        isOperator -> palette.surfaceAlt
        else -> palette.surface
    }
    val foreground = when {
        isEquals -> if (palette.isDark) Color(0xFF10221C) else Color.White
        isClear -> palette.accent
        isOperator -> palette.accent
        else -> palette.text
    }
    Surface(
        modifier = modifier
            .height(55.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(19.dp),
        color = background,
        border = if (isEquals || isClear || isOperator) null else androidx.compose.foundation.BorderStroke(1.dp, palette.outline.copy(alpha = 0.62f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                key,
                color = foreground,
                fontSize = if (key == "AC") 15.sp else 22.sp,
                fontWeight = if (isEquals || isOperator || isClear) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}
