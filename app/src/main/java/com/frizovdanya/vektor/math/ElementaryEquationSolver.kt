package com.frizovdanya.vektor.math

import kotlin.math.abs

data class ElementaryEquationSolution(
    val equation: String,
    val answer: String,
    val steps: List<String>
)

object ElementaryEquationSolver {
    fun solve(
        operation: Char,
        unknownFirst: Boolean,
        known: Double,
        target: Double,
        precision: Int = 4
    ): ElementaryEquationSolution {
        require(known.isFinite() && target.isFinite()) { "Проверь введённые числа." }
        val knownText = MathEngine.format(known, precision)
        val targetText = MathEngine.format(target, precision)
        val symbol = when (operation) {
            '+' -> "+"
            '−', '-' -> "−"
            '×', '*' -> "×"
            '÷', '/' -> "÷"
            else -> error("Выбери действие.")
        }
        val equation = if (unknownFirst) {
            "□ $symbol $knownText = $targetText"
        } else {
            "$knownText $symbol □ = $targetText"
        }
        val (value, explanation, calculation) = when (operation) {
            '+' -> Triple(
                target - known,
                "Чтобы найти неизвестное слагаемое, вычитаем известное число из ответа.",
                "$targetText − $knownText"
            )
            '−', '-' -> if (unknownFirst) {
                Triple(
                    target + known,
                    "Чтобы найти число до знака минус, складываем ответ и известное число.",
                    "$targetText + $knownText"
                )
            } else {
                Triple(
                    known - target,
                    "Чтобы найти число после знака минус, вычитаем ответ из известного числа.",
                    "$knownText − $targetText"
                )
            }
            '×', '*' -> {
                if (abs(known) < 1e-12) {
                    if (abs(target) < 1e-12) {
                        return ElementaryEquationSolution(
                            equation,
                            "Подойдёт любое число",
                            listOf("Ноль, умноженный на любое число, даёт ноль.")
                        )
                    }
                    return ElementaryEquationSolution(
                        equation,
                        "Решения нет",
                        listOf("Ноль, умноженный на любое число, не может дать ненулевой ответ.")
                    )
                }
                Triple(
                    target / known,
                    "Чтобы найти неизвестный множитель, делим ответ на известное число.",
                    "$targetText ÷ $knownText"
                )
            }
            '÷', '/' -> if (unknownFirst) {
                require(abs(known) >= 1e-12) { "На ноль делить нельзя." }
                Triple(
                    target * known,
                    "Чтобы найти число до знака деления, умножаем ответ на известное число.",
                    "$targetText × $knownText"
                )
            } else {
                if (abs(target) < 1e-12) {
                    if (abs(known) < 1e-12) {
                        return ElementaryEquationSolution(
                            equation,
                            "Любое ненулевое число",
                            listOf("Ноль, разделённый на любое ненулевое число, равен нулю.")
                        )
                    }
                    return ElementaryEquationSolution(
                        equation,
                        "Решения нет",
                        listOf("Ненулевое число нельзя разделить так, чтобы получить ноль.")
                    )
                }
                if (abs(known) < 1e-12) {
                    return ElementaryEquationSolution(
                        equation,
                        "Решения нет",
                        listOf("Ноль нельзя разделить на число и получить ненулевой ответ.")
                    )
                }
                Triple(
                    known / target,
                    "Чтобы найти число после знака деления, делим известное число на ответ.",
                    "$knownText ÷ $targetText"
                )
            }
            else -> error("Выбери действие.")
        }
        require(value.isFinite()) { "Такое число слишком велико для вычисления." }
        val answerText = MathEngine.format(value, precision)
        val checkedEquation = equation.replace("□", answerText)
        return ElementaryEquationSolution(
            equation = equation,
            answer = "□ = $answerText",
            steps = listOf(
                explanation,
                "$calculation = $answerText",
                "Проверка: $checkedEquation ✓"
            )
        )
    }
}
