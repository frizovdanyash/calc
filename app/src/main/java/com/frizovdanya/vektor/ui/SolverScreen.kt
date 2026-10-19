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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frizovdanya.vektor.math.ColumnSolution
import com.frizovdanya.vektor.math.ColumnSolver
import com.frizovdanya.vektor.math.EquationSolution
import com.frizovdanya.vektor.math.EquationSolver
import com.frizovdanya.vektor.math.MathEngine
import com.frizovdanya.vektor.math.UnitCategory
import com.frizovdanya.vektor.math.UnitConverter
import com.frizovdanya.vektor.math.UnitDefinition

@Composable
fun SolverScreen(palette: AppPalette, precision: Int) {
    var mode by rememberSaveable { mutableStateOf("column") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(17.dp)
    ) {
        ScreenHeader(
            eyebrow = "ПОНЯТНЫЙ ПРОЦЕСС",
            title = "Разбираем задачу",
            subtitle = "Не только ответ — каждый шаг на виду.",
            palette = palette
        )
        OptionSelector(
            options = listOf("column" to "Столбик", "equation" to "Уравнения", "convert" to "Конвертер"),
            selected = mode,
            onSelect = { mode = it },
            palette = palette
        )
        when (mode) {
            "column" -> ColumnSolverPanel(palette)
            "equation" -> EquationSolverPanel(palette, precision)
            else -> UnitConverterPanel(palette, precision)
        }
    }
}

