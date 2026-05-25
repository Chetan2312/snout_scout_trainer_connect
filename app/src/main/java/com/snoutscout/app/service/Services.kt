package com.snoutscout.app.service

import com.snoutscout.app.data.model.CallState
import com.snoutscout.app.data.model.CallType
import com.snoutscout.app.data.model.SessionReport
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface CallService {
    suspend fun initiateCall(trainerId: String, callType: CallType): Result<String>
    suspend fun endCall(callId: String): Result<Unit>
    fun getCallState(): StateFlow<CallState>
}

interface PaymentGateway {
    suspend fun initiatePayment(amount: Int, method: String): Result<String>
    suspend fun verifyPayment(transactionId: String): Result<Boolean>
}

interface AISummarizer {
    suspend fun generateReport(transcript: String, trainerNotes: String): Result<SessionReport>
}

interface TranscriptionService {
    suspend fun transcribe(audioUrl: String): Result<String>
}

class MockCallService : CallService {
    private val _callState = MutableStateFlow(CallState.IDLE)

    override suspend fun initiateCall(trainerId: String, callType: CallType): Result<String> {
        _callState.value = CallState.CONNECTING
        delay(2000)
        _callState.value = CallState.ACTIVE
        return Result.success("call_${System.currentTimeMillis()}")
    }

    override suspend fun endCall(callId: String): Result<Unit> {
        _callState.value = CallState.ENDED
        return Result.success(Unit)
    }

    override fun getCallState(): StateFlow<CallState> = _callState
}

class MockPaymentGateway : PaymentGateway {
    override suspend fun initiatePayment(amount: Int, method: String): Result<String> {
        delay(800)
        return Result.success("txn_${System.currentTimeMillis()}")
    }

    override suspend fun verifyPayment(transactionId: String): Result<Boolean> {
        delay(300)
        return Result.success(true)
    }
}

class MockAISummarizer : AISummarizer {
    override suspend fun generateReport(transcript: String, trainerNotes: String): Result<SessionReport> {
        delay(500)
        return Result.success(
            SessionReport(
                id = "r_${System.currentTimeMillis()}",
                sessionId = "",
                trainerName = "",
                dogName = "",
                date = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
                status = com.snoutscout.app.data.model.ReportStatus.DRAFT,
                issueDiscussed = "As discussed during the session.",
                observations = "Dog shows improvement with positive reinforcement techniques.",
                solutions = "1. Continue daily training sessions\n2. Use high-value rewards\n3. Keep sessions short (5-10 min)",
                routine = "Morning: Obedience exercises\nEvening: Play-based training",
                instructions = "Maintain consistency. End on a positive note always.",
                followUp = "Schedule follow-up in 2 weeks to assess progress."
            )
        )
    }
}

class MockTranscriptionService : TranscriptionService {
    override suspend fun transcribe(audioUrl: String): Result<String> {
        delay(600)
        return Result.success("Mock transcription of the consultation session.")
    }
}
