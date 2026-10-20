package com.frizovdanya.vektor.ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.isSystemInDarkTheme
import com.frizovdanya.vektor.math.MathEngine
import org.json.JSONArray
import org.json.JSONObject

private enum class AppDestination {
    Calculator,
    Solver,
    History,
    Settings
}

data class HistoryEntry(
    val expression: String,
    val result: String
)

private data class NavigationItem(
    val destination: AppDestination,
    val label: String,
    val icon: ImageVector
)

object HistoryStorage {
    fun load(context: Context): List<HistoryEntry> {
        val stored = context.getSharedPreferences("vektor", Context.MODE_PRIVATE).getString("history", "[]") ?: "[]"
        return runCatching {
            val json = JSONArray(stored)
            (0 until json.length()).mapNotNull { index ->
                val item = json.optJSONObject(index) ?: return@mapNotNull null
                val expression = item.optString("expression")
                val result = item.optString("result")
                if (expression.isBlank()) null else HistoryEntry(expression, result)
            }
        }.getOrDefault(emptyList())
    }

    fun save(preferences: SharedPreferences, entries: List<HistoryEntry>) {
        val json = JSONArray()
        entries.take(100).forEach { entry ->
            json.put(JSONObject().put("expression", entry.expression).put("result", entry.result))
        }
        preferences.edit().putString("history", json.toString()).apply()
    }
}