@Composable
private fun ColumnSolverPanel(palette: AppPalette) {
    var first by rememberSaveable { mutableStateOf("248") }
    var second by rememberSaveable { mutableStateOf("36") }
    var operation by rememberSaveable { mutableStateOf("×") }
    val calculation = remember(first, second, operation) {
        runCatching { ColumnSolver.solve(first, second, operation.first()) }
    }
    val solution = calculation.getOrNull()

    Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {
        SurfaceCard(palette) {
            Column(
                modifier = Modifier.padding(17.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                CardHeading("Числовой пример", "Целые числа — с переносами и промежуточными разрядами.", palette)
                OptionSelector(
                    options = listOf("+" to "+", "−" to "−", "×" to "×", "÷" to "÷"),
                    selected = operation,
                    onSelect = { operation = it },
                    palette = palette
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    NumericField(
                        value = first,
                        onValueChange = { first = it.filter(Char::isDigit) },
                        label = "Первое число",
                        palette = palette,
                        modifier = Modifier.weight(1f),
                        allowNegative = false
                    )
                    NumericField(
                        value = second,
                        onValueChange = { second = it.filter(Char::isDigit) },
                        label = "Второе число",
                        palette = palette,
                        modifier = Modifier.weight(1f),
                        allowNegative = false
                    )
                }
            }
        }
        if (solution != null) {
            SurfaceCard(palette) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(13.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            CardHeading("Решение в столбик", "Вычисление по разрядам", palette)
                        }
                        Surface(shape = CircleShape, color = palette.accentTint) {
                            Text(
                                solution.result,
                                modifier = Modifier.padding(horizontal = 13.dp, vertical = 8.dp),
                                color = palette.accent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Text(
                        solution.vertical,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(palette.surfaceAlt.copy(alpha = 0.55f), RoundedCornerShape(17.dp))
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        color = palette.text,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 20.sp,
                        lineHeight = 30.sp,
                        textAlign = TextAlign.End
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        solution.steps.forEachIndexed { index, step ->
                            StepRow(index + 1, step, palette)
                        }
                    }
                }
            }
        } else {
            ErrorCard(calculation.exceptionOrNull()?.message ?: "Проверьте введённые числа.", palette)
        }
        SurfaceCard(palette) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("↳", color = palette.accent, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Поддерживаются сложение, вычитание, умножение и деление с остатком.",
                    color = palette.muted,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun EquationSolverPanel(palette: AppPalette, precision: Int) {
    var mode by rememberSaveable { mutableStateOf("linear") }
    var a by rememberSaveable { mutableStateOf("2") }
    var b by rememberSaveable { mutableStateOf("3") }
    var c by rememberSaveable { mutableStateOf("11") }
    val solutionResult = remember(mode, a, b, c, precision) {
        runCatching {
            val first = a.replace(',', '.').toDoubleOrNull() ?: error("Введите коэффициент a")
            val second = b.replace(',', '.').toDoubleOrNull() ?: error("Введите коэффициент b")
            val third = c.replace(',', '.').toDoubleOrNull() ?: error("Введите коэффициент c")
            if (mode == "linear") {
                EquationSolver.solveLinear(first, second, third, precision)
            } else {
                EquationSolver.solveQuadratic(first, second, third, precision)
            }
        }
    }
    val solution = solutionResult.getOrNull()

    Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {
        SurfaceCard(palette) {
            Column(
                modifier = Modifier.padding(17.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CardHeading("Выбери тип", "Коэффициенты можно менять — решение обновляется сразу.", palette)
                OptionSelector(
                    options = listOf("linear" to "Линейное", "quadratic" to "Квадратное"),
                    selected = mode,
                    onSelect = {
                        mode = it
                        if (it == "linear") {
                            a = "2"
                            b = "3"
                            c = "11"
                        } else {
                            a = "1"
                            b = "-5"
                            c = "6"
                        }
                    },
                    palette = palette
                )
                Surface(
                    shape = RoundedCornerShape(17.dp),
                    color = palette.accentTint
                ) {
                    Text(
                        if (mode == "linear") "a·x + b = c" else "a·x² + b·x + c = 0",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        color = palette.accent,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumericField(a, { a = it }, "a", palette, Modifier.weight(1f))
                    NumericField(b, { b = it }, "b", palette, Modifier.weight(1f))
                    NumericField(c, { c = it }, "c", palette, Modifier.weight(1f))
                }
            }
        }
        if (solution != null) {
            EquationResultCard(solution, palette)
        } else {
            ErrorCard(solutionResult.exceptionOrNull()?.message ?: "Укажите все коэффициенты.", palette)
        }
    }
}

@Composable
private fun EquationResultCard(solution: EquationSolution, palette: AppPalette) {
    SurfaceCard(palette) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CardHeading("Ответ", "Формула и проверка по шагам", palette)
            Text(
                solution.result,
                color = palette.accent,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 32.sp
            )
            androidx.compose.material3.HorizontalDivider(color = palette.outline)
            solution.steps.forEachIndexed { index, step ->
                StepRow(index + 1, step, palette)
            }
        }
    }
}

@Composable
private fun UnitConverterPanel(palette: AppPalette, precision: Int) {
    var categoryIndex by rememberSaveable { mutableIntStateOf(0) }
    var amount by rememberSaveable { mutableStateOf("1") }
    var fromIndex by rememberSaveable { mutableIntStateOf(0) }
    var toIndex by rememberSaveable { mutableIntStateOf(1) }
    val category = UnitConverter.categories[categoryIndex.coerceIn(UnitConverter.categories.indices)]
    val from = category.units[fromIndex.coerceIn(category.units.indices)]
    val to = category.units[toIndex.coerceIn(category.units.indices)]
    val result = amount.replace(',', '.').toDoubleOrNull()?.let {
        runCatching { UnitConverter.convert(it, from, to) }.getOrNull()
    }

    Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {
        SurfaceCard(palette) {
            Column(
                modifier = Modifier.padding(17.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                CardHeading("Быстрый пересчёт", "Выбери величину и получи точное значение.", palette)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    UnitConverter.categories.forEachIndexed { index, item ->
                        CategoryChip(
                            category = item,
                            selected = index == categoryIndex,
                            palette = palette,
                            onClick = {
                                categoryIndex = index
                                fromIndex = 0
                                toIndex = if (item.units.size > 1) 1 else 0
                            }
                        )
                    }
                }
                NumericField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = "Значение",
                    palette = palette,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    UnitPicker(
                        label = "ИЗ",
                        selected = from,
                        units = category.units,
                        palette = palette,
                        modifier = Modifier.weight(1f),
                        onSelect = { fromIndex = category.units.indexOf(it) }
                    )
                    IconButton(
                        onClick = {
                            val previousFrom = fromIndex
                            fromIndex = toIndex
                            toIndex = previousFrom
                        },
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(Icons.Outlined.SwapHoriz, contentDescription = "Поменять единицы", tint = palette.accent)
                    }
                    UnitPicker(
                        label = "В",
                        selected = to,
                        units = category.units,
                        palette = palette,
                        modifier = Modifier.weight(1f),
                        onSelect = { toIndex = category.units.indexOf(it) }
                    )
                }
            }
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            color = palette.display
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Text("ПОЛУЧАЕТСЯ", color = Color(0xFFB7CDC3), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                Text(
                    result?.let { MathEngine.format(it, precision) } ?: "—",
                    color = Color(0xFF8CE6C6),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 34.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(to.name, color = Color(0xFFCBD9D2), fontSize = 13.sp)
            }
        }
        Text(
            "Встроено 8 категорий: длина, масса, температура, площадь, объём, скорость, время и данные.",
            color = palette.muted,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun CategoryChip(
    category: UnitCategory,
    selected: Boolean,
    palette: AppPalette,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(37.dp)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = if (selected) palette.accent else palette.surfaceAlt,
        border = if (selected) null else androidx.compose.foundation.BorderStroke(1.dp, palette.outline.copy(alpha = 0.65f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                category.name,
                modifier = Modifier.padding(horizontal = 14.dp),
                color = if (selected) Color.White else palette.text,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}

@Composable
private fun UnitPicker(
    label: String,
    selected: UnitDefinition,
    units: List<UnitDefinition>,
    palette: AppPalette,
    modifier: Modifier = Modifier,
    onSelect: (UnitDefinition) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            shape = RoundedCornerShape(17.dp),
            color = palette.surfaceAlt.copy(alpha = 0.62f),
            border = androidx.compose.foundation.BorderStroke(1.dp, palette.outline)
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(label, color = palette.muted, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text(
                    selected.name,
                    color = palette.text,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            units.forEach { unit ->
                DropdownMenuItem(
                    text = { Text(unit.name) },
                    onClick = {
                        expanded = false
                        onSelect(unit)
                    }
                )
            }
        }
    }
}

@Composable
private fun StepRow(index: Int, text: String, palette: AppPalette) {
    Row(horizontalArrangement = Arrangement.spacedBy(11.dp), verticalAlignment = Alignment.Top) {
        Surface(shape = CircleShape, color = palette.accentTint) {
            Text(
                index.toString(),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = palette.accent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(text, modifier = Modifier.weight(1f), color = palette.text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun ErrorCard(message: String, palette: AppPalette) {
    SurfaceCard(palette) {
        Text(
            message,
            modifier = Modifier.padding(17.dp),
            color = if (palette.isDark) Color(0xFFFFB5A9) else Color(0xFF9C443E),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
