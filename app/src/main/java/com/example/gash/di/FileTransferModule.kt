package com.example.gash.di

import com.example.gash.core.export.SpreadsheetWriter
import com.example.gash.core.export.XlsxSpreadsheetWriter
import com.example.gash.core.network.ConnectivityNetworkMonitor
import com.example.gash.core.network.NetworkMonitor
import com.example.gash.data.export.AnimalsWeightsExportBuilder
import com.example.gash.data.export.ExportBuilder
import com.example.gash.data.export.ExportRepositoryImpl
import com.example.gash.data.export.VaccinationExportBuilder
import com.example.gash.domain.repository.ExportRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FileTransferModule {

    @Binds @Singleton
    abstract fun bindNetworkMonitor(impl: ConnectivityNetworkMonitor): NetworkMonitor

    @Binds
    abstract fun bindSpreadsheetWriter(impl: XlsxSpreadsheetWriter): SpreadsheetWriter

    @Binds @Singleton
    abstract fun bindExportRepository(impl: ExportRepositoryImpl): ExportRepository

    @Binds @IntoSet
    abstract fun bindAnimalsWeightsExportBuilder(impl: AnimalsWeightsExportBuilder): ExportBuilder

    @Binds @IntoSet
    abstract fun bindVaccinationExportBuilder(impl: VaccinationExportBuilder): ExportBuilder
}