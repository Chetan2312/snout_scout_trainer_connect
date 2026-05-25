package com.snoutscout.app.navigation

sealed class Route(val route: String) {
    object Splash : Route("splash")
    object Onboarding : Route("onboarding")
    object Login : Route("login")

    object ClientHome : Route("client_home")
    object BrowseTrainers : Route("browse_trainers")
    object DogsList : Route("dogs_list")
    object Wallet : Route("wallet")
    object Settings : Route("settings")

    object TrainerProfile : Route("trainer_profile/{trainerId}") {
        fun createRoute(trainerId: String) = "trainer_profile/$trainerId"
    }
    object CallPreCheck : Route("call_precheck/{trainerId}/{callType}") {
        fun createRoute(trainerId: String, callType: String) = "call_precheck/$trainerId/$callType"
    }
    object ActiveCall : Route("active_call/{trainerId}/{dogId}/{callType}") {
        fun createRoute(trainerId: String, dogId: String, callType: String) = "active_call/$trainerId/$dogId/$callType"
    }
    object PostCall : Route("post_call/{trainerId}/{elapsed}/{cost}") {
        fun createRoute(trainerId: String, elapsed: Int, cost: Int) = "post_call/$trainerId/$elapsed/$cost"
    }
    object ScheduleBooking : Route("schedule_booking/{trainerId}") {
        fun createRoute(trainerId: String) = "schedule_booking/$trainerId"
    }
    object AddDog : Route("add_dog")
    object EditDog : Route("edit_dog/{dogId}") {
        fun createRoute(dogId: String) = "edit_dog/$dogId"
    }
    object SessionHistory : Route("session_history")
    object SessionDetail : Route("session_detail/{sessionId}") {
        fun createRoute(sessionId: String) = "session_detail/$sessionId"
    }
    object ReportsList : Route("reports_list")
    object ReportDetail : Route("report_detail/{reportId}") {
        fun createRoute(reportId: String) = "report_detail/$reportId"
    }
    object Chat : Route("chat/{sessionId}") {
        fun createRoute(sessionId: String) = "chat/$sessionId"
    }
    object Notifications : Route("notifications")

    object TrainerDashboard : Route("trainer_dashboard")
    object TrainerUpcoming : Route("trainer_upcoming")
    object TrainerNotes : Route("trainer_notes")
    object TrainerEarnings : Route("trainer_earnings")
    object TrainerProfileMgmt : Route("trainer_profile_mgmt")

    object TrainerAvailability : Route("trainer_availability")
    object TrainerWithdraw : Route("trainer_withdraw")
}
