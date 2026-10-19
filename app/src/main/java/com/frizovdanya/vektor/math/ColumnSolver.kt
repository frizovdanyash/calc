package com.frizovdanya.vektor.math

import java.math.BigInteger

data class ColumnSolution(
    val result: String,
    val vertical: String,
    val steps: List<String>
)

object ColumnSolver {
    fun solve(first: String, second: String, operation: Char): ColumnSolution {
        require(first.isNotBlank() && second.isNotBlank()) { "Введите оба числа" }
        require(first.all(Char::isDigit) && second.all(Char::isDigit)) { "Используйте целые неотрицательные числа" }
        val left = BigInteger(first)
        val right = BigInteger(second)
        return when (operation) {
            '+' -> addition(left, right)
            '−', '-' -> subtraction(left, right)
            '×', '*' -> multiplication(left, right)
            '÷', '/' -> division(left, right)
            else -> error("Неизвестная операция")
        }
    }

    private fun addition(left: BigInteger, right: BigInteger): ColumnSolution {
        val result = left + right
        val leftDigits = left.toString().reversed()
        val rightDigits = right.toString().reversed()
        var carry = 0
        val details = mutableListOf<String>()
        for (index in 0 until maxOf(leftDigits.length, rightDigits.length)) {
            val a = leftDigits.getOrNull(index)?.digitToInt() ?: 0
            val b = rightDigits.getOrNull(index)?.digitToInt() ?: 0
            val total = a + b + carry
            val nextCarry = total / 10
            val place = placeName(index)
            details += if (carry == 0) {
                "$place: $a + $b = $total, записываем ${total % 10}${if (nextCarry > 0) ", перенос $nextCarry" else ""}."
            } else {
                "$place: $a + $b + $carry = $total, записываем ${total % 10}${if (nextCarry > 0) ", перенос $nextCarry" else ""}."
            }
            carry = nextCarry
        }
        if (carry > 0) details += "Оставшийся перенос $carry записываем слева."
        return ColumnSolution(
            result.toString(),
            formatVertical(left.toString(), right.toString(), "+", result.toString()),
            details
        )
    }

    private fun subtraction(left: BigInteger, right: BigInteger): ColumnSolution {
        val negative = left < right
        val top = if (negative) right else left
        val bottom = if (negative) left else right
        val result = left - right
        val topDigits = top.toString().reversed()
        val bottomDigits = bottom.toString().reversed()
        var borrow = 0
        val details = mutableListOf<String>()
        if (negative) details += "Первое число меньше второго: меняем порядок вычитания и ставим знак минус."
        for (index in 0 until topDigits.length) {
            val original = topDigits[index].digitToInt()
            val lower = bottomDigits.getOrNull(index)?.digitToInt() ?: 0
            val current = original - borrow
            val place = placeName(index)
            if (current < lower) {
                details += "$place: занимаем 1 у следующего разряда: ${current + 10} − $lower = ${current + 10 - lower}."
                borrow = 1
            } else {
                details += "$place: $current − $lower = ${current - lower}."
                borrow = 0
            }
        }
        val vertical = formatVertical(left.toString(), right.toString(), "−", result.toString())
        return ColumnSolution(result.toString(), vertical, details)
    }

    private fun multiplication(left: BigInteger, right: BigInteger): ColumnSolution {
        val result = left * right
        val digits = right.toString().reversed()
        val partials = digits.mapIndexed { index, digit ->
            left * BigInteger.valueOf(digit.digitToInt().toLong()) to index
        }
        val width = maxOf(
            left.toString().length,
            right.toString().length + 2,
            result.toString().length,
            partials.maxOfOrNull { (part, shift) -> part.toString().length + shift } ?: 0
        )
        val rows = mutableListOf(left.toString().padStart(width), ("× " + right).padStart(width), "─".repeat(width))
        partials.filter { it.first != BigInteger.ZERO }.forEach { (part, shift) ->
            rows += part.toString().padStart(width - shift) + " ".repeat(shift)
        }
        if (partials.count { it.first != BigInteger.ZERO } > 1) rows += "─".repeat(width)
        rows += result.toString().padStart(width)
        val steps = digits.mapIndexed { index, digit ->
            val partial = left * BigInteger.valueOf(digit.digitToInt().toLong())
            val shifted = partial * BigInteger.TEN.pow(index)
            "Разряд $index: $left × ${digit.digitToInt()} = $partial${if (index > 0) "; сдвигаем на $index разряд(а), получаем $shifted" else ""}."
        }
        return ColumnSolution(result.toString(), rows.joinToString("\n"), steps)
    }

    private fun division(left: BigInteger, right: BigInteger): ColumnSolution {
        require(right != BigInteger.ZERO) { "На ноль делить нельзя" }
        val (quotient, remainder) = left.divideAndRemainder(right)
        val result = if (remainder == BigInteger.ZERO) quotient.toString() else "$quotient, остаток $remainder"
        val rows = listOf(
            "$left  ÷  $right",
            "─".repeat(maxOf(left.toString().length + right.toString().length + 4, 12)),
            if (remainder == BigInteger.ZERO) quotient.toString() else "$quotient  ост. $remainder"
        )
        var rest = BigInteger.ZERO
        val steps = left.toString().mapIndexed { index, digit ->
            val broughtDown = rest * BigInteger.TEN + BigInteger.valueOf(digit.digitToInt().toLong())
            val quotientDigit = broughtDown.divide(right)
            val product = quotientDigit * right
            val nextRest = broughtDown - product
            val prefix = left.toString().substring(0, index + 1)
            val detail = "$prefix: $broughtDown ÷ $right = $quotientDigit; $broughtDown − $product = $nextRest."
            rest = nextRest
            detail
        }
        return ColumnSolution(result, rows.joinToString("\n"), steps)
    }

    private fun formatVertical(left: String, right: String, operation: String, result: String): String {
        val width = maxOf(left.length, right.length + operation.length + 1, result.length)
        return listOf(
            left.padStart(width),
            (operation + " " + right).padStart(width),
            "─".repeat(width),
            result.padStart(width)
        ).joinToString("\n")
    }

    private fun placeName(index: Int): String {
        return when (index) {
            0 -> "Единицы"
            1 -> "Десятки"
            2 -> "Сотни"
            3 -> "Тысячи"
            else -> "Разряд ${index + 1}"
        }
    }
}
