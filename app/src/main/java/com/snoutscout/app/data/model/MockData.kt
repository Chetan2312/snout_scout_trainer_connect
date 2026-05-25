package com.snoutscout.app.data.model

import java.util.concurrent.TimeUnit

object MockData {
    val CLIENT_USER = User(
        id = "u1", name = "Aarav Sharma", phone = "+91 98765 43210",
        email = "aarav@email.com", city = "Pune", role = UserRole.CLIENT
    )

    val TRAINER_USER = User(
        id = "t1", name = "Arjun Mehta", phone = "+91 99887 76655",
        email = "arjun@email.com", city = "Pune", role = UserRole.TRAINER
    )

    val TRAINERS = listOf(
        TrainerProfile(
            id = "t1", name = "Arjun Mehta", city = "Pune", rating = 4.9f, reviewCount = 234,
            ratePerMin = 15, experience = 8, specializations = listOf("Behavior", "Aggression", "Anxiety"),
            languages = listOf("Hindi", "English", "Marathi"), breeds = listOf("German Shepherd", "Rottweiler", "Belgian Malinois"),
            bio = "Certified dog behaviorist with 8+ years specializing in aggression management and anxiety disorders. Trained over 500 dogs across India.",
            isVerified = true, isOnline = true, isFeatured = true,
            certifications = listOf("CPDT-KA", "IAABC Member", "AKC Evaluator"),
            totalSessions = 312, responseTime = "< 2 min"
        ),
        TrainerProfile(
            id = "t2", name = "Priya Sharma", city = "Mumbai", rating = 4.8f, reviewCount = 189,
            ratePerMin = 12, experience = 5, specializations = listOf("Puppy Training", "Obedience", "Socialization"),
            languages = listOf("Hindi", "English"), breeds = listOf("Golden Retriever", "Labrador", "Beagle"),
            bio = "Puppy specialist and positive reinforcement trainer. I believe every dog has potential — you just need the right approach.",
            isVerified = true, isOnline = true, isFeatured = true,
            certifications = listOf("CPDT-KA", "Fear Free Certified"),
            totalSessions = 245, responseTime = "< 3 min"
        ),
        TrainerProfile(
            id = "t3", name = "Vikram Reddy", city = "Hyderabad", rating = 4.7f, reviewCount = 156,
            ratePerMin = 10, experience = 6, specializations = listOf("Obedience", "Protection Sports", "Recall Training"),
            languages = listOf("Telugu", "English", "Hindi"), breeds = listOf("Doberman", "German Shepherd", "Belgian Malinois"),
            bio = "Sports dog trainer and obedience specialist. Champion trainer with multiple IPO/Schutzhund qualified dogs.",
            isVerified = true, isOnline = false, isFeatured = false,
            certifications = listOf("IPO Trainer Level 3", "CPDT-KSA"),
            totalSessions = 198, responseTime = "< 5 min"
        ),
        TrainerProfile(
            id = "t4", name = "Lakshmi Iyer", city = "Chennai", rating = 4.9f, reviewCount = 201,
            ratePerMin = 18, experience = 10, specializations = listOf("Therapy Dogs", "Anxiety", "Senior Dogs", "Special Needs"),
            languages = listOf("Tamil", "English"), breeds = listOf("Golden Retriever", "Labrador", "Indie (Indian Pariah)"),
            bio = "Veteran trainer specializing in therapy dog certification and working with anxious or special needs dogs. Gentle, science-based methods only.",
            isVerified = true, isOnline = true, isFeatured = true,
            certifications = listOf("TDI Evaluator", "CPDT-KA", "AKC CGC Evaluator"),
            totalSessions = 389, responseTime = "< 2 min"
        ),
        TrainerProfile(
            id = "t5", name = "Kabir Singh", city = "Delhi", rating = 4.6f, reviewCount = 134,
            ratePerMin = 8, experience = 4, specializations = listOf("Protection Sports", "Guard Dog Training", "Fitness"),
            languages = listOf("Hindi", "English", "Punjabi"), breeds = listOf("Rottweiler", "Doberman", "Cane Corso"),
            bio = "Protection sports enthusiast and guard dog trainer. Affordable rates for serious training results.",
            isVerified = true, isOnline = true, isFeatured = false,
            certifications = listOf("IGP Level 2"),
            totalSessions = 167, responseTime = "< 4 min"
        ),
        TrainerProfile(
            id = "t6", name = "Meera Nair", city = "Bengaluru", rating = 4.8f, reviewCount = 178,
            ratePerMin = 14, experience = 7, specializations = listOf("Aggression", "Behavior", "Rescue Rehabilitation"),
            languages = listOf("Malayalam", "English", "Kannada"), breeds = listOf("Indie (Indian Pariah)", "Pitbull", "Mixed Breed"),
            bio = "Passionate rescue rehabilitator and behavior consultant. Helping dogs find their second chance since 2017.",
            isVerified = true, isOnline = false, isFeatured = false,
            certifications = listOf("IAABC Associate", "CPDT-KA"),
            totalSessions = 223, responseTime = "< 3 min"
        ),
        TrainerProfile(
            id = "t7", name = "Rohit Desai", city = "Pune", rating = 4.5f, reviewCount = 98,
            ratePerMin = 10, experience = 3, specializations = listOf("Puppy Training", "Obedience", "Leash Manners"),
            languages = listOf("Marathi", "Hindi", "English"), breeds = listOf("Labrador", "Golden Retriever", "Shih Tzu"),
            bio = "Young, enthusiastic trainer focused on building strong foundations with puppies and young dogs.",
            isVerified = false, isOnline = true, isFeatured = false,
            certifications = listOf("AKC S.T.A.R. Puppy Evaluator"),
            totalSessions = 89, responseTime = "< 5 min"
        ),
        TrainerProfile(
            id = "t8", name = "Ananya Banerjee", city = "Kolkata", rating = 4.7f, reviewCount = 145,
            ratePerMin = 12, experience = 5, specializations = listOf("Puppy Training", "Trick Training", "Agility"),
            languages = listOf("Bengali", "Hindi", "English"), breeds = listOf("Beagle", "Cocker Spaniel", "Pomeranian"),
            bio = "Trick training and agility specialist. Making dog training fun and rewarding for both dog and owner!",
            isVerified = true, isOnline = true, isFeatured = false,
            certifications = listOf("Do More With Your Dog Trick Dog Instructor", "AKC Agility Evaluator"),
            totalSessions = 178, responseTime = "< 3 min"
        )
    )

