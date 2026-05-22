package com.chetan.snoutscout.data.service.fake

import com.chetan.snoutscout.data.model.CallType
import com.chetan.snoutscout.data.model.ClientProfile
import com.chetan.snoutscout.data.model.ConsultationSession
import com.chetan.snoutscout.data.model.DogProfile
import com.chetan.snoutscout.data.model.Language
import com.chetan.snoutscout.data.model.NotificationItem
import com.chetan.snoutscout.data.model.SessionReport
import com.chetan.snoutscout.data.model.TrainerProfile
import com.chetan.snoutscout.data.model.TrainerReview
import com.chetan.snoutscout.data.model.TrainerSpecialization
import com.chetan.snoutscout.data.model.User
import com.chetan.snoutscout.data.model.VerificationStatus
import com.chetan.snoutscout.data.model.WalletTransaction
import com.chetan.snoutscout.domain.model.RechargePack
import com.chetan.snoutscout.domain.model.UserRole

object FakeSeedData {

    val currentUser = User(
        id = "user_1",
        fullName = "Chetan Patil",
        phoneNumber = "+91 9876543210",
        city = "Pune",
        role = UserRole.CLIENT
    )

    val clientProfile = ClientProfile(
        userId = "user_1",
        preferredLanguage = Language.ENGLISH,
        walletBalanceInInr = 1200
    )

    val trainers = listOf(
        TrainerProfile(
            id = "trainer_1",
            fullName = "Aarav Kulkarni",
            city = "Pune",
            bio = "Obedience and puppy routine specialist for urban families.",
            yearsOfExperience = 7,
            languages = listOf(Language.ENGLISH, Language.HINDI, Language.MARATHI),
            specializations = listOf(
                TrainerSpecialization.PUPPY,
                TrainerSpecialization.OBEDIENCE,
                TrainerSpecialization.ONLINE_CONSULT
            ),
            pricePerMinuteInInr = 25,
            rating = 4.9,
            totalReviews = 142,
            onlineAvailable = true,
            verificationStatus = VerificationStatus.VERIFIED,
            featured = true
        ),
        TrainerProfile(
            id = "trainer_2",
            fullName = "Meher Bedi",
            city = "Mumbai",
            bio = "Behavior, leash reactivity, and aggression consultations.",
            yearsOfExperience = 10,
            languages = listOf(Language.ENGLISH, Language.HINDI),
            specializations = listOf(
                TrainerSpecialization.AGGRESSION,
                TrainerSpecialization.BREED_SPECIFIC,
                TrainerSpecialization.ONLINE_CONSULT
            ),
            pricePerMinuteInInr = 35,
            rating = 4.8,
            totalReviews = 203,
            onlineAvailable = true,
            verificationStatus = VerificationStatus.VERIFIED,
            featured = true
        ),
        TrainerProfile(
            id = "trainer_3",
            fullName = "Shruti Nair",
            city = "Chennai",
            bio = "Calm handling, puppy foundations, and routine design.",
            yearsOfExperience = 5,
            languages = listOf(Language.ENGLISH, Language.TAMIL),
            specializations = listOf(
                TrainerSpecialization.PUPPY,
                TrainerSpecialization.OBEDIENCE
            ),
            pricePerMinuteInInr = 28,
            rating = 4.7,
            totalReviews = 96,
            onlineAvailable = true,
            verificationStatus = VerificationStatus.UNDER_REVIEW,
            featured = false
        ),
        TrainerProfile(
            id = "trainer_4",
            fullName = "Kabir Arora",
            city = "Delhi",
            bio = "Protection sports and advanced behavior case handling.",
            yearsOfExperience = 12,
            languages = listOf(Language.ENGLISH, Language.HINDI),
            specializations = listOf(
                TrainerSpecialization.PROTECTION_SPORTS,
                TrainerSpecialization.AGGRESSION
            ),
            pricePerMinuteInInr = 45,
            rating = 4.7,
            totalReviews = 121,
            onlineAvailable = false,
            verificationStatus = VerificationStatus.VERIFIED,
            featured = false
        ),
        TrainerProfile(
            id = "trainer_5",
            fullName = "Ritika Menon",
            city = "Bengaluru",
            bio = "Therapy-oriented support and confidence-building routines.",
            yearsOfExperience = 8,
            languages = listOf(Language.ENGLISH, Language.KANNADA, Language.HINDI),
            specializations = listOf(
                TrainerSpecialization.THERAPY_DOGS,
                TrainerSpecialization.ONLINE_CONSULT
            ),
            pricePerMinuteInInr = 40,
            rating = 4.9,
            totalReviews = 88,
            onlineAvailable = true,
            verificationStatus = VerificationStatus.VERIFIED,
            featured = true
        )
    )

