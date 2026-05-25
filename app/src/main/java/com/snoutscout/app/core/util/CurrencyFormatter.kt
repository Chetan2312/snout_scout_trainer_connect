package com.snoutscout.app.core.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    private val indianFormat = NumberFormat.getNumberInstance(Locale("en", "IN"))

    fun format(amount: Int): String = "₹${indianFormat.format(amount)}"
    fun format(amount: Long): String = "₹${indianFormat.format(amount)}"
}
