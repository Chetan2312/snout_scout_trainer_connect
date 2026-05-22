package com.chetan.snoutscout.feature.call

import com.chetan.snoutscout.data.model.CallState
import com.chetan.snoutscout.data.model.CallType

data class CallUiState(
    val callType: CallType = CallType.VOICE,
    val trainerName: String = "Aarav Kulkarni",
    val dogName: String = "Bruno",
    val pricePerMinuteInInr: Int = 25,
    val startingBalance: Int = 1200,
    val callState: CallState = CallState.IDLE,
    val elapsedSeconds: Int = 0,
    val deductedAmount: Int = 0,
    val lowBalanceWarningVisible: Boolean = false,
    val isMuted: Boolean = false,
    val isSpeakerOn: Boolean = true,
    val isVideoEnabled: Boolean = false,
    val isEmergency: Boolean = false
) {
    val remainingBalance: Int
        get() = startingBalance - deductedAmount
}