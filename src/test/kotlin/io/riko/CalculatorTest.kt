package io.riko

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CalculatorTest {

    private val stocks = setOf(
        Stock(ticker = "AAA", dividendYield = 4.90, rating = 5),
        Stock(ticker = "BBB", dividendYield = 3.29, rating = 5),
        Stock(ticker = "CCC", dividendYield = 6.13, rating = 4),
        Stock(ticker = "DDD", dividendYield = 5.69, rating = 2),
        Stock(ticker = "EEE", dividendYield = 4.91, rating = 5),
        Stock(ticker = "FFF", dividendYield = 4.92, rating = 4),
        Stock(ticker = "GGG", dividendYield = 4.84, rating = 2),
        Stock(ticker = "HHH", dividendYield = 4.87, rating = 4),
        Stock(ticker = "III", dividendYield = 3.74, rating = 3),
        Stock(ticker = "JJJ", dividendYield = 4.46, rating = 1),
        Stock(ticker = "LLL", dividendYield = 3.90, rating = 4),
        Stock(ticker = "MMM", dividendYield = 6.95, rating = 5),
        Stock(ticker = "NNN", dividendYield = 2.13, rating = 1),
        Stock(ticker = "OOO", dividendYield = 3.69, rating = 4),
        Stock(ticker = "PPP", dividendYield = 4.10, rating = 2),
        Stock(ticker = "QQQ", dividendYield = 2.92, rating = 3),
        Stock(ticker = "RRR", dividendYield = 1.84, rating = 1),
        Stock(ticker = "SSS", dividendYield = 3.57, rating = 4),
        Stock(ticker = "TTT", dividendYield = 6.14, rating = 2),
        Stock(ticker = "UUU", dividendYield = 4.06, rating = 1),
        Stock(ticker = "VVV", dividendYield = 4.06, rating = 1),
        Stock(ticker = "XXX", dividendYield = 1.30, rating = 2),
        Stock(ticker = "ZZZ", dividendYield = 0.00, rating = 3)
    )

    @Test
    fun `Calculate YELD strategy`() {
        val input = CalculationInput(Strategy.YIELD, stocks)
        val result = calculatePie(input)
        result.forEach(::println)

        assertEquals(100.0, result.sumByDouble { it.weight }, 0.1)
    }

    @Test
    fun `Calculate RATING strategy`() {
        val input = CalculationInput(Strategy.RATING, stocks)
        val result = calculatePie(input)
        result.forEach(::println)

        assertEquals(100.0, result.sumByDouble { it.weight }, 0.1)
    }

    @Test
    fun `Calculate RATING AND YIELD strategy`() {
        val stocks = setOf(
            Stock(ticker = "AAA", rating = 5, dividendYield = 3.0),
            Stock(ticker = "BBB", rating = 4, dividendYield = 5.0),
            Stock(ticker = "CCC", rating = 3, dividendYield = 4.0),
            Stock(ticker = "DDD", rating = 5, dividendYield = 1.0)
        )

        val result = calculatePie(CalculationInput(Strategy.RATING_YIELD_64, stocks))
        result.forEach(::println)

        assertEquals(100.0, result.sumByDouble { it.weight }, 0.1)
    }
}