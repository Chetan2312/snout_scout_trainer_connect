# SNOUT SCOUT TRAINER CONNECT — Developer Handoff Document
## Complete Android App Implementation Specification

> **For Claude Code / AI-assisted development in VS Code**
> This document contains EVERYTHING needed to implement the full native Android app.
> Feed this entire document to Claude Sonnet in VS Code and instruct it to implement the app step by step.

---

## TABLE OF CONTENTS
1. [Project Overview](#1-project-overview)
2. [Tech Stack & Dependencies](#2-tech-stack--dependencies)
3. [Project Structure](#3-project-structure)
4. [Design System & Theme](#4-design-system--theme)
5. [Data Models](#5-data-models)
6. [Room Database](#6-room-database)
7. [Repository Layer & Mock Services](#7-repository-layer--mock-services)
8. [Navigation Graph](#8-navigation-graph)
9. [Screen Specifications (All 33 Screens)](#9-screen-specifications)
10. [State Management & ViewModels](#10-state-management--viewmodels)
11. [Call System State Machine](#11-call-system-state-machine)
12. [Wallet & Billing Logic](#12-wallet--billing-logic)
13. [AI Report System](#13-ai-report-system)
14. [Mock Data (India-Specific)](#14-mock-data)
15. [GitHub Actions Workflow](#15-github-actions-workflow)
16. [Build & Run Instructions](#16-build--run-instructions)
17. [Implementation Order](#17-implementation-order)
18. [Verification Checklist](#18-verification-checklist)

---

## 1. PROJECT OVERVIEW

**App Name:** Snout Scout Trainer Connect
**Short Name:** Snout Scout
**Tagline:** "Trusted dog trainers, one call away"
**Package Name:** `com.snoutscout.app`
**Min SDK:** 26 (Android 8.0)
**Target SDK:** 35
**Compile SDK:** 35

**What it does:** Premium marketplace connecting Indian dog owners with verified professional dog trainers through prepaid voice/video consultations. Think "Urban Company meets Practo for Dog Training."

**Two user roles in one app:**
- **Client** — Dog owners who browse trainers, book calls, manage dogs, recharge wallet
- **Trainer** — Dog trainers who manage availability, take calls, write consultation notes, track earnings

**Role switching:** Users can switch between Client and Trainer mode from Profile/Settings screen.

---

## 2. TECH STACK & DEPENDENCIES

### build.gradle.kts (Project level)
```kotlin
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
}
```

### build.gradle.kts (App level)
```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.snoutscout.app"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.snoutscout.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }
    buildFeatures { compose = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    // Compose BOM
    val composeBom = platform("androidx.compose:compose-bom:2024.12.01")
    implementation(composeBom)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Core
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.5")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Coil (image loading)
    implementation("io.coil-kt:coil-compose:2.7.0")

    // DataStore (preferences)
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // Serialization (for JSON)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}
```

### gradle/libs.versions.toml
Use the versions above. The project should use Kotlin 2.1.0+ with the Compose compiler plugin (NOT the old `kotlinCompilerExtensionVersion` approach).

---

## 3. PROJECT STRUCTURE

```
app/src/main/java/com/snoutscout/app/
├── SnoutScoutApp.kt                    # Application class
├── MainActivity.kt                      # Single Activity
├── navigation/
│   ├── NavGraph.kt                      # Navigation graph setup
│   ├── Routes.kt                        # Sealed class/object routes
│   └── BottomNavItems.kt               # Bottom nav definitions
├── core/
│   ├── theme/
│   │   ├── Theme.kt                     # Material 3 theme (light + dark)
│   │   ├── Color.kt                     # Color definitions
│   │   ├── Type.kt                      # Typography
│   │   └── Shape.kt                     # Shape definitions
│   ├── ui/
│   │   ├── SSCard.kt                    # Reusable card component
│   │   ├── SSButton.kt                  # Button variants
│   │   ├── SSChip.kt                    # Filter chip
│   │   ├── SSAvatar.kt                  # Avatar with initials
│   │   ├── SSRating.kt                  # Star rating display
│   │   ├── SSBadge.kt                   # Status badge
│   │   ├── SSInput.kt                   # Text input field
│   │   ├── SSBottomSheet.kt             # Bottom sheet wrapper
│   │   ├── SSDialog.kt                  # Dialog component
│   │   ├── SSEmptyState.kt              # Empty state placeholder
│   │   ├── SSSectionHeader.kt           # Section header with action
│   │   ├── SSTrainerCard.kt             # Trainer list item card
│   │   └── SSTopBar.kt                  # Top app bar
│   └── util/
│       ├── CurrencyFormatter.kt         # INR formatting
│       ├── DateFormatter.kt             # Date/time formatting
│       └── Extensions.kt                # Kotlin extensions
├── data/
│   ├── model/
│   │   ├── User.kt
│   │   ├── ClientProfile.kt
│   │   ├── TrainerProfile.kt
│   │   ├── DogProfile.kt
│   │   ├── TrainerSpecialization.kt     # Enum
│   │   ├── Language.kt                  # Enum
│   │   ├── WalletTransaction.kt
│   │   ├── ConsultationSession.kt
│   │   ├── CallType.kt                  # Enum: VOICE, VIDEO
│   │   ├── CallState.kt                 # Enum: IDLE, CONNECTING, ACTIVE, PAUSED, ENDED, FAILED
│   │   ├── SessionReport.kt
│   │   ├── TrainerReview.kt
│   │   ├── AvailabilitySlot.kt
│   │   ├── PayoutRequest.kt
│   │   ├── VerificationStatus.kt        # Enum
│   │   ├── ConsentRecord.kt
│   │   ├── NotificationItem.kt
│   │   └── RechargePack.kt
│   ├── local/
│   │   ├── AppDatabase.kt              # Room database
│   │   ├── dao/
│   │   │   ├── DogProfileDao.kt
│   │   │   ├── WalletTransactionDao.kt
│   │   │   ├── SessionDao.kt
│   │   │   ├── ReportDao.kt
│   │   │   ├── NotificationDao.kt
│   │   │   └── TrainerDao.kt
│   │   ├── entity/                      # Room entities (mirror data models)
│   │   │   ├── DogProfileEntity.kt
│   │   │   ├── WalletTransactionEntity.kt
│   │   │   ├── SessionEntity.kt
│   │   │   ├── ReportEntity.kt
│   │   │   ├── NotificationEntity.kt
│   │   │   └── TrainerEntity.kt
│   │   └── converter/
│   │       └── Converters.kt           # Type converters for Room
│   └── repository/
│       ├── AuthRepository.kt
│       ├── TrainerRepository.kt
│       ├── DogRepository.kt
│       ├── WalletRepository.kt
│       ├── SessionRepository.kt
│       ├── ReportRepository.kt
│       ├── NotificationRepository.kt
│       └── CallRepository.kt
├── domain/
│   ├── model/                           # Domain models (if different from data)
│   └── usecase/
│       ├── GetTrainersUseCase.kt
│       ├── FilterTrainersUseCase.kt
│       ├── RechargeWalletUseCase.kt
│       ├── StartCallUseCase.kt
│       ├── EndCallUseCase.kt
│       ├── GenerateReportUseCase.kt
│       └── SwitchRoleUseCase.kt
├── di/
│   └── AppContainer.kt                 # Manual DI container
├── feature/
│   ├── auth/
│   │   ├── SplashScreen.kt
│   │   ├── OnboardingScreen.kt
│   │   └── LoginScreen.kt
│   ├── client_home/
│   │   ├── HomeScreen.kt
│   │   └── HomeViewModel.kt
│   ├── trainer_browse/
│   │   ├── BrowseTrainersScreen.kt
│   │   ├── BrowseTrainersViewModel.kt
│   │   └── FilterBottomSheet.kt
│   ├── trainer_profile/
│   │   ├── TrainerProfileScreen.kt
│   │   └── TrainerProfileViewModel.kt
│   ├── dog_profile/
│   │   ├── DogsListScreen.kt
│   │   ├── DogFormScreen.kt
│   │   └── DogViewModel.kt
│   ├── wallet/
│   │   ├── WalletScreen.kt
│   │   └── WalletViewModel.kt
│   ├── call/
│   │   ├── CallPreCheckScreen.kt
│   │   ├── ActiveCallScreen.kt
│   │   ├── PostCallScreen.kt
│   │   ├── ScheduleBookingScreen.kt
│   │   └── CallViewModel.kt
│   ├── reports/
│   │   ├── ReportsListScreen.kt
│   │   ├── ReportDetailScreen.kt
│   │   └── ReportsViewModel.kt
│   ├── history/
│   │   ├── SessionHistoryScreen.kt
│   │   ├── SessionDetailScreen.kt
│   │   └── HistoryViewModel.kt
│   ├── reviews/
│   │   └── ReviewsSection.kt            # Embedded in TrainerProfile
│   ├── chat/
│   │   └── ChatScreen.kt
│   ├── notifications/
│   │   └── NotificationsScreen.kt
│   ├── settings/
│   │   └── SettingsScreen.kt
│   ├── trainer_dashboard/
│   │   ├── TrainerDashboardScreen.kt
│   │   └── TrainerDashboardViewModel.kt
│   ├── trainer_profile_mgmt/
│   │   └── TrainerProfileMgmtScreen.kt
│   ├── trainer_availability/
│   │   └── TrainerAvailabilityScreen.kt
│   ├── trainer_notes/
│   │   ├── TrainerNotesScreen.kt
│   │   └── TrainerNotesViewModel.kt
│   ├── earnings/
│   │   ├── TrainerEarningsScreen.kt
│   │   └── TrainerWithdrawScreen.kt
│   └── trainer_upcoming/
│       └── TrainerUpcomingScreen.kt
└── service/
    ├── CallService.kt                   # Interface for future Agora/Twilio
    ├── MockCallService.kt               # Mock implementation
    ├── PaymentGateway.kt                # Interface for future Razorpay
    ├── MockPaymentGateway.kt            # Mock implementation
    ├── AISummarizer.kt                  # Interface for future LLM
    ├── MockAISummarizer.kt              # Mock implementation
    └── TranscriptionService.kt          # Interface for future Whisper

app/src/main/res/
├── drawable/
│   └── ic_launcher_foreground.xml       # App icon (paw + phone concept)
├── mipmap-*/                            # Launcher icons
├── values/
│   ├── strings.xml                      # ALL user-facing strings centralized
│   ├── colors.xml                       # Color resources
│   └── themes.xml                       # App theme
└── font/
    └── plus_jakarta_sans_*.ttf          # Font files (or use Google Fonts downloadable)
```

---

## 4. DESIGN SYSTEM & THEME

### Color Palette

```kotlin
// Color.kt
object SnoutScoutColors {
    // Light Theme
    val Background = Color(0xFFFAF7F2)
    val Surface = Color(0xFFFFFFFF)
    val SurfaceAlt = Color(0xFFF0E6D8)
    val SurfaceDim = Color(0xFFEDE3D5)

    val Primary = Color(0xFF5C6B4F)         // Olive green
    val PrimaryLight = Color(0xFF7A8B6A)
    val PrimaryDark = Color(0xFF4A5D3E)
    val OnPrimary = Color(0xFFFFFFFF)

    val Secondary = Color(0xFFA67C52)        // Warm brown
    val SecondaryLight = Color(0xFFC4956A)
    val SecondaryDark = Color(0xFF8B6540)
    val OnSecondary = Color(0xFFFFFFFF)

    val Accent = Color(0xFFD4A54A)           // Gold accent

    val Text = Color(0xFF2A2520)
    val TextSecondary = Color(0xFF6D635A)
    val TextTertiary = Color(0xFF9E9489)

    val Border = Color(0xFFE8DDD0)
    val BorderLight = Color(0xFFF0E6D8)

    val Error = Color(0xFFC75050)
    val Success = Color(0xFF5A8A5E)
    val Warning = Color(0xFFD4A54A)

    // Dark Theme
    val DarkBackground = Color(0xFF1A1816)
    val DarkSurface = Color(0xFF252220)
    val DarkSurfaceAlt = Color(0xFF302C28)
    val DarkBorder = Color(0xFF3D3832)
    val DarkText = Color(0xFFF0E6D8)
    val DarkTextSecondary = Color(0xFFA89E94)
}
```

### Typography

```kotlin
// Type.kt — Use "Plus Jakarta Sans" Google Font
// Weights: 400 (Regular), 500 (Medium), 600 (SemiBold), 700 (Bold), 800 (ExtraBold)

val SnoutScoutTypography = Typography(
    displayLarge = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.W800, fontSize = 32.sp, letterSpacing = (-0.5).sp),
    headlineLarge = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.W800, fontSize = 28.sp, letterSpacing = (-0.3).sp),
    headlineMedium = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.W700, fontSize = 22.sp, letterSpacing = (-0.3).sp),
    headlineSmall = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.W700, fontSize = 18.sp),
    titleLarge = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.W700, fontSize = 16.sp),
    titleMedium = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.W600, fontSize = 15.sp),
    titleSmall = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.W600, fontSize = 14.sp),
    bodyLarge = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.W400, fontSize = 15.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.W400, fontSize = 14.sp, lineHeight = 22.sp),
    bodySmall = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.W400, fontSize = 13.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.W600, fontSize = 14.sp),
    labelMedium = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.W600, fontSize = 13.sp),
    labelSmall = TextStyle(fontFamily = PlusJakartaSans, fontWeight = FontWeight.W600, fontSize = 11.sp)
)
```

### Shape System

```kotlin
val SnoutScoutShapes = Shapes(
    small = RoundedCornerShape(8.dp),    // sm
    medium = RoundedCornerShape(12.dp),  // md
    large = RoundedCornerShape(16.dp),   // lg
    extraLarge = RoundedCornerShape(20.dp) // xl
)
// Full rounded (chips, pills): RoundedCornerShape(999.dp)
```

### Elevation / Shadow

```kotlin
// Card: tonalElevation = 1.dp, shadowElevation = 2.dp
// Elevated: tonalElevation = 2.dp, shadowElevation = 4.dp
// Bottom Sheet: tonalElevation = 3.dp, shadowElevation = 8.dp
```

### Spacing Scale
```
4dp, 6dp, 8dp, 10dp, 12dp, 14dp, 16dp, 20dp, 24dp, 32dp, 40dp, 48dp
```

---

## 5. DATA MODELS

### User
```kotlin
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
```

### TrainerProfile
```kotlin
data class TrainerProfile(
    val id: String,
    val name: String,
    val city: String,
    val rating: Float,          // 0.0 - 5.0
    val reviewCount: Int,
    val ratePerMin: Int,         // in INR
    val experience: Int,         // years
    val specializations: List<String>,
    val languages: List<String>,
    val breeds: List<String>,
    val bio: String,
    val isVerified: Boolean,
    val isOnline: Boolean,
    val isFeatured: Boolean,
    val certifications: List<String>,
    val totalSessions: Int,
    val responseTime: String     // e.g. "< 2 min"
)
```

### DogProfile
```kotlin
data class DogProfile(
    val id: String,
    val name: String,
    val breed: String,
    val age: String,            // e.g. "3 years"
    val gender: String,         // "Male" or "Female"
    val weight: String,         // e.g. "34 kg"
    val vaccination: String,    // "Up to date", "Overdue", "Not started"
    val lastVaccination: String,
    val issues: List<String>,   // behavioral issues
    val medicalHistory: String,
    val previousSessions: Int,
    val imageUri: String? = null
)
```

### WalletTransaction
```kotlin
data class WalletTransaction(
    val id: String,
    val type: TransactionType,  // RECHARGE or DEDUCTION
    val amount: Int,            // positive for recharge, negative for deduction
    val method: String? = null, // "UPI", "Card", etc.
    val date: Long,             // epoch millis
    val label: String,          // description
    val sessionId: String? = null
)

enum class TransactionType { RECHARGE, DEDUCTION }
```

### ConsultationSession
```kotlin
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
```

### CallState (State Machine)
```kotlin
enum class CallState {
    IDLE,
    CONNECTING,
    ACTIVE,
    PAUSED,
    ENDED,
    FAILED
}
```

### SessionReport
```kotlin
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
```

### TrainerReview
```kotlin
data class TrainerReview(
    val id: String,
    val trainerId: String,
    val userName: String,
    val rating: Int,
    val date: String,
    val text: String
)
```

### RechargePack
```kotlin
data class RechargePack(
    val id: String,
    val minutes: Int,
    val price: Int,
    val label: String,
    val isPopular: Boolean = false,
    val isBestValue: Boolean = false
)
```

### AvailabilitySlot
```kotlin
data class AvailabilitySlot(
    val dayOfWeek: String,       // "monday", "tuesday", etc.
    val slots: List<TimeSlot>
)

data class TimeSlot(
    val start: String,           // "09:00"
    val end: String              // "12:00"
)
```

### NotificationItem
```kotlin
data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val title: String,
    val body: String,
    val date: Long,
    val isRead: Boolean = false
)

enum class NotificationType { REPORT, REMINDER, PROMO, SESSION }
```

### PayoutRequest
```kotlin
data class PayoutRequest(
    val id: String,
    val amount: Int,
    val date: String,
    val status: String,     // "completed", "pending", "failed"
    val method: String      // "Bank Transfer", "UPI"
)
```

### UpcomingCall (Trainer side)
```kotlin
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
```

---

## 6. ROOM DATABASE

### AppDatabase
```kotlin
@Database(
    entities = [
        DogProfileEntity::class,
        WalletTransactionEntity::class,
        SessionEntity::class,
        ReportEntity::class,
        NotificationEntity::class,
        TrainerEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dogProfileDao(): DogProfileDao
    abstract fun walletTransactionDao(): WalletTransactionDao
    abstract fun sessionDao(): SessionDao
    abstract fun reportDao(): ReportDao
    abstract fun notificationDao(): NotificationDao
    abstract fun trainerDao(): TrainerDao
}
```

### Type Converters
```kotlin
class Converters {
    @TypeConverter fun fromStringList(value: List<String>): String = value.joinToString("|||")
    @TypeConverter fun toStringList(value: String): List<String> = if (value.isEmpty()) emptyList() else value.split("|||")
}
```

### Example DAO
```kotlin
@Dao
interface DogProfileDao {
    @Query("SELECT * FROM dog_profiles ORDER BY name ASC")
    fun getAllDogs(): Flow<List<DogProfileEntity>>

    @Query("SELECT * FROM dog_profiles WHERE id = :id")
    suspend fun getDogById(id: String): DogProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDog(dog: DogProfileEntity)

    @Delete
    suspend fun deleteDog(dog: DogProfileEntity)
}
```

Prepopulate the database on first run with the mock data from Section 14.

---

## 7. REPOSITORY LAYER & MOCK SERVICES

### Service Interfaces (for future backend integration)

```kotlin
// These interfaces allow swapping mock implementations for real ones later

interface CallService {
    suspend fun initiateCall(trainerId: String, callType: CallType): Result<String> // returns callId
    suspend fun endCall(callId: String): Result<Unit>
    fun getCallState(): StateFlow<CallState>
}

interface PaymentGateway {
    suspend fun initiatePayment(amount: Int, method: String): Result<String> // returns transactionId
    suspend fun verifyPayment(transactionId: String): Result<Boolean>
}

interface AISummarizer {
    suspend fun generateReport(transcript: String, trainerNotes: String): Result<SessionReport>
}

interface TranscriptionService {
    suspend fun transcribe(audioUrl: String): Result<String>
}
```

### Mock Implementations
Each mock service should simulate realistic delays (300-800ms) using `delay()` and return success with sample data. The MockCallService should use a `MutableStateFlow<CallState>` that transitions through IDLE → CONNECTING (2s) → ACTIVE.

### Repository Pattern
Each repository wraps a DAO + optional mock service. Example:

```kotlin
class WalletRepository(
    private val walletDao: WalletTransactionDao,
    private val paymentGateway: PaymentGateway
) {
    fun getBalance(): Flow<Int> = walletDao.getAllTransactions().map { txList ->
        txList.sumOf { it.amount }
    }
    fun getTransactions(): Flow<List<WalletTransaction>> = walletDao.getAllTransactions().map { ... }
    suspend fun recharge(amount: Int, method: String): Result<Unit> { ... }
    suspend fun deduct(amount: Int, label: String, sessionId: String?): Result<Unit> { ... }
}
```

---

## 8. NAVIGATION GRAPH

### Routes (Sealed class)
```kotlin
sealed class Route(val route: String) {
    // Auth
    object Splash : Route("splash")
    object Onboarding : Route("onboarding")
    object Login : Route("login")

    // Client bottom nav tabs
    object ClientHome : Route("client_home")
    object BrowseTrainers : Route("browse_trainers")
    object DogsList : Route("dogs_list")
    object Wallet : Route("wallet")
    object Settings : Route("settings")

    // Client detail screens
    data class TrainerProfile(val trainerId: String) : Route("trainer_profile/{trainerId}")
    data class CallPreCheck(val trainerId: String, val callType: String) : Route("call_precheck/{trainerId}/{callType}")
    data class ActiveCall(val trainerId: String, val dogId: String, val callType: String) : Route("active_call/{trainerId}/{dogId}/{callType}")
    data class PostCall(val trainerId: String, val elapsed: Int, val cost: Int) : Route("post_call/{trainerId}/{elapsed}/{cost}")
    data class ScheduleBooking(val trainerId: String) : Route("schedule_booking/{trainerId}")
    object AddDog : Route("add_dog")
    data class EditDog(val dogId: String) : Route("edit_dog/{dogId}")
    object SessionHistory : Route("session_history")
    data class SessionDetail(val sessionId: String) : Route("session_detail/{sessionId}")
    object ReportsList : Route("reports_list")
    data class ReportDetail(val reportId: String) : Route("report_detail/{reportId}")
    data class Chat(val sessionId: String) : Route("chat/{sessionId}")
    object Notifications : Route("notifications")

    // Trainer bottom nav tabs
    object TrainerDashboard : Route("trainer_dashboard")
    object TrainerUpcoming : Route("trainer_upcoming")
    object TrainerNotes : Route("trainer_notes")
    object TrainerEarnings : Route("trainer_earnings")
    object TrainerProfileMgmt : Route("trainer_profile_mgmt")

    // Trainer detail screens
    object TrainerAvailability : Route("trainer_availability")
    object TrainerWithdraw : Route("trainer_withdraw")
}
```

### Bottom Navigation
```kotlin
// Client tabs
val clientTabs = listOf(
    BottomNavItem("Home", Icons.Outlined.Home, Route.ClientHome),
    BottomNavItem("Browse", Icons.Outlined.Search, Route.BrowseTrainers),
    BottomNavItem("My Dogs", Icons.Outlined.Pets, Route.DogsList),     // Use Pets icon
    BottomNavItem("Wallet", Icons.Outlined.AccountBalanceWallet, Route.Wallet),
    BottomNavItem("Profile", Icons.Outlined.Person, Route.Settings)
)

// Trainer tabs
val trainerTabs = listOf(
    BottomNavItem("Dashboard", Icons.Outlined.Dashboard, Route.TrainerDashboard),
    BottomNavItem("Calls", Icons.Outlined.Phone, Route.TrainerUpcoming),
    BottomNavItem("Notes", Icons.Outlined.Description, Route.TrainerNotes),
    BottomNavItem("Earnings", Icons.Outlined.AttachMoney, Route.TrainerEarnings),
    BottomNavItem("Profile", Icons.Outlined.Person, Route.TrainerProfileMgmt)
)
```

### Navigation behavior
- Bottom nav screens: no back stack (replace)
- Detail screens: push onto back stack
- Call screens: hide bottom nav entirely
- Auth screens: no bottom nav, no back navigation to app screens

---

## 9. SCREEN SPECIFICATIONS

### SCREEN 1: Splash Screen
- **Duration:** 2.2 seconds, then auto-navigate to Onboarding
- **Layout:** Full screen, gradient background (Primary → PrimaryDark, 160°)
- **Content:** App logo (assets/logo.jpeg) — 120dp circular with shadow, app name "Snout Scout" in white 28sp ExtraBold, tagline "Trusted dog trainers, one call away" in white/70% opacity 13sp
- **Background decoration:** Subtle scattered paw prints at 6% opacity

### SCREEN 2: Onboarding (3 slides)
- **Slide 1:** Logo image, "Find Expert Trainers", "Browse verified dog trainers across India — filtered by specialization, language, and city."
- **Slide 2:** Video icon (in Primary/14% bg circle 100dp), "Connect Instantly", "Start a voice or video consultation in under 2 minutes. Pay only for the time you use."
- **Slide 3:** File icon, "Get Detailed Reports", "Receive AI-powered session reports with training plans, routines, and follow-up advice."
- **Navigation:** Dot indicators (active=24dp wide Primary, inactive=8dp Border), Back/Next buttons, final slide shows "Get Started"

### SCREEN 3: Login / Register
- **Logo at top** (72dp circular)
- **Two modes:** Login ("Welcome back") and Register ("Create account") — toggle at bottom
- **Phone input:** +91 prefix, 10-digit input field
- **Register mode adds:** Full name field above phone
- **OTP step:** 4 separate digit inputs (52×56dp each), border changes to Primary when filled
- **Auto-advance focus** to next input on digit entry
- **"Change number" link** to go back

### SCREEN 4: Client Home Dashboard
- **Header:** Logo (40dp circular) + greeting ("Good afternoon, Aarav 👋") + notification bell with unread dot
- **Quick actions row:** 4 buttons in a row — Instant Call, Schedule, My Dogs, Wallet — each with icon in colored bg circle + label
- **Wallet balance card:** Gradient (Primary → PrimaryDark), shows balance in ₹, approximate minutes at avg rate, "+ Recharge" button
- **Recent Session section:** Card with trainer avatar, name, dog name, date, "Report ready" badge, summary text
- **Top-Rated Trainers:** List of SSTrainerCard components (avatar with initials, name, verified badge, online dot, city, specializations, rating, reviews count, rate/min, chevron right)
- **Coming Soon:** 2×2 grid of locked feature cards (AI Behavior Assistant, Group Consultations, Training Courses, Vet Integration) — each with icon, lock icon, label, "Coming soon" text, 70% opacity

### SCREEN 5: Browse Trainers
- **Top bar:** "Find a Trainer" + back arrow + filter icon button
- **Search bar:** Input with search icon, placeholder "Search by name, specialization..."
- **Sort chips:** Horizontal scroll — "Top Rated" (default), "Lowest Price", "Most Experienced" — active chip has Primary bg
- **Active filter chips:** Show active filters (e.g., "Online ✕") that can be dismissed
- **Results count:** "8 trainers found" in TextTertiary
- **Trainer list:** Vertical list of SSTrainerCard components
- **Empty state:** Search icon + "No trainers found" + "Try adjusting your filters"

### SCREEN 6: Filter Bottom Sheet
- **Drag handle** at top
- **Title:** "Filter Trainers"
- **City section:** Chip grid — All Cities, Pune, Mumbai, Bengaluru, Delhi, Hyderabad, Chennai, Kolkata, Jaipur, Ahmedabad
- **Specialization section:** Chip grid with "Any" + all specializations
- **Online toggle:** Label "Online now only" + Switch
- **Price slider:** "Max price: ₹XX/min" + Range slider (₹5–₹25)
- **Buttons:** "Reset" (secondary) + "Show N trainers" (primary)
- **All filters apply in real-time** to the trainer list

### SCREEN 7: Trainer Profile
- **Transparent top bar** with back arrow + share icon
- **Hero section:** Avatar (72dp), name + verified badge, city + experience, 3-column stats (Rating | Sessions | Rate/min with dividers)
- **Action buttons row:** "Call Now" (Primary, disabled if offline) + "Video Call" (Secondary/Accent) + "Schedule" (Outline)
- **Tab bar:** About | Reviews | Credentials — underline indicator on active tab
- **About tab:** Bio text, Specializations badges (Primary variant), Languages badges (Accent variant), Breed expertise chips
- **Reviews tab:** Overall rating with stars + count, list of review cards (avatar, name, stars, text, date)
- **Credentials tab:** Certifications list with Award icons, "Identity Verified" card with Shield icon

### SCREEN 8: Call Pre-Check
- **Trainer summary card:** Avatar, name, city, rate/min, online badge
- **Balance info card:** Your balance (left) + Estimated time (right) — red background variant if < 5 min, shows "Low balance — Recharge now" button
- **Dog selector:** Radio-style list — each dog shows emoji, name, breed, age, checkmark when selected
- **Consent checkbox:** Recording/AI notes consent text with custom checkbox
- **CTA button:** "Start Voice/Video Call" — full width, large, disabled until dog selected + consent checked

### SCREEN 9: Active Call Screen
- **Full screen**, gradient or dark background
- **Connecting state:** "Connecting..." text + spinner animation
- **Active state:** Trainer avatar (80dp), name, call type label, LARGE timer (42sp tabular-nums), cost ticker ("Cost: ₹XX"), remaining minutes, progress bar showing wallet consumption
- **Video call variant:** Dark bg with "You" pip in top-right corner (100×140dp rounded)
- **Controls row:** Mute toggle, Speaker toggle, Camera toggle (video only), Pause/Resume — circular 56dp buttons, white icons, toggle fills white on active
- **End Call button:** Full width, red, 52dp height with phone icon
- **Low Balance Dialog:** Title, remaining time info, "Continue" + "End Call" buttons

### SCREEN 10: Post-Call Summary
- **Success icon:** Green checkmark in circle (72dp)
- **"Session Complete"** heading + "Your consultation has ended"
- **Summary card:** Trainer avatar + name, dog name + breed, Duration | Total Cost | Type in 3-column grid with dividers
- **AI report note:** File icon + "AI-generated report will be ready within 30 minutes"
- **Star rating:** 5 tappable stars (32dp), golden when selected with scale animation
- **Buttons:** "Back to Home" (Primary) + "View Session History" (Secondary)

### SCREEN 11: Schedule Booking
- **Trainer mini card:** Avatar, name, rate/min
- **Date picker:** Horizontal scrolling 7-day strip — each shows weekday, date number, month — selected has Primary bg
- **Time slots:** Chip grid of available times (09:00, 10:00, 11:00, 14:00, etc.)
- **Duration picker:** Chips — 15 min, 30 min, 60 min
- **Dog selector:** Chips with dog emoji + name
- **Estimated cost** text at bottom
- **"Confirm Booking"** button — disabled until time selected

### SCREEN 12: Dog Profiles List
- **Top bar:** "My Dogs" + back + "+" add button
- **Dog cards:** Each shows dog emoji (🐕), name (16sp Bold), breed + age + gender, vaccination badge (green if "Up to date", warning if overdue), issue count badge, chevron right
- **Empty state:** Paw icon, "No dogs added yet", "Add your dog's profile to get personalized training advice", "Add Dog" button

### SCREEN 13: Add/Edit Dog Form
- **Top bar:** "Add Dog" or "Edit [name]" + back
- **Fields:** Dog Name* (input), Breed* (chip selection from allBreeds), Age (input), Weight (input), Gender (Male/Female chips), Vaccination Status (3 chips), Behavioral Issues (tag input with add/remove), Medical History (multiline textarea)
- **Save button:** Full width, disabled until name + breed filled

### SCREEN 14: Wallet Screen
- **Balance card:** Gradient, large balance amount, "≈ XX min at avg. rate", red variant if low balance with warning text
- **Recharge Packs:** 2×2 grid — each shows label, price (large), minutes, "POPULAR"/"BEST VALUE" ribbon badges
- **Custom Amount:** Expands to input + "Pay" button (min ₹100)
- **Security note:** Shield icon + "Secure payments via Razorpay" + "UPI · Cards · Net Banking · Wallets"
- **Transaction History:** List with label, date+method, amount (green for recharge, red for deduction)
- **Processing overlay:** Centered card with hourglass emoji + "Processing payment..." during recharge

### SCREEN 15: Session History
- **Session cards:** Avatar, trainer name + call type icon, dog name + duration + cost, date + "Report" badge, summary text
- **Empty state:** Clock icon + "No sessions yet"

### SCREEN 16: Session Detail
- **Trainer card:** Avatar (48dp), name, date + time
- **Stats grid:** Duration | Cost | Type in 3 columns
- **Summary text**
- **Action buttons:** "View Report" (if hasReport), "Open Chat" (if hasChat), "Book Again"

### SCREEN 17: Reports List
- **Report cards:** File icon in Primary bg circle, dog name + trainer name, date, Approved/Pending badge
- **Empty state:** File icon + "No reports yet"

### SCREEN 18: Report Detail
- **Top bar:** "Consultation Report" + back + share/download icons
- **Header card:** Dog name, trainer name, date, "AI-Generated" badge
- **Sections (each with uppercase Primary label 13sp):**
  - Issue Discussed
  - Trainer Observations
  - Suggested Solutions
  - Daily Routine
  - Training Instructions
  - Follow-up Recommendations
- **Trust badge:** Shield icon + "This report was generated using AI..."

### SCREEN 19: Chat Screen
- **Top bar:** Trainer name + back
- **Message list:** Bubbles — user messages right-aligned (Primary bg, white text, rounded with small bottom-right radius), trainer messages left-aligned (Surface bg with border)
- **Each bubble:** Text + time (small, right-aligned, 60% opacity)
- **Input area:** TextField with rounded border + send button (Primary circle)

### SCREEN 20: Notifications
- **Notification items:** Type icon in circle, title (Bold), body text, time ago, unread dot (Primary) + tinted background for unread items
- **Types:** report=file icon, reminder=clock, promo=zap, session=phone

### SCREEN 21: Settings / Profile
- **Profile card:** Avatar (56dp), name (18sp Bold), phone, city, "Edit" button
- **Role switcher card:** Dashboard icon + "Switch to Trainer/Client Mode" — tappable, Primary tinted bg
- **Menu items list:** Session History, My Reports, My Reviews, Notifications (with "2" badge), Privacy & Data Policy, Consent Settings ("Call recording & AI notes"), Report a Problem, Help & Support, Sign Out (danger red)
- **Each item:** Icon in circle + label + optional description + chevron right

### SCREEN 22: Trainer Dashboard
- **Header:** "Trainer Dashboard" label + name "Arjun Mehta" + notification bell
- **Stats grid (2×2):** This Month earnings (green), Pending Payout (brown), Sessions count (olive), Avg Rating (gold) — each with icon in colored bg + label + large value
- **Upcoming Calls section:** Cards with client avatar, name, dog + breed, duration, time + date, call type badge, issue text
- **Quick actions row:** My Profile, Availability, Notes, Earnings — 4 icon buttons
- **Verification status card:** Shield + "Verified Trainer" + "KYC complete" + Active badge

### SCREEN 23: Trainer Profile Management
- **Avatar (72dp), name, Verified badge**
- **Editable fields:** Bio (multiline), Rate per minute (number)
- **Read-only displays:** Specializations badges, Languages badges, Certifications list
- **"Save Changes" button**

### SCREEN 24: Availability Settings
- **Online toggle:** "Online Status" + "Accepting calls" subtitle + Switch
- **Weekly schedule:** Card per day — day name (capitalized), time slot badges (e.g., "09:00 – 12:00") or "Day off" badge (warning variant)

### SCREEN 25: Trainer Earnings
- **Earnings hero card:** Gradient (Secondary → SecondaryDark), "Total Earned" + large amount, This Month + Last Month breakdown
- **Pending payout card:** Amount + "Withdraw" button
- **Recent payouts list:** Method, date, amount (green), status badge

### SCREEN 26: Trainer Withdraw
- **Available amount** (large, centered card)
- **Method selector:** Bank Transfer / UPI chips
- **Amount input**
- **Info text:** Processing time, minimum withdrawal, commission rate
- **"Request Withdrawal" button**

### SCREEN 27: Consultation Notes (Trainer)
- **List view:** Report cards with file icon, dog name, issue preview, Approved/Review badge
- **Tap into detail view:** AI-Generated Draft badge, each section displayed
- **Two modes:** View (read-only) and Edit (all sections become textareas)
- **Buttons:** Edit + "Approve & Send" in view mode, Cancel + "Save Changes" in edit mode

### SCREEN 28: Upcoming Calls (Trainer)
- **Call cards:** Client avatar, name, dog + breed, call type badge, date + time + duration, issue text with 📋 prefix

---

## 10. STATE MANAGEMENT & VIEWMODELS

### AppState (held in a top-level ViewModel or shared across app)
```kotlin
data class AppState(
    val isLoggedIn: Boolean = false,
    val currentRole: UserRole = UserRole.CLIENT,
    val walletBalance: Int = 450,
    val currentUser: User = MOCK_CLIENT_USER
)
```

### Key ViewModels

**HomeViewModel:** Exposes topTrainers, recentSession, walletBalance, unreadNotificationCount

**BrowseTrainersViewModel:** Manages search query, filters (city, specialization, online, maxPrice), sortBy, filtered trainer list

**CallViewModel:** Manages CallState state machine, elapsed timer (seconds), cost calculation, mute/speaker/video/pause toggles. Timer runs via coroutine `while(active) { delay(1000); elapsed++ }`. Calculates `costSoFar = ceil(elapsed / 60) * ratePerMin`. Triggers low balance warning at `remainingMins <= 2`. Auto-ends call when `remainingBalance <= 0`.

**WalletViewModel:** Manages balance, transactions, recharge processing state

**DogViewModel:** CRUD operations for dog profiles

---

## 11. CALL SYSTEM STATE MACHINE

```
IDLE → (initiateCall) → CONNECTING → (2s delay) → ACTIVE
ACTIVE → (togglePause) → PAUSED → (togglePause) → ACTIVE
ACTIVE → (endCall) → ENDED → (navigate to PostCall)
ACTIVE → (balanceZero) → ENDED → (navigate to PostCall)
CONNECTING → (timeout/error) → FAILED
```

### Timer Logic (in CallViewModel)
```kotlin
private var timerJob: Job? = null

fun startTimer() {
    timerJob = viewModelScope.launch {
        while (callState.value == CallState.ACTIVE) {
            delay(1000)
            _elapsed.value += 1

            val costSoFar = ceil(_elapsed.value / 60.0).toInt() * ratePerMin
            val remaining = walletBalance - costSoFar
            val remainingMins = remaining / ratePerMin

            if (remainingMins <= 2 && !lowBalanceShown) {
                _showLowBalanceWarning.value = true
                lowBalanceShown = true
            }
            if (remaining <= 0) {
                endCall()
            }
        }
    }
}
```

### Call Controls
- **Mute:** Toggle `isMuted` state, update UI icon (mic / micOff)
- **Speaker:** Toggle `isSpeakerOn`
- **Video:** Toggle `isVideoOn` (video calls only)
- **Pause:** Toggle between ACTIVE and PAUSED states, pause timer during PAUSED

---

## 12. WALLET & BILLING LOGIC

### Initial Balance: ₹450

### Recharge Packs
| ID | Minutes | Price | Label | Badge |
|----|---------|-------|-------|-------|
| p1 | 5 | ₹75 | Quick Chat | — |
| p2 | 10 | ₹140 | Short Session | — |
| p3 | 30 | ₹399 | Full Consultation | POPULAR |
| p4 | 60 | ₹749 | Deep Dive | BEST VALUE |

### Custom recharge: Min ₹100, Max ₹5000

### Recharge Flow
1. User taps pack or enters custom amount
2. Show processing overlay (simulate 800ms delay)
3. Add WalletTransaction(type=RECHARGE, amount=+price)
4. Update balance
5. Show snackbar "Recharged ₹XXX successfully!"

### Call Deduction Flow
1. On call end: calculate `totalCost = ceil(elapsedSeconds / 60) * ratePerMin`
2. Add WalletTransaction(type=DEDUCTION, amount=-totalCost)
3. Update balance

### Low Balance Warning
- Trigger when `remainingMinutes <= 2` during active call
- Show dialog with remaining time, "Continue" and "End Call" buttons
- Show warning on wallet card when `balance < 100`

---

## 13. AI REPORT SYSTEM

### Mock Pipeline
After a consultation ends, simulate AI report generation:

1. **Mock Transcript:** Pre-written sample transcript stored in MockAISummarizer
2. **Generate Structured Report:** Return a `SessionReport` with:
   - issueDiscussed
   - observations
   - solutions (numbered list)
   - routine (Morning/MidDay/Evening/Night schedule)
   - instructions (specific actionable steps)
   - followUp (next session recommendation)
3. **Status flow:** DRAFT → trainer reviews → edits (optional) → APPROVED → visible to client

### Trainer Flow
- Trainer sees report in Notes screen with "Review" badge
- Can tap to view full report
- "Edit" button makes all sections editable
- "Approve & Send" finalizes and sends to client

### Client Flow
- Client sees "Report ready" notification
- Opens from Session History or Reports list
- Read-only view with all sections
- Share/Download action placeholders

---

## 14. MOCK DATA (INDIA-SPECIFIC)

### Trainers (8 trainers — use EXACT data from prototype)
See Section 5 TrainerProfile model. The 8 trainers are:
1. Arjun Mehta — Pune, ₹15/min, Behavior/Aggression/Anxiety, Hindi/English/Marathi, 4.9★
2. Priya Sharma — Mumbai, ₹12/min, Puppy/Obedience/Socialization, Hindi/English, 4.8★
3. Vikram Reddy — Hyderabad, ₹10/min, Obedience/Protection/Recall, Telugu/English/Hindi, 4.7★
4. Lakshmi Iyer — Chennai, ₹18/min, Therapy/Anxiety/Senior/Special Needs, Tamil/English, 4.9★
5. Kabir Singh — Delhi, ₹8/min, Protection/Guard/Fitness, Hindi/English/Punjabi, 4.6★
6. Meera Nair — Bengaluru, ₹14/min, Aggression/Behavior/Rescue, Malayalam/English/Kannada, 4.8★
7. Rohit Desai — Pune, ₹10/min, General/Puppy/Leash, Marathi/Hindi/English, 4.5★
8. Ananya Banerjee — Kolkata, ₹12/min, Puppy/Trick/Agility, Bengali/Hindi/English, 4.7★

### Dogs (2 dogs)
1. Bruno — German Shepherd, 3 years, Male, 34kg, vaccinated, issues: leash pulling + stranger anxiety + doorbell barking
2. Cookie — Golden Retriever, 1 year, Female, 22kg, vaccinated, issues: excessive barking + jumping on guests + chewing furniture

### Sessions (3 completed sessions)
### Reports (2 approved reports — with full structured content)
### Reviews (5 reviews)
### Notifications (4 items, 2 unread)
### Wallet Transactions (6 items — 3 recharges, 3 deductions)
### Trainer Earnings (thisMonth: ₹12,450, pending: ₹4,200, total: ₹89,600)
### Upcoming Calls (3 scheduled calls for trainer)

*All data values are in the prototype's mock-data.js — copy values exactly.*

### Reference Data Lists
- **Cities:** Pune, Mumbai, Bengaluru, Delhi, Hyderabad, Chennai, Kolkata, Jaipur, Ahmedabad
- **Languages:** English, Hindi, Marathi, Tamil, Telugu, Kannada, Malayalam, Bengali, Punjabi
- **Specializations:** Behavior, Aggression, Puppy Training, Obedience, Therapy Dogs, Protection Sports, Anxiety, Socialization, Recall Training, Trick Training, Agility, Rescue Rehabilitation, Leash Manners, Senior Dogs, Guard Dog Training
- **Breeds:** German Shepherd, Golden Retriever, Labrador, Indie (Indian Pariah), Rottweiler, Beagle, Shih Tzu, Pomeranian, Doberman, Belgian Malinois, Cocker Spaniel, Cane Corso, Pitbull, Mixed Breed, Other
- **Currency:** Always format as ₹ with Indian number system (e.g., ₹12,450 not ₹12450)

---

## 15. GITHUB ACTIONS WORKFLOW

Create this file at `.github/workflows/build.yml`:

```yaml
name: Build & Release APK

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Cache Gradle packages
        uses: actions/cache@v4
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: gradle-${{ runner.os }}-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
          restore-keys: gradle-${{ runner.os }}-

      - name: Grant execute permission for gradlew
        run: chmod +x gradlew

      - name: Build Debug APK
        run: ./gradlew assembleDebug

      - name: Run Lint
        run: ./gradlew lintDebug
        continue-on-error: true

      - name: Upload Debug APK
        uses: actions/upload-artifact@v4
        with:
          name: snout-scout-debug-apk
          path: app/build/outputs/apk/debug/app-debug.apk
          retention-days: 30

      - name: Upload Lint Report
        uses: actions/upload-artifact@v4
        if: always()
        with:
          name: lint-report
          path: app/build/reports/lint-results-debug.html
          retention-days: 7

  release:
    needs: build
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Cache Gradle packages
        uses: actions/cache@v4
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: gradle-${{ runner.os }}-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
          restore-keys: gradle-${{ runner.os }}-

      - name: Grant execute permission for gradlew
        run: chmod +x gradlew

      - name: Build Release APK
        run: ./gradlew assembleRelease

      - name: Sign APK (debug keystore for now)
        run: |
          # For production, replace with your keystore
          # The debug build is already signed with debug keystore

      - name: Get version
        id: version
        run: echo "version=$(date +'%Y.%m.%d-%H%M')" >> $GITHUB_OUTPUT

      - name: Create Release
        uses: softprops/action-gh-release@v2
        with:
          tag_name: v${{ steps.version.outputs.version }}
          name: "Snout Scout v${{ steps.version.outputs.version }}"
          body: |
            ## Snout Scout Trainer Connect
            Auto-built from main branch.

            ### Download
            Download the APK below and install on your Android device.
            (Enable "Install from unknown sources" in Settings)
          files: app/build/outputs/apk/debug/app-debug.apk
          draft: false
          prerelease: true
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

### What this workflow does:
1. **On every push to main:** Builds the debug APK
2. **Uploads APK as artifact:** Downloadable from GitHub Actions tab
3. **Creates a GitHub Release:** With the APK attached, downloadable from Releases page
4. **Runs lint checks** for code quality

### To download and test:
1. Go to your repo → Actions tab → latest successful run
2. Download "snout-scout-debug-apk" artifact
3. OR go to Releases → download the APK
4. Transfer to your Android phone, enable "Install from unknown sources", install

---

## 16. BUILD & RUN INSTRUCTIONS

### For Claude Code / AI coding agent:

```bash
# 1. Create new Android project
# Use Android Studio project structure or create manually

# 2. Set up Gradle wrapper
gradle wrapper --gradle-version 8.11.1

# 3. Create the project structure as defined in Section 3

# 4. Implement in order (Section 17)

# 5. Build
./gradlew assembleDebug

# 6. If build fails, read error output and fix

# 7. Run on emulator
# Create AVD: Pixel 7, API 35, x86_64
./gradlew installDebug
adb shell am start -n com.snoutscout.app/.MainActivity

# 8. Test all flows per verification checklist (Section 18)
```

### Settings for gradle.properties:
```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
kotlin.code.style=official
android.nonTransitiveRClass=true
```

---

## 17. IMPLEMENTATION ORDER

Follow this order strictly. Each step should compile before moving to the next.

### Phase 1: Project Setup (build must pass)
1. Create project with build.gradle files
2. Create `MainActivity.kt` with empty Compose content
3. Add `SnoutScoutApp.kt` Application class
4. Add `AndroidManifest.xml`
5. **VERIFY: `./gradlew assembleDebug` passes**

### Phase 2: Theme & Core UI
6. Implement `Color.kt`, `Type.kt`, `Shape.kt`, `Theme.kt`
7. Implement all core UI components (SSCard, SSButton, SSChip, SSAvatar, SSRating, SSBadge, SSInput, SSEmptyState, SSSectionHeader, SSTrainerCard, SSTopBar)
8. **VERIFY: Build passes**

### Phase 3: Data Layer
9. Create all data models (Section 5)
10. Create Room entities, DAOs, database, converters
11. Create service interfaces (CallService, PaymentGateway, AISummarizer)
12. Create mock service implementations
13. Create repositories
14. Create DI container (AppContainer)
15. Create mock data provider class
16. **VERIFY: Build passes**

### Phase 4: Navigation
17. Create Routes sealed class
18. Create NavGraph with all destinations (composable stubs initially)
19. Create BottomNavItems
20. Set up MainActivity with NavHost + bottom nav + role-based tab switching
21. **VERIFY: App launches, bottom nav visible, tabs switch**

### Phase 5: Auth Screens
22. Implement SplashScreen
23. Implement OnboardingScreen
24. Implement LoginScreen
25. **VERIFY: Splash → Onboarding → Login → Home flow works**

### Phase 6: Client Screens
26. Implement HomeScreen + HomeViewModel
27. Implement BrowseTrainersScreen + BrowseTrainersViewModel + FilterBottomSheet
28. Implement TrainerProfileScreen + TrainerProfileViewModel
29. Implement DogsListScreen + DogFormScreen + DogViewModel
30. Implement WalletScreen + WalletViewModel
31. **VERIFY: All client tab screens work, navigation between them works**

### Phase 7: Call System
32. Implement CallPreCheckScreen
33. Implement ActiveCallScreen + CallViewModel (with timer, state machine, deduction)
34. Implement PostCallScreen
35. Implement ScheduleBookingScreen
36. **VERIFY: Full call flow works — pre-check → connecting → timer → deduction → low balance → end → summary**

### Phase 8: History & Reports
37. Implement SessionHistoryScreen + SessionDetailScreen
38. Implement ReportsListScreen + ReportDetailScreen
39. Implement ChatScreen
40. Implement NotificationsScreen
41. Implement SettingsScreen with role switcher
42. **VERIFY: All history/report screens work, role switching works**

### Phase 9: Trainer Mode
43. Implement TrainerDashboardScreen + TrainerDashboardViewModel
44. Implement TrainerProfileMgmtScreen
45. Implement TrainerAvailabilityScreen
46. Implement TrainerEarningsScreen + TrainerWithdrawScreen
47. Implement TrainerNotesScreen + TrainerNotesViewModel (with edit/approve flow)
48. Implement TrainerUpcomingScreen
49. **VERIFY: All trainer screens work, can switch between client/trainer**

### Phase 10: Polish
50. Add dark mode support (test all screens)
51. Add loading/skeleton states
52. Add error states with retry
53. Add confirmation dialogs for sensitive actions
54. Add snackbars for wallet recharge, booking, report approval
55. Add the app logo (assets/logo.jpeg) to splash, onboarding, login, home
56. Centralize all strings to strings.xml
57. Add input validation
58. **VERIFY: Full verification checklist (Section 18)**

### Phase 11: GitHub
59. Initialize git repo
60. Add .github/workflows/build.yml (Section 15)
61. Add .gitignore for Android
62. Push to GitHub
63. **VERIFY: GitHub Actions builds successfully, APK artifact available**

---

## 18. VERIFICATION CHECKLIST

Run through EVERY item. Fix failures before proceeding.

### Build
- [ ] `./gradlew assembleDebug` passes with no errors
- [ ] `./gradlew lintDebug` has no critical issues
- [ ] App launches on emulator without crash

### Auth Flow
- [ ] Splash screen shows logo + branding for ~2s
- [ ] Onboarding shows 3 slides with navigation
- [ ] Login accepts phone number, shows OTP screen
- [ ] OTP verification navigates to home

### Client Navigation
- [ ] Bottom nav has 5 tabs: Home, Browse, My Dogs, Wallet, Profile
- [ ] All 5 tabs are reachable and render content
- [ ] Back navigation works on detail screens
- [ ] Bottom nav hides on call screens

### Home Dashboard
- [ ] Shows greeting with logo
- [ ] Quick action buttons navigate correctly
- [ ] Wallet balance card shows correct amount
- [ ] Recent session card appears
- [ ] Top trainers list renders
- [ ] Coming soon cards are visible but locked

### Browse & Search
- [ ] Trainers list renders all 8 trainers
- [ ] Search filters by name and specialization
- [ ] Sort by rating/price/experience works
- [ ] Filter bottom sheet opens
- [ ] City filter works
- [ ] Specialization filter works
- [ ] Online toggle works
- [ ] Price slider works
- [ ] "Show N trainers" count updates in real-time
- [ ] Reset clears all filters

### Trainer Profile
- [ ] Shows hero section with stats
- [ ] Call/Video/Schedule buttons work (disabled if offline)
- [ ] About tab shows bio, specializations, languages, breeds
- [ ] Reviews tab shows ratings and review cards
- [ ] Credentials tab shows certifications and verification

### Dog Profiles
- [ ] List shows existing dogs (Bruno, Cookie)
- [ ] Tap opens edit form with pre-filled data
- [ ] "+" button opens add form
- [ ] Can select breed from chips
- [ ] Can add/remove behavioral issues
- [ ] Save creates/updates dog in local database
- [ ] Snackbar confirms save

### Wallet
- [ ] Balance card shows ₹450 initially
- [ ] Low balance warning shows when balance < ₹100
- [ ] Tapping recharge pack adds to balance
- [ ] Processing overlay shows briefly
- [ ] Custom recharge works (min ₹100)
- [ ] Transaction history updates after recharge
- [ ] Currency formatted as ₹X,XXX (Indian format)

### Call System
- [ ] Pre-check screen shows trainer, balance, estimated time
- [ ] Dog selection works
- [ ] Consent checkbox required
- [ ] Low balance → "Recharge now" button navigates to wallet
- [ ] Connecting state shows for ~2s
- [ ] Timer counts up every second
- [ ] Cost ticker updates per minute
- [ ] Progress bar shows wallet consumption
- [ ] Mute toggle works
- [ ] Speaker toggle works
- [ ] Video toggle works (video call)
- [ ] Pause/resume works (timer pauses)
- [ ] Low balance warning dialog appears at ≤2 min remaining
- [ ] End call navigates to post-call summary
- [ ] Auto-end at ₹0 balance
- [ ] Post-call shows duration, cost, rating stars

### History & Reports
- [ ] Session history shows 3 sessions
- [ ] Session detail shows all info + action buttons
- [ ] Reports list shows 2 reports
- [ ] Report detail shows all 6 sections
- [ ] Chat screen shows mock messages

### Notifications
- [ ] Shows 4 notifications
- [ ] Unread items have tinted background and dot
- [ ] Type-specific icons render

### Settings
- [ ] Profile info displays correctly
- [ ] Role switcher navigates to trainer dashboard
- [ ] Menu items are tappable
- [ ] Sign out returns to splash

### Trainer Mode
- [ ] Dashboard shows stats grid (4 items)
- [ ] Upcoming calls list renders
- [ ] Quick action buttons navigate correctly
- [ ] Verification status card shows
- [ ] Profile management shows editable fields
- [ ] Availability shows weekly schedule
- [ ] Earnings shows totals and payout history
- [ ] Withdraw screen works
- [ ] Notes list shows reports with status
- [ ] Can open report, edit sections, approve
- [ ] Upcoming calls list renders with details

### Role Switching
- [ ] Client → Trainer switches bottom nav tabs
- [ ] Trainer → Client switches back
- [ ] Snackbar confirms switch

### Dark Mode
- [ ] All screens render correctly in dark mode
- [ ] Colors contrast properly
- [ ] Cards, badges, buttons all theme correctly

### UX Quality
- [ ] No placeholder "TODO" text visible on screens
- [ ] Empty states are polished (icon + title + subtitle)
- [ ] Forms have helpful placeholders
- [ ] Buttons have appropriate disabled states
- [ ] Snackbars show for key actions
- [ ] No obvious visual bugs or misalignment

---

## INSTRUCTIONS FOR CLAUDE CODE

When implementing this app in VS Code with Claude:

1. **Give Claude this entire document** as context
2. **Tell Claude:** "Implement the Snout Scout Android app following this specification exactly. Start with Phase 1 and proceed through each phase. Verify the build passes after each phase before continuing."
3. **After each phase,** ask Claude to run `./gradlew assembleDebug` and fix any errors
4. **After Phase 10,** run through the verification checklist
5. **After Phase 11,** push to GitHub and verify the Actions workflow builds successfully

### Key reminders for Claude Code:
- Use Kotlin 2.1.0+ with the Compose compiler plugin (NOT kotlinCompilerExtensionVersion)
- Use KSP for Room annotation processing (NOT kapt)
- All composables should have @Preview functions with sample data
- Keep files under 300 lines — split large screens into smaller composable functions
- Use `Flow` and `StateFlow` for reactive data
- Hoist state in composables — pass lambdas for actions
- Format all currency as Indian Rupees with `₹` symbol and Indian number formatting
- The app logo is at `assets/logo.jpeg` in this project — copy it to `app/src/main/res/drawable/`

---

*Document generated from the Snout Scout Trainer Connect interactive prototype. The prototype is the source of truth for all visual and interaction details.*
