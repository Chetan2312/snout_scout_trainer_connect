# Snout Scout Trainer Connect — Implementation Tracker

> **How to use this file:**
> At the start of every new chat session, read this file first.
> It tells you exactly what is done, what the current build status is, and what to do next.
> After completing any work, update this file before ending the session.

---

## Current Status

**Build:** ✅ assembleDebug passes (as of 2026-05-25, commit `7a918f5`)
**Lint:** ⚠️ lintDebug has 68 warnings but `continue-on-error: true` — not blocking
**GitHub Actions:** ✅ APK artifact uploading; Release step may still fail (403 on some repos — see Known Issues)
**Last commit:** `ce50825` — feat: add app logo, sign out dialog, approve dialog

---

## Phase Completion

| Phase | Description | Status | Notes |
|-------|-------------|--------|-------|
| 1 | Project Setup (Gradle, Manifest, App/Main classes) | ✅ DONE | |
| 2 | Theme & Core UI Components | ✅ DONE | SSComponents, SSTrainerCard, Theme, Colors, Type, Shape |
| 3 | Data Layer (models, Room, repos, services, DI) | ✅ DONE | All in single files: Models.kt, MockData.kt, Entities.kt, Daos.kt, Repositories.kt, Services.kt, AppContainer.kt |
| 4 | Navigation Graph | ✅ DONE | Routes.kt, NavGraph.kt, BottomNavItems.kt — role-based client/trainer tabs |
| 5 | Auth Screens | ✅ DONE | SplashScreen, OnboardingScreen, LoginScreen |
| 6 | Client Screens | ✅ DONE | Home, BrowseTrainers+Filter, TrainerProfile, DogsList+Form, Wallet |
| 7 | Call System | ✅ DONE | CallPreCheck, ActiveCall, PostCall, ScheduleBooking, CallViewModel |
| 8 | History, Reports, Chat, Notifications, Settings | ✅ DONE | SessionHistory+Detail, ReportsList+Detail, ChatScreen, NotificationsScreen, SettingsScreen |
| 9 | Trainer Mode | ✅ DONE | TrainerDashboard, TrainerProfileMgmt, TrainerAvailability, TrainerEarnings+Withdraw, TrainerNotes, TrainerUpcoming |
| 10 | Polish | ⏳ PARTIAL | Basic polish done. Items below NOT done — see Phase 10 remaining work |
| 11 | GitHub Actions | ✅ DONE | build.yml with APK artifact + release step |

---

## Phase 10 — Remaining Work

These items from the spec are **not yet implemented**:

- [x] **App logo on screens** — `logo.jpeg` copied to `res/drawable/`. Used on Splash (120dp circle), Login (72dp circle), Onboarding page 1 (100dp circle). Home screen still uses paw emoji in header (low priority).
- [ ] **Loading/skeleton states** — No shimmer or skeleton while Room data loads. Screens show empty or immediately render mock data.
- [ ] **Error states with retry** — No error handling UI on any screen. Repositories return results but screens don't display error messages.
- [x] **Confirmation dialogs** — Sign Out (SettingsScreen) and Approve & Send (TrainerNotesScreen) now have AlertDialog confirmations. "Book Again" button in SessionDetailScreen has no action (navigates nowhere) — low priority.
- [x] **Input validation** — Wallet custom recharge enforces min ₹100 (button disabled below). Login enforces 10-digit phone length. Max ₹5000 NOT enforced — low priority.
- [ ] **Strings centralized** — Many UI strings are hardcoded in composables rather than in `res/values/strings.xml`.
- [ ] **Dark mode testing** — Theme has dark colors defined but screens haven't been verified in dark mode.
- [ ] **@Preview annotations** — Spec says all composables should have `@Preview` functions. None exist currently.

---

## File Map (what is in each file)

### Single-file modules (multi-class files)
- `data/model/Models.kt` — ALL data classes + enums: User, UserRole, TrainerProfile, DogProfile, WalletTransaction, TransactionType, ConsultationSession, CallType, SessionStatus, CallState, SessionReport, ReportStatus, TrainerReview, RechargePack, AvailabilitySlot, TimeSlot, NotificationItem, NotificationType, PayoutRequest, UpcomingCall, AppState
- `data/model/MockData.kt` — MockData object with TRAINERS(8), DOGS(2), SESSIONS(3), REPORTS(2), REVIEWS(5), NOTIFICATIONS(4), WALLET_TRANSACTIONS(6), RECHARGE_PACKS(4), UPCOMING_CALLS(3), ALL_CITIES, ALL_BREEDS, ALL_SPECIALIZATIONS, etc.
- `data/local/entity/Entities.kt` — DogProfileEntity, WalletTransactionEntity, SessionEntity, ReportEntity, NotificationEntity, TrainerEntity
- `data/local/dao/Daos.kt` — DogProfileDao, WalletTransactionDao, SessionDao, ReportDao, NotificationDao, TrainerDao
- `data/repository/Repositories.kt` — TrainerRepository, DogRepository, WalletRepository, SessionRepository, ReportRepository, NotificationRepository
- `service/Services.kt` — CallService interface, PaymentGateway interface, AISummarizer interface, TranscriptionService interface, MockCallService, MockPaymentGateway, MockAISummarizer
- `core/ui/SSComponents.kt` — SSCard, SSButton, ButtonVariant, SSChip, SSAvatar, SSRating, SSBadge, BadgeType, SSInput, SSEmptyState, SSSectionHeader, SSTopBar

