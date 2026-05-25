package com.snoutscout.app.data.model

data class User(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val city: String,
    val avatarUrl: String? = null,
    val role: UserRole = UserRole.CLIENT
)

enum class UserRole { CLIENT, TRAINER }

data class TrainerProfile(
    val id: String,
    val name: String,
    val city: String,
    val rating: Float,
    val reviewCount: Int,
    val ratePerMin: Int,
    val experience: Int,
    val specializations: List<String>,
    val languages: List<String>,
    val breeds: List<String>,
    val bio: String,
    val isVerified: Boolean,
    val isOnline: Boolean,
    val isFeatured: Boolean,
    val certifications: List<String>,
    val totalSessions: Int,
    val responseTime: String
)

data class DogProfile(
    val id: String,
    val name: String,
    val breed: String,
    val age: String,
    val gender: String,
    val weight: String,
    val vaccination: String,
    val lastVaccination: String,
    val issues: List<String>,
    val medicalHistory: String,
    val previousSessions: Int,
    val imageUri: String? = null
)

data class WalletTransaction(
    val id: String,
    val type: TransactionType,
    val amount: Int,
    val method: String? = null,
    val date: Long,
    val label: String,
    val sessionId: String? = null
)

enum class TransactionType { RECHARGE, DEDUCTION }

data class ConsultationSession(
    val id: String,
    val trainerId: String,
    val trainerName: String,
    val dogId: String,
    val dogName: String,
    val type: CallType,
    val durationMinutes: Int,
    val cost: Int,
    val date: Long,
    val status: SessionStatus,
    val rating: Int? = null,
    val hasReport: Boolean = false,
    val hasChat: Boolean = false,
    val summary: String
)

enum class CallType { VOICE, VIDEO }
enum class SessionStatus { SCHEDULED, ACTIVE, COMPLETED, CANCELLED }

enum class CallState { IDLE, CONNECTING, ACTIVE, PAUSED, ENDED, FAILED }

data class SessionReport(
    val id: String,
    val sessionId: String,
    val trainerName: String,
    val dogName: String,
    val date: String,
    val status: ReportStatus,
    val issueDiscussed: String,
    val observations: String,
    val solutions: String,
    val routine: String,
    val instructions: String,
    val followUp: String
)

enum class ReportStatus { DRAFT, PENDING_REVIEW, APPROVED }

data class TrainerReview(
    val id: String,
    val trainerId: String,
    val userName: String,
    val rating: Int,
    val date: String,
    val text: String
)

data class RechargePack(
    val id: String,
    val minutes: Int,
    val price: Int,
    val label: String,
    val isPopular: Boolean = false,
    val isBestValue: Boolean = false
)

data class AvailabilitySlot(
    val dayOfWeek: String,
    val slots: List<TimeSlot>
)

data class TimeSlot(
    val start: String,
    val end: String
)

data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val title: String,
    val body: String,
    val date: Long,
    val isRead: Boolean = false
)

enum class NotificationType { REPORT, REMINDER, PROMO, SESSION }

data class PayoutRequest(
    val id: String,
    val amount: Int,
    val date: String,
    val status: String,
    val method: String
)

data class UpcomingCall(
    val id: String,
    val clientName: String,
    val dogName: String,
    val breed: String,
    val type: CallType,
    val scheduledAt: Long,
    val durationMinutes: Int,
    val issue: String
)

data class AppState(
    val isLoggedIn: Boolean = false,
    val currentRole: UserRole = UserRole.CLIENT,
    val walletBalance: Int = 450,
    val currentUser: User = User("u1", "Aarav Sharma", "+91 98765 43210", "aarav@email.com", "Pune")
)
