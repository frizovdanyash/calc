package com.frizovdanya.vektor

import com.frizovdanya.vektor.math.ColumnSolver
import com.frizovdanya.vektor.math.EquationSolver
import com.frizovdanya.vektor.math.MathEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
class MathEngineTest {
    @Test
    fun evaluatesOperatorPrecedenceAndParentheses() {
        assertEquals(14.0, MathEngine.evaluate("2 + 3 × 4"), 1e-10)
        assertEquals(20.0, MathEngine.evaluate("(2 + 3) × 4"), 1e-10)
        assertEquals(14.0, MathEngine.evaluate("2(3 + 4)"), 1e-10)
    }

    @Test
    fun evaluatesScientificFunctionsAndModes() {
        assertEquals(0.5, MathEngine.evaluate("sin(30)"), 1e-10)
        assertEquals(-1.0, MathEngine.evaluate("cos(π)", degrees = false), 1e-10)
        assertEquals(120.0, MathEngine.evaluate("5!"), 1e-10)
        assertEquals(3.0, MathEngine.evaluate("sqrt(9)"), 1e-10)
    }

    @Test
    fun solvesColumnArithmetic() {
        assertEquals("8928", ColumnSolver.solve("248", "36", '×').result)
        assertEquals("1000", ColumnSolver.solve("587", "413", '+').result)
        assertEquals("3, остаток 2", ColumnSolver.solve("17", "5", '÷').result)
    }

    @Test
    fun solvesLinearAndQuadraticEquations() {
        val linear = EquationSolver.solveLinear(2.0, 3.0, 11.0)
        assertTrue(linear.result.contains("4"))
        val quadratic = EquationSolver.solveQuadratic(1.0, -5.0, 6.0)
        assertTrue(quadratic.result.contains("2"))
        assertTrue(quadratic.result.contains("3"))
        assertTrue(EquationSolver.solveQuadratic(1.0, 2.0, 5.0).result.contains("±"))
    }
}
