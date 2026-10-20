package com.frizovdanya.vektor.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class AccentChoice(
    val id: String,
    val label: String,
    val light: Color,
    val dark: Color,
    val lightContainer: Color,
    val darkContainer: Color
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
    val onAccent: Color,
    val outline: Color,
    val display: Color
)

val AccentChoices = listOf(
    AccentChoice("system", "Система", Color(0xFF6750A4), Color(0xFFD0BCFF), Color(0xFFEADDFF), Color(0xFF4F378B)),
    AccentChoice("mint", "Зелёный", Color(0xFF386A20), Color(0xFFA1D18C), Color(0xFFD4E8C5), Color(0xFF234D12)),
    AccentChoice("blue", "Синий", Color(0xFF0061A4), Color(0xFF9ECAFF), Color(0xFFD1E4FF), Color(0xFF00497D)),
    AccentChoice("violet", "Фиолетовый", Color(0xFF6750A4), Color(0xFFD0BCFF), Color(0xFFEADDFF), Color(0xFF4F378B)),
    AccentChoice("amber", "Оранжевый", Color(0xFF8B5000), Color(0xFFFFB95C), Color(0xFFFFDCBE), Color(0xFF633F00)),
    AccentChoice("rose", "Розовый", Color(0xFFA90057), Color(0xFFFFB0C8), Color(0xFFFFD9E2), Color(0xFF7D003D))
)

fun appPalette(isDark: Boolean, choice: AccentChoice): AppPalette {
    return if (isDark) {
        AppPalette(
            isDark = true,
            background = Color(0xFF141218),
            surface = Color(0xFF141218),
            surfaceAlt = Color(0xFF2B2930),
            text = Color(0xFFE6E1E9),
            muted = Color(0xFFCAC4D0),
            accent = choice.dark,
            accentTint = choice.darkContainer,
            onAccent = Color(0xFF1C1B1F),
            outline = Color(0xFF938F99),
            display = Color(0xFF29272F)
        )
    } else {
        AppPalette(
            isDark = false,
            background = Color(0xFFFAF8FF),
            surface = Color(0xFFFAF8FF),
            surfaceAlt = Color(0xFFF0EDF5),
            text = Color(0xFF1C1B1F),
            muted = Color(0xFF49454F),
            accent = choice.light,
            accentTint = choice.lightContainer,
            onAccent = Color.White,
            outline = Color(0xFF79747E),
            display = Color(0xFFF0EDF5)
        )
    }
}

fun appPalette(isDark: Boolean, scheme: ColorScheme): AppPalette {
    return AppPalette(
        isDark = isDark,
        background = scheme.background,
        surface = scheme.surface,
        surfaceAlt = scheme.surfaceVariant,
        text = scheme.onSurface,
        muted = scheme.onSurfaceVariant,
        accent = scheme.primary,
        accentTint = scheme.primaryContainer,
        onAccent = scheme.onPrimary,
        outline = scheme.outline,
        display = scheme.surfaceVariant
    )
}

@Composable
fun CalculatorTheme(palette: AppPalette, content: @Composable () -> Unit) {
    val scheme = if (palette.isDark) {
        darkColorScheme(
            primary = palette.accent,
            onPrimary = palette.onAccent,
            primaryContainer = palette.accentTint,
            onPrimaryContainer = palette.text,
            secondary = palette.accent,
            onSecondary = palette.onAccent,
            secondaryContainer = palette.accentTint,
            onSecondaryContainer = palette.text,
            background = palette.background,
            onBackground = palette.text,
            surface = palette.surface,
            onSurface = palette.text,
            surfaceVariant = palette.surfaceAlt,
            onSurfaceVariant = palette.muted,
            outline = palette.outline,
            outlineVariant = palette.surfaceAlt
        )
    } else {
        lightColorScheme(
            primary = palette.accent,
            onPrimary = palette.onAccent,
            primaryContainer = palette.accentTint,
            onPrimaryContainer = palette.text,
            secondary = palette.accent,
            onSecondary = palette.onAccent,
            secondaryContainer = palette.accentTint,
            onSecondaryContainer = palette.text,
            background = palette.background,
            onBackground = palette.text,
            surface = palette.surface,
            onSurface = palette.text,
            surfaceVariant = palette.surfaceAlt,
            onSurfaceVariant = palette.muted,
            outline = palette.outline,
            outlineVariant = palette.surfaceAlt
        )
    }
    MaterialTheme(colorScheme = scheme, content = content)
}
