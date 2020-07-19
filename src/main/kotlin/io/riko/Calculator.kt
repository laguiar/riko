package io.riko

import io.riko.Strategy.*
import java.math.RoundingMode

private val DEFAULT_WEIGHT_FACTOR = 0.65 to 0.35
private const val MINIMAL_WEIGHT = 0.50

fun calculatePie(input: CalculationInput): List<Slice> =
    when (input.strategy) {
        YIELD -> calculateByDividendYield(input.stocks)
        RATING -> calculateByRating(input.stocks)
        else -> calculateByRatingAndYield(input.stocks, input.strategy)
    }


private fun calculateByDividendYield(stocks: Set<Stock>): List<Slice> {
    val sum = stocks.sumByDouble { it.dividendYield }

    return stocks.map { stock ->
        val weight = stock.dividendYield.div(sum) * 100
        buildSlice(stock.ticker, weight)

    }.sortedByDescending { it.weight }
}


private fun calculateByRating(stocks: Set<Stock>): List<Slice> {
    val sum = stocks.sumByDouble { it.rating.toDouble() }

    return stocks.map { stock ->
        val weight = stock.rating.div(sum) * 100
        buildSlice(stock.ticker, weight)

    }.sortedByDescending { it.weight }
}


private fun calculateByRatingAndYield(stocks: Set<Stock>, strategy: Strategy): List<Slice> {
    val (ratingFactor, yieldFactor) = when (strategy) {
        RATING_YIELD_64 -> 0.60 to 0.40
        RATING_YIELD_73 -> 0.70 to 0.30
        RATING_YIELD_82 -> 0.80 to 0.20
        YIELD_RATING_64 -> 0.40 to 0.60
        YIELD_RATING_73 -> 0.30 to 0.70
        YIELD_RATING_82 -> 0.20 to 0.80
        else -> DEFAULT_WEIGHT_FACTOR
    }

    val sumRating = stocks.sumByDouble { it.rating.toDouble() }
    val sumYield = stocks.sumByDouble { it.dividendYield }

    return stocks.map { stock ->
        val ratingWeight = stock.rating.div(sumRating) * ratingFactor
        val yieldWeight = stock.dividendYield.div(sumYield) * yieldFactor
        val weight = (ratingWeight + yieldWeight) * 100
        buildSlice(stock.ticker, weight)

    }.sortedByDescending { it.weight }
}


private fun buildSlice(ticker: String, weight: Double): Slice =
    Slice(ticker, weight.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble())