    val DOGS = listOf(
        DogProfile(
            id = "d1", name = "Bruno", breed = "German Shepherd", age = "3 years", gender = "Male",
            weight = "34 kg", vaccination = "Up to date", lastVaccination = "15 Jan 2024",
            issues = listOf("Leash pulling", "Stranger anxiety", "Doorbell barking"),
            medicalHistory = "Healthy. Hip evaluation done — mild dysplasia noted. On joint supplement.",
            previousSessions = 3
        ),
        DogProfile(
            id = "d2", name = "Cookie", breed = "Golden Retriever", age = "1 year", gender = "Female",
            weight = "22 kg", vaccination = "Up to date", lastVaccination = "3 Mar 2024",
            issues = listOf("Excessive barking", "Jumping on guests", "Chewing furniture"),
            medicalHistory = "Healthy puppy. Spayed at 8 months.",
            previousSessions = 1
        )
    )

    private val now = System.currentTimeMillis()
    private fun daysAgo(days: Int) = now - TimeUnit.DAYS.toMillis(days.toLong())

    val SESSIONS = listOf(
        ConsultationSession(
            id = "s1", trainerId = "t1", trainerName = "Arjun Mehta", dogId = "d1", dogName = "Bruno",
            type = CallType.VIDEO, durationMinutes = 30, cost = 450, date = daysAgo(3),
            status = SessionStatus.COMPLETED, rating = 5, hasReport = true, hasChat = true,
            summary = "Discussed Bruno's leash reactivity. Arjun demonstrated the 'Look at That' technique and created a structured desensitization plan."
        ),
        ConsultationSession(
            id = "s2", trainerId = "t2", trainerName = "Priya Sharma", dogId = "d2", dogName = "Cookie",
            type = CallType.VOICE, durationMinutes = 20, cost = 240, date = daysAgo(10),
            status = SessionStatus.COMPLETED, rating = 4, hasReport = true, hasChat = false,
            summary = "Addressed Cookie's jumping behavior. Priya recommended a 4-step 'Four on the Floor' protocol."
        ),
        ConsultationSession(
            id = "s3", trainerId = "t4", trainerName = "Lakshmi Iyer", dogId = "d1", dogName = "Bruno",
            type = CallType.VOICE, durationMinutes = 15, cost = 270, date = daysAgo(21),
            status = SessionStatus.COMPLETED, rating = 5, hasReport = false, hasChat = true,
            summary = "Quick check-in on Bruno's anxiety management progress. Discussed environmental enrichment strategies."
        )
    )

