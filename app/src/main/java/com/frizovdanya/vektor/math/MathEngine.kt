package com.frizovdanya.vektor.math

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

object MathEngine {
    fun evaluate(expression: String, degrees: Boolean = true): Double {
        return Parser(expression, degrees).parse()
    }

    fun format(value: Double, precision: Int = 8): String {
        if (value.isNaN()) return "Не число"
        if (value == Double.POSITIVE_INFINITY) return "∞"
        if (value == Double.NEGATIVE_INFINITY) return "−∞"
        if (abs(value) < 0.0000000000005) return "0"
        val digits = precision.coerceIn(0, 12)
        val pattern = if (digits == 0) "#,##0" else "#,##0." + "#".repeat(digits)
        val symbols = DecimalFormatSymbols(Locale("ru", "RU"))
        return DecimalFormat(pattern, symbols).apply {
            roundingMode = RoundingMode.HALF_UP
            isGroupingUsed = true
        }.format(value)
    }

    fun raw(value: Double): String {
        if (!value.isFinite()) return value.toString()
        if (abs(value) < 0.0000000000005) return "0"
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString()
    }

    private class Parser(source: String, private val degrees: Boolean) {
        private val input = source
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
            .replace("π", "pi")
            .replace(",", ".")
        private var position = 0

        fun parse(): Double {
            val value = expression()
            skipSpaces()
            require(position == input.length) { "Лишний символ в выражении" }
            require(value.isFinite()) { "Результат не определён" }
            return value
        }

        private fun expression(): Double {
            var value = term()
            while (true) {
                skipSpaces()
                value = when {
                    consume('+') -> value + term()
                    consume('-') -> value - term()
                    else -> return value
                }
            }
        }

        private fun term(): Double {
            var value = unary()
            while (true) {
                skipSpaces()
                value = when {
                    consume('*') -> value * unary()
                    consume('/') -> value / unary()
                    startsImplicitPrimary() -> value * unary()
                    else -> return value
                }
            }
        }

        private fun unary(): Double {
            skipSpaces()
            return when {
                consume('+') -> unary()
                consume('-') -> -unary()
                else -> power()
            }
        }

        private fun power(): Double {
            val value = postfix()
            skipSpaces()
            return if (consume('^')) value.pow(unary()) else value
        }

        private fun postfix(): Double {
            var value = primary()
            while (true) {
                skipSpaces()
                when {
                    consume('!') -> value = factorial(value)
                    consume('%') -> value /= 100.0
                    else -> return value
                }
            }
        }

        private fun primary(): Double {
            skipSpaces()
            if (consume('(')) {
                val value = expression()
                skipSpaces()
                require(consume(')')) { "Не закрыта скобка" }
                return value
            }
            if (position < input.length && (input[position].isDigit() || input[position] == '.')) {
                return number()
            }
            val name = identifier().lowercase(Locale.ROOT)
            return when (name) {
                "pi" -> PI
                "e" -> kotlin.math.E
                "sin", "cos", "tan", "asin", "acos", "atan", "sqrt", "ln", "log", "log10", "abs", "exp" -> function(name)
                else -> error("Неизвестная функция: $name")
            }
        }

        private fun function(name: String): Double {
            skipSpaces()
            require(consume('(')) { "После $name нужна скобка" }
            val argument = expression()
            skipSpaces()
            require(consume(')')) { "Не закрыта скобка функции" }
            val radians = if (degrees) argument * PI / 180.0 else argument
            return when (name) {
                "sin" -> sin(radians)
                "cos" -> cos(radians)
                "tan" -> tan(radians)
                "asin" -> angleResult(asin(argument))
                "acos" -> angleResult(acos(argument))
                "atan" -> angleResult(atan(argument))
                "sqrt" -> sqrt(argument)
                "ln" -> ln(argument)
                "log", "log10" -> log10(argument)
                "abs" -> abs(argument)
                "exp" -> exp(argument)
                else -> error("Неизвестная функция: $name")
            }
        }

        private fun angleResult(radians: Double): Double {
            return if (degrees) radians * 180.0 / PI else radians
        }

        private fun factorial(value: Double): Double {
            require(value >= 0.0 && value <= 170.0 && value == floor(value)) {
                "Факториал определён для целых чисел от 0 до 170"
            }
            var result = 1.0
            for (factor in 2..value.toInt()) result *= factor
            return result
        }

        private fun number(): Double {
            val start = position
            var decimalPoint = false
            while (position < input.length) {
                val character = input[position]
                if (character.isDigit()) {
                    position++
                } else if (character == '.' && !decimalPoint) {
                    decimalPoint = true
                    position++
                } else {
                    break
                }
            }
            require(position > start && input.substring(start, position) != ".") { "Некорректное число" }
            return input.substring(start, position).toDouble()
        }

        private fun identifier(): String {
            val start = position
            while (position < input.length && (input[position].isLetter() || input[position] == '_')) {
                position++
            }
            require(position > start) { "Ожидалось число или функция" }
            return input.substring(start, position)
        }

        private fun startsImplicitPrimary(): Boolean {
            if (position >= input.length) return false
            val character = input[position]
            return character == '(' || character.isLetter()
        }

        private fun consume(character: Char): Boolean {
            if (position < input.length && input[position] == character) {
                position++
                return true
            }
            return false
        }

        private fun skipSpaces() {
            while (position < input.length && input[position].isWhitespace()) position++
        }
    }
}
