package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.model.Language
import com.chetan.snoutscout.data.model.TrainerProfile
import com.chetan.snoutscout.data.model.TrainerSpecialization
import com.chetan.snoutscout.data.service.fake.FakeSeedData
import com.chetan.snoutscout.domain.model.TrainerFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTrainerRepository : TrainerRepository {

    override fun getAllTrainers(): Flow<List<TrainerProfile>> {
        return flowOf(FakeSeedData.trainers)
    }

    override fun getFilteredTrainers(filter: TrainerFilter): Flow<List<TrainerProfile>> {
        val filtered = FakeSeedData.trainers.filter { trainer ->
            val cityMatch = filter.city == null || trainer.city.equals(filter.city, ignoreCase = true)
            val languageMatch = filter.language == null || trainer.languages.any {
                it.name.equals(filter.language, ignoreCase = true)
            }
            val aggressionMatch = !filter.aggressionSpecialist ||
                trainer.specializations.contains(TrainerSpecialization.AGGRESSION)
            val puppyMatch = !filter.puppyTrainer ||
                trainer.specializations.contains(TrainerSpecialization.PUPPY)
            val obedienceMatch = !filter.obedienceTrainer ||
                trainer.specializations.contains(TrainerSpecialization.OBEDIENCE)
            val protectionMatch = !filter.protectionSports ||
                trainer.specializations.contains(TrainerSpecialization.PROTECTION_SPORTS)
            val therapyMatch = !filter.therapyDogs ||
                trainer.specializations.contains(TrainerSpecialization.THERAPY_DOGS)
            val onlineMatch = !filter.onlineAvailability || trainer.onlineAvailable
            val priceMatch = filter.maxPricePerMinute == null ||
                trainer.pricePerMinuteInInr <= filter.maxPricePerMinute
            val ratingMatch = filter.minRating == null || trainer.rating >= filter.minRating

            cityMatch &&
                languageMatch &&
                aggressionMatch &&
                puppyMatch &&
                obedienceMatch &&
                protectionMatch &&
                therapyMatch &&
                onlineMatch &&
                priceMatch &&
                ratingMatch
        }
        return flowOf(filtered)
    }

    override fun getFeaturedTrainers(): Flow<List<TrainerProfile>> {
        return flowOf(FakeSeedData.trainers.filter { it.featured })
    }
}