### Known deviations from spec structure
- Spec defines one file per model/entity/dao — we use consolidated files. This is fine; do not split them.
- `SnoutScoutApp.kt` and `MainActivity.kt` are at the root package (correct).
- No `domain/` layer (use cases, domain models) — skipped intentionally. Keep this way.
- `TrainerDashboardViewModel` was not created as a separate file; dashboard uses MockData directly.

---

## Known Issues / Bugs Fixed History

| Issue | Fix | Commit |
|-------|-----|--------|
| `Icons.AutoMirrored` import missing | Added `import androidx.compose.material.icons.automirrored.filled.ArrowBack` | bd842b1 |
| `TopAppBar` requires `@OptIn(ExperimentalMaterial3Api::class)` | Added opt-in to `SSTopBar` | feaa575 |
| `FlowRow` needs `@OptIn(ExperimentalLayoutApi::class)` on each fn that uses it | Added to AboutTab, FilterBottomSheet, etc. | feaa575 |
| Duplicate `@OptIn` annotations not repeatable | Merged into single `@OptIn(A::class, B::class)` | feaa575 |
| `scope.launch` / `kotlinx.coroutines.launch` missing import | Added import | feaa575 |
| `viewModelScope` in inline ViewModel missing import | Added `import androidx.lifecycle.viewModelScope` | feaa575 |
| `VerticalDivider` vs deprecated `Divider` | Replaced all vertical Divider usages with `VerticalDivider` | bd842b1 |
| `AppState.currentUser` circular reference with MockData | Changed to inline `User(...)` default | bd842b1 |
| Room `prepopulate` callback: `instance?` was null | Used local `dbRef` variable | bd842b1 |
| `SettingsScreen` broken Modifier extension | Removed extension, used `import androidx.compose.foundation.clickable` | bd842b1 |
| `StateFlow` collection pattern broken in SessionDetail/ReportDetail | Changed to `viewModel.flow.collectAsStateWithLifecycle()` | bd842b1 |
| `HomeScreen` private `Modifier.alpha` conflict | Removed; used `Modifier.alpha(0.7f)` with draw import | bd842b1 |
| CAMERA permission lint error (ChromeOS) | Added `<uses-feature android:required="false">` to Manifest | 7a918f5 |
| GitHub Release 403 (missing `contents: write` permission) | Added `permissions: contents: write` to build job | 7a918f5 |

---

## Compile-time Patterns (critical — do not regress)

These patterns caused compile errors when wrong. Always use these exact forms:

```kotlin
// ✅ Arrow back icon
import androidx.compose.material.icons.automirrored.filled.ArrowBack
Icons.AutoMirrored.Filled.ArrowBack

// ✅ TopAppBar (ExperimentalMaterial3Api)
@OptIn(ExperimentalMaterial3Api::class)
fun SSTopBar(...) { TopAppBar(...) }

// ✅ FlowRow (each function that calls FlowRow needs its own @OptIn)
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MyComposable() { FlowRow(...) }

// ✅ Multiple @OptIn — merge into one annotation (not repeatable)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

// ✅ VerticalDivider — NOT Divider with height/width
VerticalDivider(modifier = Modifier.height(40.dp))

// ✅ Coroutine scope in Composable
val scope = rememberCoroutineScope()
scope.launch { snackbarHostState.showSnackbar("message") }

// ✅ ViewModelScope in ViewModel class
import androidx.lifecycle.viewModelScope
class MyVM : ViewModel() {
    val data = repo.flow.stateIn(viewModelScope, ...)
}

// ✅ StateFlow in Composable
val items by viewModel.items.collectAsStateWithLifecycle()
```

---

## Architecture Notes

- **DI:** Manual via `AppContainer` in `SnoutScoutApp`. Access via `(context.applicationContext as SnoutScoutApp).container`.
- **ViewModels:** Created with factory classes (e.g., `HistoryViewModelFactory(container)`). No Hilt.
- **Navigation:** `NavHost` in `NavGraph.kt`. Bottom nav shown conditionally — hidden for call screens and auth screens.
- **Role switching:** `AppViewModel` holds `AppState.currentRole`. Switching role navigates to trainer/client root and swaps bottom nav items.
- **Room prepopulation:** `AppDatabase.kt` uses `addCallback(object : RoomDatabase.Callback() { override fun onCreate(...) })` with local `dbRef` variable (not `instance?`).
- **Mock services:** `MockCallService` uses `MutableStateFlow<CallState>`, transitions IDLE→CONNECTING(2s delay)→ACTIVE via coroutine.

---

## What To Do Next (ordered by priority)

1. **Verify GitHub Actions release step works** — push this file, confirm APK is downloadable from Releases tab. If 403 persists, the repo may need Settings → Actions → Workflow permissions → "Read and write permissions" enabled.
2. **App logo** — copy `assets/logo.jpeg` to `app/src/main/res/drawable/logo.jpeg`, update SplashScreen, OnboardingScreen, LoginScreen, HomeScreen to use `Image(painterResource(R.drawable.logo), ...)` in the appropriate places.
3. **Input validation** — wallet custom recharge (min ₹100), login phone (10 digits).
4. **Confirmation dialogs** — sign out, approve & send.
5. **Polish / dark mode audit** — run through verification checklist in DEVELOPER_HANDOFF.md Section 18.

---

## GitHub
- **Repo:** https://github.com/Chetan2312/snout_scout_trainer_connect
- **Main branch:** `main`
- **Actions:** https://github.com/Chetan2312/snout_scout_trainer_connect/actions
- **Push requires PAT** (no SSH configured locally):
  ```bash
  git remote set-url origin https://YOUR_PAT@github.com/Chetan2312/snout_scout_trainer_connect.git
  git push origin main
  ```

---

*Last updated: 2026-05-25 by Claude Sonnet 4.6*