    val dogProfiles = listOf(
        DogProfile(
            id = "dog_1",
            name = "Bruno",
            breed = "Labrador Retriever",
            ageInMonths = 18,
            vaccinationStatus = "Up to date",
            behavioralIssues = "Pulling on leash, overexcitement around guests",
            medicalHistory = "No major medical issues",
            previousSessionsSummary = "Worked on sit-stay and leash basics"
        )
    )

    val walletTransactions = listOf(
        WalletTransaction(
            id = "txn_1",
            title = "Wallet recharge",
            amountInInr = 1000,
            type = "credit",
            createdAt = "2026-05-18 10:30"
        ),
        WalletTransaction(
            id = "txn_2",
            title = "Video consultation with Aarav Kulkarni",
            amountInInr = -250,
            type = "debit",
            createdAt = "2026-05-19 17:10"
        )
    )

    val sessions = listOf(
        ConsultationSession(
            id = "session_1",
            trainerName = "Aarav Kulkarni",
            dogName = "Bruno",
            callType = CallType.VIDEO,
            scheduledAt = "2026-05-19 17:00",
            durationMinutes = 10,
            totalAmountInInr = 250,
            notesReady = true
        )
    )

    val reports = listOf(
        SessionReport(
            id = "report_1",
            sessionId = "session_1",
            dogIssueDiscussed = "Leash pulling and guest excitement",
            trainerObservations = "Dog responds well to food reinforcement but loses focus at the door.",
            suggestedSolutions = "Use threshold calmness drills and reward check-ins every 3 to 5 steps.",
            dailyRoutine = "Two structured walks, one enrichment meal, one settle session indoors.",
            trainingInstructions = "Practice short leash reset loops and place command before guest entry.",
            followUpRecommendations = "Book another 10-minute review in 5 days.",
            approved = true
        )
    )

    val reviews = listOf(
        TrainerReview(
            id = "review_1",
            trainerId = "trainer_1",
            clientName = "Nikita",
            rating = 5.0,
            reviewText = "Very practical and calm guidance. Helped us improve Bruno’s walk routine quickly."
        )
    )

    val notifications = listOf(
        NotificationItem(
            id = "notif_1",
            title = "Low wallet balance soon",
            body = "Recharge before your next video consult to avoid interruption.",
            createdAt = "2026-05-21 09:30",
            read = false
        ),
        NotificationItem(
            id = "notif_2",
            title = "Report approved",
            body = "Your latest training report is ready to view and share.",
            createdAt = "2026-05-20 19:00",
            read = true
        )
    )

    val rechargePacks = listOf(
        RechargePack("pack_1", "Starter Pack", 199, "Good for quick check-ins"),
        RechargePack("pack_2", "5 Minute Pack", 250, "Ideal for focused advice"),
        RechargePack("pack_3", "10 Minute Pack", 500, "Best for behavior reviews"),
        RechargePack("pack_4", "30 Minute Pack", 1400, "Great for deeper coaching"),
        RechargePack("pack_5", "60 Minute Pack", 2600, "Best value for training plans")
    )
}