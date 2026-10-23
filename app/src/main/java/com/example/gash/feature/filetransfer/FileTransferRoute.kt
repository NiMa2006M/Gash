package com.example.gash.feature.filetransfer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gash.R
import com.example.gash.core.share.shareFile

@Composable
fun FileTransferRoute(viewModel: FileTransferViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val chooserTitle = stringResource(R.string.file_transfer_share_chooser_title)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is FileTransferEvent.ShareFile ->
                    shareFile(context, event.file.filePath, event.file.mimeType, chooserTitle)
            }
        }
    }

    FileTransferScreen(
        uiState = uiState,
        onRetryConnectivity = viewModel::onRetryConnectivity,
        onOptionSelected = viewModel::onOptionSelected,
        onOperationSelected = viewModel::onOperationSelected,
        onExportClick = viewModel::onExportClick
    )
}