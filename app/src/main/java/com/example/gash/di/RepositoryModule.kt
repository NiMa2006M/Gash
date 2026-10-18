package com.example.gash.di

import com.example.gash.core.locale.AppCompatLanguageProvider
import com.example.gash.core.locale.LanguageProvider
import com.example.gash.core.security.ActivationCodeValidator
import com.example.gash.core.security.AndroidIdProvider
import com.example.gash.core.security.DeviceIdProvider
import com.example.gash.core.security.XorActivationCodeValidator
import com.example.gash.data.repository.ActivationRepositoryImpl
import com.example.gash.data.repository.AnimalRepositoryImpl
import com.example.gash.data.repository.HerdRepositoryImpl
import com.example.gash.data.repository.ProfileImageRepositoryImpl
import com.example.gash.data.repository.RfidTagRepositoryImpl
import com.example.gash.data.repository.TagSettingsRepositoryImpl
import com.example.gash.data.repository.VaccinationRepositoryImpl
import com.example.gash.data.repository.VaccineTypeRepositoryImpl
import com.example.gash.data.repository.WeightRecordRepositoryImpl
import com.example.gash.data.transaction.RoomTransactionRunner
import com.example.gash.domain.repository.ActivationRepository
import com.example.gash.domain.repository.AnimalRepository
import com.example.gash.domain.repository.HerdRepository
import com.example.gash.domain.repository.ProfileImageRepository
import com.example.gash.domain.repository.RfidTagRepository
import com.example.gash.domain.repository.TagSettingsRepository
import com.example.gash.domain.repository.VaccinationRepository
import com.example.gash.domain.repository.VaccineTypeRepository
import com.example.gash.domain.repository.WeightRecordRepository
import com.example.gash.domain.transaction.TransactionRunner

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindActivationRepository(impl: ActivationRepositoryImpl): ActivationRepository

    @Binds
    @Singleton
    abstract fun bindHerdRepository(impl: HerdRepositoryImpl): HerdRepository

    @Binds
    @Singleton
    abstract fun bindAnimalRepository(impl: AnimalRepositoryImpl): AnimalRepository

    @Binds
    @Singleton
    abstract fun bindRfidTagRepository(impl: RfidTagRepositoryImpl): RfidTagRepository

    @Binds
    @Singleton
    abstract fun bindDeviceIdProvider(impl: AndroidIdProvider): DeviceIdProvider

    @Binds
    @Singleton
    abstract fun bindActivationCodeValidator(impl: XorActivationCodeValidator): ActivationCodeValidator

    @Binds
    @Singleton
    abstract fun bindLanguageProvider(impl: AppCompatLanguageProvider): LanguageProvider

    @Binds
    @Singleton
    abstract fun bindWeightRecordRepository(impl: WeightRecordRepositoryImpl): WeightRecordRepository

    @Binds
    @Singleton
    abstract fun bindTagSettingsRepository(impl: TagSettingsRepositoryImpl): TagSettingsRepository

    @Binds
    @Singleton
    abstract fun bindProfileImageRepository(impl: ProfileImageRepositoryImpl): ProfileImageRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRunner(impl: RoomTransactionRunner): TransactionRunner

    @Binds
    @Singleton
    abstract fun bindVaccineTypeRepository(impl: VaccineTypeRepositoryImpl): VaccineTypeRepository

    @Binds
    @Singleton
    abstract fun bindVaccinationRepository(impl: VaccinationRepositoryImpl): VaccinationRepository
}