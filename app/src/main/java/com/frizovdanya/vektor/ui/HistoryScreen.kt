package com.frizovdanya.vektor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun HistoryScreen(
    entries: List<HistoryEntry>,
    palette: AppPalette,
    onSelect: (HistoryEntry) -> Unit,
    onClear: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(17.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                ScreenHeader(
                    eyebrow = "ВСЁ СОХРАНЕНО",
                    title = "История",
                    subtitle = "Последние вычисления всегда под рукой.",
                    palette = palette
                )
            }
            if (entries.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Outlined.DeleteOutline, contentDescription = "Очистить историю", tint = palette.muted)
                }
            }
        }
        if (entries.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                color = palette.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, palette.outline)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 34.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(shape = CircleShape, color = palette.accentTint) {
                        Icon(
                            Icons.Outlined.History,
                            contentDescription = null,
                            modifier = Modifier.padding(15.dp),
                            tint = palette.accent
                        )
                    }
                    Text("Пока пусто", color = palette.text, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Text(
                        "Выполненные вычисления появятся здесь. Нажмите на запись, чтобы продолжить с тем же выражением.",
                        color = palette.muted,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            entries.forEachIndexed { index, entry ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(entry) },
                    shape = RoundedCornerShape(21.dp),
                    color = palette.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, palette.outline.copy(alpha = 0.7f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 17.dp, vertical = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(13.dp)
                    ) {
                        Surface(shape = CircleShape, color = palette.accentTint) {
                            Text(
                                (index + 1).toString().padStart(2, '0'),
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 7.dp),
                                color = palette.accent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                entry.expression,
                                color = palette.muted,
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                entry.result,
                                color = palette.text,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text("↗", color = palette.accent, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}
