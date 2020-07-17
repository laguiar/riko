package io.riko

data class Stock(
    val ticker: String,
    val rating: Int,
    val dividendYield: Double
)

enum class Strategy {
    YIELD, RATING, RATING_YIELD_64, RATING_YIELD_73
}

data class CalculationInput(
    val strategy: Strategy,
    val stocks: Set<Stock>
)

data class Slice(
    val ticker: String,
    val weight: Double
)