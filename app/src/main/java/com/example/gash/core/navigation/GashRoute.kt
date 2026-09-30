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
    // demo
    // @Serializable
    // data class HerdDetail(val herdId: Long) : GashRoute
}