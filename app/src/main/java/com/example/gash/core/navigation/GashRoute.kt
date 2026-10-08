package com.example.gash.core.navigation

import kotlinx.serialization.Serializable


sealed interface GashRoute {

    @Serializable
    data object Welcome : GashRoute

    @Serializable
    data object Activation : GashRoute

    @Serializable
    data object Main : GashRoute

    @Serializable
    data object Home : GashRoute

    @Serializable
    data object Account : GashRoute

    @Serializable
    data object FileTransfer : GashRoute

    @Serializable
    data class HerdDetail(val herdId: Long) : GashRoute

    @Serializable
    data class AnimalDetail(val animalId: Long) : GashRoute
    @Serializable
    data object AccountGeneralInfo : GashRoute

    @Serializable
    data object AccountSettings : GashRoute

    @Serializable
    data object AccountAbout : GashRoute
}

