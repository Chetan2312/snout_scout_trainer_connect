package com.chetan.snoutscout.domain.model

data class RechargePack(
    val id: String,
    val title: String,
    val amountInInr: Int,
    val bonusText: String? = null
)