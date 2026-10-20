package com.frizovdanya.vektor.math

import kotlin.math.abs
import kotlin.math.sqrt

data class EquationSolution(
    val result: String,
    val steps: List<String>
)

object EquationSolver {
    fun solveLinear(a: Double, b: Double, c: Double, precision: Int = 8): EquationSolution {
        require(listOf(a, b, c).all { it.isFinite() }) { "Введите корректные коэффициенты" }
        val aText = MathEngine.format(a, precision)
        val bText = MathEngine.format(abs(b), precision)
        val cText = MathEngine.format(c, precision)
        val middle = if (b < 0.0) " − $bText" else " + $bText"
        val steps = mutableListOf("Исходное уравнение: ${aText}x$middle = $cText")
        if (abs(a) < 1e-12) {
            return if (abs(b - c) < 1e-12) {
                EquationSolution("Бесконечно много решений", steps + "Коэффициент при x равен нулю, а левая и правая части совпадают для любого x.")
            } else {
                EquationSolution("Решений нет", steps + "Коэффициент при x равен нулю, но постоянные части различаются.")
            }
        }
        val numerator = c - b
        val result = numerator / a
        steps += "Переносим свободный член: ${aText}x = ${MathEngine.format(c, precision)} − (${MathEngine.format(b, precision)}) = ${MathEngine.format(numerator, precision)}."
        steps += "Делим обе части на ${MathEngine.format(a, precision)}: x = ${MathEngine.format(numerator, precision)} ÷ ${MathEngine.format(a, precision)}."
        steps += "Проверка: ${MathEngine.format(a, precision)} × ${MathEngine.format(result, precision)} + ${MathEngine.format(b, precision)} = ${MathEngine.format(c, precision)}."
        return EquationSolution("x = ${MathEngine.format(result, precision)}", steps)
    }

    fun solveQuadratic(a: Double, b: Double, c: Double, precision: Int = 8): EquationSolution {
        require(listOf(a, b, c).all { it.isFinite() }) { "Введите корректные коэффициенты" }
        if (abs(a) < 1e-12) {
            val linear = solveLinear(b, c, 0.0, precision)
            return EquationSolution(linear.result, listOf("Коэффициент a равен нулю, поэтому уравнение становится линейным.") + linear.steps)
        }
        val aText = MathEngine.format(a, precision)
        val bText = MathEngine.format(b, precision)
        val cText = MathEngine.format(c, precision)
        val discriminant = b * b - 4.0 * a * c
        val steps = mutableListOf(
            "Исходное уравнение: ${aText}x² + ${bText}x + $cText = 0.",
            "D = b² − 4ac = (${bText})² − 4 × ${aText} × ${cText} = ${MathEngine.format(discriminant, precision)}."
        )
        if (discriminant < -1e-12) {
            val realPart = -b / (2.0 * a)
            val imaginaryPart = sqrt(-discriminant) / abs(2.0 * a)
            steps += "D меньше нуля, поэтому корни комплексные."
            return EquationSolution(
                "x₁,₂ = ${MathEngine.format(realPart, precision)} ± ${MathEngine.format(imaginaryPart, precision)}i",
                steps
            )
        }
        if (abs(discriminant) <= 1e-12) {
            val root = -b / (2.0 * a)
            steps += "D = 0: уравнение имеет один двойной корень x = −b ÷ (2a)."
            return EquationSolution("x₁ = x₂ = ${MathEngine.format(root, precision)}", steps)
        }
        val squareRoot = sqrt(discriminant)
        val firstRoot = (-b + squareRoot) / (2.0 * a)
        val secondRoot = (-b - squareRoot) / (2.0 * a)
        steps += "По формуле x = (−b ± √D) ÷ (2a): √D = ${MathEngine.format(squareRoot, precision)}, 2a = ${MathEngine.format(2.0 * a, precision)}."
        steps += "Подставляем значения отдельно для каждого корня."
        return EquationSolution(
            "x₁ = ${MathEngine.format(firstRoot, precision)}\nx₂ = ${MathEngine.format(secondRoot, precision)}",
            steps
        )
    }
}
