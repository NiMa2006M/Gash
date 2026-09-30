package com.example.gash.feature.filetransfer

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.gash.core.navigation.GashRoute

fun NavGraphBuilder.fileTransferScreen() {
    composable<GashRoute.FileTransfer> {
        FileTransferScreen()
    }
}