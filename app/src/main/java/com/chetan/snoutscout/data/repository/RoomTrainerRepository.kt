package com.chetan.snoutscout.data.repository

import com.chetan.snoutscout.data.local.dao.TrainerDao
import com.chetan.snoutscout.data.mapper.toDomain
import com.chetan.snoutscout.data.model.TrainerProfile
import com.chetan.snoutscout.domain.model.TrainerFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomTrainerRepository(
    private val trainerDao: TrainerDao
) : TrainerRepository {

    override fun getAllTrainers(): Flow<List<TrainerProfile>> {
        return trainerDao.observeAll().map { items ->
            items.map { it.toDomain() }
        }
    }

    override fun getFilteredTrainers(filter: TrainerFilter): Flow<List<TrainerProfile>> {
        return trainerDao.observeAll().map { items ->
            items.map { it.toDomain() }.filter { trainer ->
                val cityMatch = filter.city == null || trainer.city.equals(filter.city, ignoreCase = true)
                val languageMatch = filter.language == null || trainer.languages.any {
                    it.name.equals(filter.language, ignoreCase = true)
                }
                val aggressionMatch = !filter.aggressionSpecialist ||
                    trainer.specializations.any { it.name == "AGGRESSION" }
                val puppyMatch = !filter.puppyTrainer ||
                    trainer.specializations.any { it.name == "PUPPY" }
                val obedienceMatch = !filter.obedienceTrainer ||
                    trainer.specializations.any { it.name == "OBEDIENCE" }
                val protectionMatch = !filter.protectionSports ||
                    trainer.specializations.any { it.name == "PROTECTION_SPORTS" }
                val therapyMatch = !filter.therapyDogs ||
                    trainer.specializations.any { it.name == "THERAPY_DOGS" }
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
        }
    }

    override fun getFeaturedTrainers(): Flow<List<TrainerProfile>> {
        return trainerDao.observeFeatured().map { items ->
            items.map { it.toDomain() }
        }
    }
}