package com.chetan.snoutscout.feature.call

fun formatElapsed(seconds: Int): String {
    val minutes = seconds / 60
    val remainder = seconds % 60
    return "%02d:%02d".format(minutes, remainder)
}