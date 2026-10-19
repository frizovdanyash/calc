package com.frizovdanya.vektor.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class AccentChoice(
    val id: String,
    val label: String,
    val light: Color,
    val dark: Color
)

data class AppPalette(
    val isDark: Boolean,
    val background: Color,
    val surface: Color,
    val surfaceAlt: Color,
    val text: Color,
    val muted: Color,
    val accent: Color,
    val accentTint: Color,
    val outline: Color,
    val display: Color
)

val AccentChoices = listOf(
    AccentChoice("mint", "Мята", Color(0xFF147C66), Color(0xFF6CE0BC)),
    AccentChoice("blue", "Сапфир", Color(0xFF2863B7), Color(0xFF82B8FF)),
    AccentChoice("violet", "Ирис", Color(0xFF7952B3), Color(0xFFC4A1FF)),
    AccentChoice("amber", "Янтарь", Color(0xFFAC6500), Color(0xFFFFC46D)),
    AccentChoice("rose", "Роза", Color(0xFFB34E67), Color(0xFFFF9FB3))
)

fun appPalette(isDark: Boolean, choice: AccentChoice): AppPalette {
    val accent = if (isDark) choice.dark else choice.light
    return if (isDark) {
        AppPalette(
            isDark = true,
            background = Color(0xFF101615),
            surface = Color(0xFF19211F),
            surfaceAlt = Color(0xFF222D29),
            text = Color(0xFFEAF2EE),
            muted = Color(0xFF9BAAA3),
            accent = accent,
            accentTint = choice.dark.copy(alpha = 0.16f),
            outline = Color(0xFF34423C),
            display = Color(0xFF1A3831)
        )
    } else {
        AppPalette(
            isDark = false,
            background = Color(0xFFF4F6F2),
            surface = Color(0xFFFFFFFF),
            surfaceAlt = Color(0xFFE9EEEA),
            text = Color(0xFF18231F),
            muted = Color(0xFF718078),
            accent = accent,
            accentTint = choice.light.copy(alpha = 0.11f),
            outline = Color(0xFFDCE4DE),
            display = Color(0xFF173C34)
        )
    }
}

@Composable
fun VektorTheme(palette: AppPalette, content: @Composable () -> Unit) {
    val scheme = if (palette.isDark) {
        darkColorScheme(
            primary = palette.accent,
            onPrimary = Color(0xFF10221C),
            secondary = palette.accent,
            onSecondary = Color(0xFF10221C),
            background = palette.background,
            onBackground = palette.text,
            surface = palette.surface,
            onSurface = palette.text,
            surfaceVariant = palette.surfaceAlt,
            onSurfaceVariant = palette.muted,
            outline = palette.outline
        )
    } else {
        lightColorScheme(
            primary = palette.accent,
            onPrimary = Color.White,
            secondary = palette.accent,
            onSecondary = Color.White,
            background = palette.background,
            onBackground = palette.text,
            surface = palette.surface,
            onSurface = palette.text,
            surfaceVariant = palette.surfaceAlt,
            onSurfaceVariant = palette.muted,
            outline = palette.outline
        )
    }
    MaterialTheme(colorScheme = scheme, content = content)
}
