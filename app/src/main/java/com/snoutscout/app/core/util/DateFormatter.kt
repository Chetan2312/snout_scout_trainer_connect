package com.snoutscout.app.core.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateFormatter {
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

    fun formatDate(epochMillis: Long): String = dateFormat.format(Date(epochMillis))
    fun formatDateTime(epochMillis: Long): String = dateTimeFormat.format(Date(epochMillis))

    fun timeAgo(epochMillis: Long): String {
        val diff = System.currentTimeMillis() - epochMillis
        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> "Just now"
            diff < TimeUnit.HOURS.toMillis(1) -> "${diff / TimeUnit.MINUTES.toMillis(1)}m ago"
            diff < TimeUnit.DAYS.toMillis(1) -> "${diff / TimeUnit.HOURS.toMillis(1)}h ago"
            diff < TimeUnit.DAYS.toMillis(7) -> "${diff / TimeUnit.DAYS.toMillis(1)}d ago"
            else -> formatDate(epochMillis)
        }
    }

    fun formatElapsed(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return "%02d:%02d".format(m, s)
    }
}
