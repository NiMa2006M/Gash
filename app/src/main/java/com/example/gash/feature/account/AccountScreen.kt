package com.example.gash.feature.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gash.R
import com.example.gash.ui.theme.GashBackground_GRAY
import com.example.gash.ui.theme.GashGreen

@Composable
fun AccountScreen(
    onNavigateToGeneralInfo: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val farmName by viewModel.farmName.collectAsStateWithLifecycle()
    var isMenuExpanded by remember { mutableStateOf(false) }
    val profileImagePath by viewModel.profileImagePath.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GashGreen)
    ) {
        AccountHeader(
            farmName = farmName.orEmpty(),
            profileImagePath = profileImagePath,
            onAvatarClick = onNavigateToGeneralInfo,
            isMenuExpanded = isMenuExpanded,
            onMenuClick = { isMenuExpanded = true },
            onDismissMenu = { isMenuExpanded = false },
            onLogout = { isMenuExpanded = false; viewModel.onLogout() }
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = GashBackground_GRAY
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp, top = 16.dp)
            ) {
                item {
                    AccountSectionTitle(
                        title = stringResource(R.string.account_general_info)
                    )
                }

                item {
                    AccountSectionCard {
                        AccountMenuItem(
                            icon = Icons.Default.Person,
                            painter = painterResource(R.drawable.icon_general_info),
                            title = stringResource(R.string.account_general_info),
                            subtitle = stringResource(R.string.account_general_info_dec),
                            onClick = onNavigateToGeneralInfo
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    AccountSectionTitle(
                        title = stringResource(R.string.account_settings)
                    )
                }

                item {
                    AccountSectionCard {
                        AccountMenuItem(
                            icon = Icons.Default.Settings,
                            painter = painterResource(R.drawable.icon_setting),
                            title = stringResource(R.string.account_settings),
                            subtitle = stringResource(R.string.account_settings_dec),
                            onClick = onNavigateToSettings
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    AccountSectionTitle(
                        title = stringResource(R.string.account_about)
                    )
                }

                item {
                    AccountSectionCard {
                        AccountMenuItem(
                            icon = Icons.Default.Info,
                            painter = null,
                            title = stringResource(R.string.account_about),
                            subtitle = stringResource(R.string.account_about_dec),
                            onClick = onNavigateToAbout
                        )
                    }
                }
            }
        }
    }
}