package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.model.CallState
import com.chetan.snoutscout.data.model.CallType
import kotlinx.coroutines.flow.StateFlow

interface CallRepository {
    val callState: StateFlow<CallState>
    val elapsedSeconds: StateFlow<Int>
    val deductedAmount: StateFlow<Int>
    val lowBalanceWarningVisible: StateFlow<Boolean>

    fun startCall(callType: CallType, pricePerMinuteInInr: Int, startingBalance: Int)
    fun pauseCall()
    fun resumeCall()
    fun endCall()
    fun dismissLowBalanceWarning()
}