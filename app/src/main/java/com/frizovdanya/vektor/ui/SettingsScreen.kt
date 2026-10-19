package com.frizovdanya.vektor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    palette: AppPalette,
    themeMode: String,
    accentId: String,
    degrees: Boolean,
    precision: Int,
    onThemeModeChange: (String) -> Unit,
    onAccentChange: (String) -> Unit,
    onDegreesChange: (Boolean) -> Unit,
    onPrecisionChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        ScreenHeader(
            eyebrow = "ПОД ТВОЙ РИТМ",
            title = "Настройки",
            subtitle = "Собери свой удобный калькулятор.",
            palette = palette
        )
        SurfaceCard(palette) {
            Column(
                modifier = Modifier.padding(17.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                CardHeading("Оформление", "Интерфейс подстроится сразу.", palette)
                listOf("system" to "Как в системе", "light" to "Светлая тема", "dark" to "Тёмная тема").forEach { (mode, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onThemeModeChange(mode) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = themeMode == mode,
                            onClick = { onThemeModeChange(mode) }
                        )
                        Text(label, color = palette.text, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
        SurfaceCard(palette) {
            Column(
                modifier = Modifier.padding(vertical = 17.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 17.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    CardHeading("Цвет акцента", "Пять спокойных оттенков для кнопок и деталей.", palette)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 13.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AccentChoices.forEach { choice ->
                        val color = if (palette.isDark) choice.dark else choice.light
                        Column(
                            modifier = Modifier
                                .clickable { onAccentChange(choice.id) }
                                .padding(horizontal = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(43.dp)
                                    .background(
                                        color = if (accentId == choice.id) color else color.copy(alpha = 0.78f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (accentId == choice.id) {
                                    Icon(Icons.Outlined.Check, contentDescription = "Выбранный цвет", tint = if (palette.isDark) Color(0xFF10221C) else Color.White)
                                }
                            }
                            Text(choice.label, color = palette.muted, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
        SurfaceCard(palette) {
            Column(
                modifier = Modifier.padding(17.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                CardHeading("Вычисления", "Точность и тригонометрический режим.", palette)
                Text("Точность результата", color = palette.text, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                OptionSelector(
                    options = listOf("2" to "2", "4" to "4", "8" to "8", "12" to "12"),
                    selected = precision.toString(),
                    onSelect = { onPrecisionChange(it.toInt()) },
                    palette = palette
                )
                Text("Максимум знаков после запятой", color = palette.muted, fontSize = 11.sp)
                androidx.compose.material3.HorizontalDivider(color = palette.outline)
                Text("Углы в тригонометрии", color = palette.text, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                OptionSelector(
                    options = listOf("degrees" to "Градусы · DEG", "radians" to "Радианы · RAD"),
                    selected = if (degrees) "degrees" else "radians",
                    onSelect = { onDegreesChange(it == "degrees") },
                    palette = palette
                )
            }
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = palette.display
        ) {
            Column(
                modifier = Modifier.padding(19.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Text("VEKTOR  ·  1.0.0", color = Color(0xFF8CE6C6), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.3.sp)
                Text("Математика — яснее.", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Text("Создатель  @frizovdanya", color = Color(0xFFCBD9D2), fontSize = 13.sp)
                Text("Лицензия MIT · Открытый исходный код", color = Color(0xFFCBD9D2), fontSize = 12.sp)
                Text("Вычисления выполняются на устройстве.", color = Color(0xFF9FB5AA), fontSize = 11.sp)
            }
        }
    }
}
