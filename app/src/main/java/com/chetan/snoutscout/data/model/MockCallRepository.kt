package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.model.CallState
import com.chetan.snoutscout.data.model.CallType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

class MockCallRepository : CallRepository {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var timerJob: Job? = null
    private var activePricePerMinute = 0
    private var activeStartingBalance = 0

    private val _callState = MutableStateFlow(CallState.IDLE)
    override val callState: StateFlow<CallState> = _callState

    private val _elapsedSeconds = MutableStateFlow(0)
    override val elapsedSeconds: StateFlow<Int> = _elapsedSeconds

    private val _deductedAmount = MutableStateFlow(0)
    override val deductedAmount: StateFlow<Int> = _deductedAmount

    private val _lowBalanceWarningVisible = MutableStateFlow(false)
    override val lowBalanceWarningVisible: StateFlow<Boolean> = _lowBalanceWarningVisible

    override fun startCall(callType: CallType, pricePerMinuteInInr: Int, startingBalance: Int) {
        timerJob?.cancel()
        activePricePerMinute = pricePerMinuteInInr
        activeStartingBalance = startingBalance
        _elapsedSeconds.value = 0
        _deductedAmount.value = 0
        _lowBalanceWarningVisible.value = false
        _callState.value = CallState.CONNECTING

        scope.launch {
            delay(1200)
            _callState.value = CallState.ACTIVE
            startTicker()
        }
    }

    override fun pauseCall() {
        if (_callState.value == CallState.ACTIVE) {
            _callState.value = CallState.PAUSED
            timerJob?.cancel()
        }
    }

    override fun resumeCall() {
        if (_callState.value == CallState.PAUSED) {
            _callState.value = CallState.ACTIVE
            startTicker()
        }
    }

    override fun endCall() {
        timerJob?.cancel()
        _callState.value = CallState.ENDED
    }

    override fun dismissLowBalanceWarning() {
        _lowBalanceWarningVisible.value = false
    }

    private fun startTicker() {
        timerJob?.cancel()
        timerJob = scope.launch {
            while (_callState.value == CallState.ACTIVE) {
                delay(1000)
                val nextSeconds = _elapsedSeconds.value + 1
                _elapsedSeconds.value = nextSeconds

                val billedMinutes = max(1, kotlin.math.ceil(nextSeconds / 60.0).toInt())
                val deducted = billedMinutes * activePricePerMinute
                _deductedAmount.value = deducted

                val remainingBalance = activeStartingBalance - deducted
                if (remainingBalance <= activePricePerMinute * 2) {
                    _lowBalanceWarningVisible.value = true
                }
                if (remainingBalance <= 0) {
                    _callState.value = CallState.ENDED
                    timerJob?.cancel()
                }
            }
        }
    }
}