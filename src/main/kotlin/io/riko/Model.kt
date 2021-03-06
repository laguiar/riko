package io.riko

data class Stock(
    val ticker: String,
    val rating: Int,
    val dividendYield: Double
)

data class StockSummary(
    val ticker: String
)

enum class Strategy {
    YIELD,
    RATING,
    RATING_YIELD_64,
    RATING_YIELD_73,
    RATING_YIELD_82,
    YIELD_RATING_64,
    YIELD_RATING_73,
    YIELD_RATING_82
}

data class CalculationInput(
    val strategy: Strategy,
    val stocks: Set<Stock>
)

data class Slice(
    val ticker: String,
    val weight: Double
)