@Composable
fun CalculatorApp() {
    val context = LocalContext.current
    val preferences = remember { context.getSharedPreferences("vektor", Context.MODE_PRIVATE) }
    var themeMode by remember { mutableStateOf(preferences.getString("theme", "system") ?: "system") }
    var accentId by remember { mutableStateOf(preferences.getString("accent", "system") ?: "system") }
    var degrees by remember { mutableStateOf(preferences.getBoolean("degrees", true)) }
    var precision by remember { mutableIntStateOf(preferences.getInt("precision", 8)) }
    var destination by rememberSaveable { mutableStateOf(AppDestination.Calculator) }
    var expression by rememberSaveable { mutableStateOf("") }
    var lastAnswer by rememberSaveable { mutableStateOf(preferences.getString("last_answer", "") ?: "") }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var justEvaluated by rememberSaveable { mutableStateOf(false) }
    val history = remember {
        mutableStateListOf<HistoryEntry>().apply { addAll(HistoryStorage.load(context)) }
    }
    val systemDark = isSystemInDarkTheme()
    val dark = when (themeMode) {
        "dark" -> true
        "light" -> false
        else -> systemDark
    }
    val accent = AccentChoices.firstOrNull { it.id == accentId } ?: AccentChoices.first()
    val dynamicScheme = if (accentId == "system" && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        null
    }
    val palette = dynamicScheme?.let { appPalette(dark, it) } ?: appPalette(dark, accent)

    SideEffect {
        val window = (context as? Activity)?.window
        if (window != null) {
            window.statusBarColor = palette.background.toArgb()
            window.navigationBarColor = palette.background.toArgb()
            WindowCompat.getInsetsController(window, window.decorView).apply {
                isAppearanceLightStatusBars = !dark
                isAppearanceLightNavigationBars = !dark
            }
        }
    }

    val savePreferences: (String, Any) -> Unit = { key, value ->
        preferences.edit().apply {
            when (value) {
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
            }
        }.apply()
    }

    fun addToHistory(expressionText: String, resultText: String) {
        if (history.firstOrNull()?.let { it.expression == expressionText && it.result == resultText } != true) {
            history.add(0, HistoryEntry(expressionText, resultText))
            while (history.size > 100) history.removeAt(history.lastIndex)
            HistoryStorage.save(preferences, history)
        }
    }

    fun evaluateExpression() {
        if (expression.isBlank()) return
        runCatching { MathEngine.evaluate(expression, degrees) }
            .onSuccess { value ->
                val raw = MathEngine.raw(value)
                val result = MathEngine.format(value, precision)
                lastAnswer = raw
                preferences.edit().putString("last_answer", raw).apply()
                addToHistory(expression, result)
                errorMessage = null
                justEvaluated = true
            }
            .onFailure { failure ->
                errorMessage = failure.message ?: "Не удалось вычислить выражение"
            }
    }

    val handleKey: (String) -> Unit = { key ->
        errorMessage = null
        when (key) {
            "AC" -> {
                expression = ""
                justEvaluated = false
            }
            "DEL" -> {
                expression = expression.dropLast(1)
                justEvaluated = false
            }
            "=" -> evaluateExpression()
            "±" -> {
                if (expression.isBlank()) expression = "−" else expression += "×(−1)"
                justEvaluated = false
            }
            else -> {
                val startsFresh = key.firstOrNull()?.isDigit() == true || key == "." || key == "π" || key == "e" || key == "ANS" || key == "("
                if (justEvaluated && startsFresh) expression = ""
                justEvaluated = false
                expression += when (key) {
                    "sin" -> "sin("
                    "cos" -> "cos("
                    "tan" -> "tan("
                    "asin" -> "asin("
                    "acos" -> "acos("
                    "atan" -> "atan("
                    "√" -> "sqrt("
                    "ln" -> "ln("
                    "log" -> "log("
                    "x²" -> "^2"
                    "xʸ" -> "^"
                    "ANS" -> lastAnswer.ifBlank { "0" }
                    else -> key
                }
            }
        }
    }

    val items = listOf(
        NavigationItem(AppDestination.Calculator, "Калькулятор", Icons.Outlined.Calculate),
        NavigationItem(AppDestination.Solver, "Решение", Icons.Outlined.School),
        NavigationItem(AppDestination.History, "История", Icons.Outlined.History),
        NavigationItem(AppDestination.Settings, "Настройки", Icons.Outlined.Tune)
    )

    CalculatorTheme(palette) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = palette.background,
            bottomBar = {
                NavigationBar(
                    containerColor = palette.surface,
                    tonalElevation = 0.dp
                ) {
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = destination == item.destination,
                            onClick = { destination = item.destination },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = {
                                Text(
                                    item.label,
                                    fontWeight = if (destination == item.destination) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = palette.accent,
                                selectedTextColor = palette.accent,
                                indicatorColor = palette.accentTint,
                                unselectedIconColor = palette.muted,
                                unselectedTextColor = palette.muted
                            )
                        )
                    }
                }
            }
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                when (destination) {
                    AppDestination.Calculator -> CalculatorScreen(
                        expression = expression,
                        preview = if (expression.isBlank()) "0" else runCatching {
                            MathEngine.format(MathEngine.evaluate(expression, degrees), precision)
                        }.getOrDefault("—"),
                        errorMessage = errorMessage,
                        degrees = degrees,
                        lastAnswer = lastAnswer,
                        palette = palette,
                        onKey = handleKey,
                        onEvaluate = ::evaluateExpression,
                        onToggleAngle = {
                            degrees = !degrees
                            savePreferences("degrees", degrees)
                        },
                        onOpenHistory = { destination = AppDestination.History }
                    )
                    AppDestination.Solver -> SolverScreen(
                        palette = palette,
                        precision = precision
                    )
                    AppDestination.History -> HistoryScreen(
                        entries = history,
                        palette = palette,
                        onSelect = { entry ->
                            expression = entry.expression
                            errorMessage = null
                            justEvaluated = false
                            destination = AppDestination.Calculator
                        },
                        onClear = {
                            history.clear()
                            HistoryStorage.save(preferences, history)
                        }
                    )
                    AppDestination.Settings -> SettingsScreen(
                        palette = palette,
                        themeMode = themeMode,
                        accentId = accentId,
                        degrees = degrees,
                        precision = precision,
                        onThemeModeChange = {
                            themeMode = it
                            savePreferences("theme", it)
                        },
                        onAccentChange = {
                            accentId = it
                            savePreferences("accent", it)
                        },
                        onDegreesChange = {
                            degrees = it
                            savePreferences("degrees", it)
                        },
                        onPrecisionChange = {
                            precision = it
                            savePreferences("precision", it)
                        }
                    )
                }
            }
        }
    }
}