    val REPORTS = listOf(
        SessionReport(
            id = "r1", sessionId = "s1", trainerName = "Arjun Mehta", dogName = "Bruno",
            date = "22 May 2025", status = ReportStatus.APPROVED,
            issueDiscussed = "Leash reactivity towards strangers and other dogs during walks. Dog lunges and barks aggressively.",
            observations = "Bruno shows classic fear-based reactivity. His threshold distance is approximately 5 meters. He responds well to food rewards and disengages quickly once the trigger is removed.",
            solutions = "1. Implement 'Look at That' (LAT) game\n2. Counter-conditioning with high-value treats\n3. Structured decompression walks\n4. Distance management protocol",
            routine = "Morning: 15-min structured sniff walk with LAT practice\nAfteroon: Short training session (5 min) - focus/attention exercises\nEvening: Decompression walk in low-traffic area\nNight: Mental enrichment (snuffle mat, Kong)",
            instructions = "Keep Bruno at least 6 meters from triggers initially. Say 'Yes!' the moment he looks at the trigger calmly and deliver treat. Gradually decrease distance over 2-3 weeks. Never force interaction.",
            followUp = "Schedule follow-up in 3 weeks. Focus will be on reducing threshold distance to 3 meters and introducing controlled greetings."
        ),
        SessionReport(
            id = "r2", sessionId = "s2", trainerName = "Priya Sharma", dogName = "Cookie",
            date = "15 May 2025", status = ReportStatus.APPROVED,
            issueDiscussed = "Jumping on guests and family members. Cookie also barks excessively when visitors arrive.",
            observations = "Cookie's jumping is attention-seeking behavior reinforced by inconsistent responses from family. She is highly food motivated and learns quickly.",
            solutions = "1. Four on the Floor protocol\n2. Visitor greeting ritual\n3. Consistent family response\n4. 'Place' command training",
            routine = "Morning: 10-min obedience session (sit, stay, place)\nEvening: 15-min training with focus on 'Four on the Floor'\nVisitors: Follow the greeting protocol every single time",
            instructions = "When Cookie jumps: turn away completely, cross arms, no eye contact, no speaking. Only give attention when all 4 paws are on the ground. Every family member must be consistent.",
            followUp = "2-week check-in recommended. If family consistency is maintained, Cookie should show improvement within 10 days."
        )
    )

    val REVIEWS = listOf(
        TrainerReview("rv1", "t1", "Rahul M.", 5, "May 2025", "Arjun is incredible! Bruno's leash reactivity has reduced by 80% in just 3 weeks. Highly recommend!"),
        TrainerReview("rv2", "t1", "Sneha P.", 5, "Apr 2025", "Extremely knowledgeable and patient. He explained everything clearly and the techniques actually work."),
        TrainerReview("rv3", "t1", "Kiran D.", 4, "Mar 2025", "Very professional. My GSD is so much calmer now. Only wish sessions were longer."),
        TrainerReview("rv4", "t2", "Pooja A.", 5, "May 2025", "Priya is amazing with puppies! Cookie stopped jumping within a week of following her protocol."),
        TrainerReview("rv5", "t2", "Amit R.", 4, "Apr 2025", "Great trainer, very patient. Would have given 5 stars but scheduling was a bit difficult.")
    )

    val NOTIFICATIONS = listOf(
        NotificationItem(
            id = "n1", type = NotificationType.REPORT, title = "Report Ready",
            body = "Your session report with Arjun Mehta is ready to view.", date = daysAgo(0), isRead = false
        ),
        NotificationItem(
            id = "n2", type = NotificationType.REMINDER, title = "Upcoming Session Reminder",
            body = "Your session with Priya Sharma starts in 30 minutes.", date = daysAgo(1), isRead = false
        ),
        NotificationItem(
            id = "n3", type = NotificationType.PROMO, title = "Offer: 20% Cashback",
            body = "Recharge ₹500 or more today and get 20% cashback. Valid till midnight!", date = daysAgo(3), isRead = true
        ),
        NotificationItem(
            id = "n4", type = NotificationType.SESSION, title = "Session Completed",
            body = "Your 20-minute session with Priya Sharma has ended. Total cost: ₹240", date = daysAgo(10), isRead = true
        )
    )

