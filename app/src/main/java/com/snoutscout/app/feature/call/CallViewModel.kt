package com.snoutscout.app.feature.call

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.snoutscout.app.data.model.CallState
import com.snoutscout.app.data.model.CallType
import com.snoutscout.app.di.AppContainer
import com.snoutscout.app.service.CallService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.ceil

class CallViewModel(
    private val callService: CallService,
    private val walletBalance: Int,
    private val ratePerMin: Int
) : ViewModel() {

    val callState: StateFlow<CallState> = callService.getCallState()
        .stateIn(viewModelScope, SharingStarted.Eagerly, CallState.IDLE)

    private val _elapsed = MutableStateFlow(0)
    val elapsed = _elapsed.asStateFlow()

    private val _isMuted = MutableStateFlow(false)
    val isMuted = _isMuted.asStateFlow()

    private val _isSpeakerOn = MutableStateFlow(false)
    val isSpeakerOn = _isSpeakerOn.asStateFlow()

    private val _isVideoOn = MutableStateFlow(true)
    val isVideoOn = _isVideoOn.asStateFlow()

    private val _showLowBalanceWarning = MutableStateFlow(false)
    val showLowBalanceWarning = _showLowBalanceWarning.asStateFlow()

    private var timerJob: Job? = null
    private var lowBalanceShown = false
    private var callId: String? = null

    val costSoFar: StateFlow<Int> = _elapsed.map { e ->
        ceil(e / 60.0).toInt() * ratePerMin
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    fun initiateCall(trainerId: String, callType: CallType) = viewModelScope.launch {
        val result = callService.initiateCall(trainerId, callType)
        result.onSuccess { id ->
            callId = id
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (callState.value == CallState.ACTIVE) {
                delay(1000)
                if (callState.value == CallState.ACTIVE) {
                    _elapsed.value += 1
                    val cost = ceil(_elapsed.value / 60.0).toInt() * ratePerMin
                    val remaining = walletBalance - cost
                    val remainingMins = remaining / ratePerMin
                    if (remainingMins <= 2 && !lowBalanceShown) {
                        _showLowBalanceWarning.value = true
                        lowBalanceShown = true
                    }
                    if (remaining <= 0) endCall()
                }
            }
        }
    }

    fun endCall() = viewModelScope.launch {
        timerJob?.cancel()
        callId?.let { callService.endCall(it) }
    }

    fun toggleMute() { _isMuted.value = !_isMuted.value }
    fun toggleSpeaker() { _isSpeakerOn.value = !_isSpeakerOn.value }
    fun toggleVideo() { _isVideoOn.value = !_isVideoOn.value }
    fun dismissLowBalanceWarning() { _showLowBalanceWarning.value = false }

    fun togglePause() {
        // pause handled by state transitions in service
    }
}

class CallViewModelFactory(
    private val container: AppContainer,
    private val walletBalance: Int,
    private val ratePerMin: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CallViewModel(container.callService, walletBalance, ratePerMin) as T
    }
}
