package com.chetan.snoutscout.feature.call

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chetan.snoutscout.data.model.CallState
import com.chetan.snoutscout.data.model.CallType
import com.chetan.snoutscout.data.repository.CallRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CallViewModel(
    private val callRepository: CallRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CallUiState())
    val uiState: StateFlow<CallUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            callRepository.callState.collect { state ->
                _uiState.value = _uiState.value.copy(callState = state)
            }
        }
        viewModelScope.launch {
            callRepository.elapsedSeconds.collect { elapsed ->
                _uiState.value = _uiState.value.copy(elapsedSeconds = elapsed)
            }
        }
        viewModelScope.launch {
            callRepository.deductedAmount.collect { deducted ->
                _uiState.value = _uiState.value.copy(deductedAmount = deducted)
            }
        }
        viewModelScope.launch {
            callRepository.lowBalanceWarningVisible.collect { visible ->
                _uiState.value = _uiState.value.copy(lowBalanceWarningVisible = visible)
            }
        }
    }

    fun startVoiceCall() {
        val current = _uiState.value.copy(
            callType = CallType.VOICE,
            isVideoEnabled = false,
            isEmergency = false
        )
        _uiState.value = current
        callRepository.startCall(
            callType = current.callType,
            pricePerMinuteInInr = current.pricePerMinuteInInr,
            startingBalance = current.startingBalance
        )
    }

    fun startVideoCall() {
        val current = _uiState.value.copy(
            callType = CallType.VIDEO,
            isVideoEnabled = true,
            isEmergency = false
        )
        _uiState.value = current
        callRepository.startCall(
            callType = current.callType,
            pricePerMinuteInInr = current.pricePerMinuteInInr,
            startingBalance = current.startingBalance
        )
    }

    fun startEmergencyCall() {
        val emergencyRate = 45
        val current = _uiState.value.copy(
            callType = CallType.EMERGENCY,
            pricePerMinuteInInr = emergencyRate,
            isVideoEnabled = false,
            isEmergency = true
        )
        _uiState.value = current
        callRepository.startCall(
            callType = current.callType,
            pricePerMinuteInInr = current.pricePerMinuteInInr,
            startingBalance = current.startingBalance
        )
    }

    fun toggleMute() {
        _uiState.value = _uiState.value.copy(isMuted = !_uiState.value.isMuted)
    }

    fun toggleSpeaker() {
        _uiState.value = _uiState.value.copy(isSpeakerOn = !_uiState.value.isSpeakerOn)
    }

    fun toggleVideo() {
        _uiState.value = _uiState.value.copy(isVideoEnabled = !_uiState.value.isVideoEnabled)
    }

    fun pauseOrResume() {
        when (_uiState.value.callState) {
            CallState.ACTIVE -> callRepository.pauseCall()
            CallState.PAUSED -> callRepository.resumeCall()
            else -> Unit
        }
    }

    fun endCall() {
        callRepository.endCall()
    }

    fun dismissLowBalanceWarning() {
        callRepository.dismissLowBalanceWarning()
    }

    companion object {
        fun factory(callRepository: CallRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CallViewModel(callRepository) as T
                }
            }
    }
}