    val WALLET_TRANSACTIONS = listOf(
        WalletTransaction("tx1", TransactionType.RECHARGE, 500, "UPI", daysAgo(1), "Wallet Recharge", null),
        WalletTransaction("tx2", TransactionType.DEDUCTION, -450, null, daysAgo(3), "Session - Arjun Mehta (30 min)", "s1"),
        WalletTransaction("tx3", TransactionType.RECHARGE, 399, "Card", daysAgo(7), "Wallet Recharge", null),
        WalletTransaction("tx4", TransactionType.DEDUCTION, -240, null, daysAgo(10), "Session - Priya Sharma (20 min)", "s2"),
        WalletTransaction("tx5", TransactionType.RECHARGE, 749, "UPI", daysAgo(20), "Wallet Recharge", null),
        WalletTransaction("tx6", TransactionType.DEDUCTION, -270, null, daysAgo(21), "Session - Lakshmi Iyer (15 min)", "s3")
    )

    val RECHARGE_PACKS = listOf(
        RechargePack("p1", 5, 75, "Quick Chat"),
        RechargePack("p2", 10, 140, "Short Session"),
        RechargePack("p3", 30, 399, "Full Consultation", isPopular = true),
        RechargePack("p4", 60, 749, "Deep Dive", isBestValue = true)
    )

    val UPCOMING_CALLS = listOf(
        UpcomingCall("uc1", "Aarav Sharma", "Bruno", "German Shepherd", CallType.VIDEO,
            now + TimeUnit.HOURS.toMillis(2), 30, "Leash reactivity follow-up"),
        UpcomingCall("uc2", "Deepika Roy", "Max", "Labrador", CallType.VOICE,
            now + TimeUnit.HOURS.toMillis(5), 20, "Excessive barking and separation anxiety"),
        UpcomingCall("uc3", "Suresh Pillai", "Rocky", "Indie (Indian Pariah)", CallType.VIDEO,
            now + TimeUnit.DAYS.toMillis(1), 45, "Aggression towards other dogs")
    )

    val AVAILABILITY = listOf(
        AvailabilitySlot("monday", listOf(TimeSlot("09:00", "12:00"), TimeSlot("17:00", "20:00"))),
        AvailabilitySlot("tuesday", listOf(TimeSlot("09:00", "12:00"), TimeSlot("17:00", "20:00"))),
        AvailabilitySlot("wednesday", emptyList()),
        AvailabilitySlot("thursday", listOf(TimeSlot("09:00", "12:00"), TimeSlot("17:00", "20:00"))),
        AvailabilitySlot("friday", listOf(TimeSlot("09:00", "12:00"), TimeSlot("14:00", "18:00"))),
        AvailabilitySlot("saturday", listOf(TimeSlot("10:00", "14:00"))),
        AvailabilitySlot("sunday", emptyList())
    )

    val PAYOUTS = listOf(
        PayoutRequest("po1", 8000, "10 May 2025", "completed", "Bank Transfer"),
        PayoutRequest("po2", 5500, "25 Apr 2025", "completed", "UPI"),
        PayoutRequest("po3", 4200, "31 May 2025", "pending", "Bank Transfer")
    )

    val ALL_BREEDS = listOf(
        "German Shepherd", "Golden Retriever", "Labrador", "Indie (Indian Pariah)",
        "Rottweiler", "Beagle", "Shih Tzu", "Pomeranian", "Doberman",
        "Belgian Malinois", "Cocker Spaniel", "Cane Corso", "Pitbull", "Mixed Breed", "Other"
    )

    val ALL_SPECIALIZATIONS = listOf(
        "Behavior", "Aggression", "Puppy Training", "Obedience", "Therapy Dogs",
        "Protection Sports", "Anxiety", "Socialization", "Recall Training",
        "Trick Training", "Agility", "Rescue Rehabilitation", "Leash Manners",
        "Senior Dogs", "Guard Dog Training"
    )

    val ALL_CITIES = listOf("Pune", "Mumbai", "Bengaluru", "Delhi", "Hyderabad", "Chennai", "Kolkata", "Jaipur", "Ahmedabad")
}
